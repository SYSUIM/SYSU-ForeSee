import os
import re
import csv
import time
import json
import glob
import pdfplumber
import pandas as pd
from jsonpath import jsonpath


# step1.锁定目录的页数范围
def find_catalogue(pdf):
    for page in pdf.pages[:30]:
        text = page.extract_text()
        # 去除页首和页尾
        text = text.split('\n')[1:-1]
        for line in text[:2]:
            line = line.replace(' ', '')
            if '目录' in line:
                begin = page.page_number
                break
        # 判断是否已经遍历过目录，进入【释义】部分
        if '释义' in text[0]:
            end = page.page_number
            return begin, end

    if begin:
        return begin, page.page_number
    else:
        return None, None


# step2.在目录中寻找【竞争地位】所在页数范围
def get_compete_section(pages):
    for page in pages:
        text = page.extract_text()
        # 去除页首和页尾
        text = text.split('\n')[1:-1]
        line_num = 0
        for num, line in enumerate(text):
            if '竞争' in line and '同业竞争' not in line:
                begin = int(line.split('.')[-1].strip())
                line_num = num
            if num == line_num + 1 and line_num > 0:
                end = int(line.split('.')[-1].strip())
                return begin, end
    return None, None


# step3. 从【竞争地位】中抽取竞争对手
def get_all_competitors(lines, stock_dict, compet_path):
    # 创建临时目录
    generatePath(f'{compet_path}detail/')
    # 删除此前的临时记录文件，防止在旧数据上追加
    stock_code = stock_dict['stock_code']
    result_filename = f'{compet_path}{stock_code}_competitors.json'
    detail_filename = f'{compet_path}detail/{stock_code}_competitors.json'
    if os.path.exists(detail_filename):
        os.remove(detail_filename)

    # 开始抽取竞争对手
    section = ''
    section_dict = {'title': ''}
    for line in lines:
        if len(line) < 25:
            title = extract_title(line)
            if title:
                # 说明当前为下一节的标题，需要对此前积累的上一节正文进行抽取
                competitors = get_section_competitors(section)
                # 写入上一节的相关信息
                if competitors:
                    section_dict['competitors'] = competitors
                    record_competitors(section_dict, detail_filename)
                else:
                    m = extract_title(section_dict['title'], company=True)
                    if m:
                        section_dict['competitors'] = [m.group(0)]
                        record_competitors(section_dict, detail_filename)
                # 清空上一节的正文，开启下一节的积累
                section = ''
                section_dict['title'] = title
            else:
                section += line.strip()    # 对非标题的短文本继续积累
        else:
            # 累积每节正文
            section += line.strip()
    if section:
        competitors = get_section_competitors(section)
        # 写入上一节的相关信息
        if competitors:
            section_dict['competitors'] = competitors
            record_competitors(section_dict, detail_filename)

    # 根据数据传输要求，产出对应文件
    if os.path.exists(detail_filename):
        output_competitors(result_filename, detail_filename, stock_dict)
    else:
        print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    No competitors in prospectus for stock: {stock_code}')


# step3.1 抽取各小节标题
def extract_title(short_text, company=False):
    '''
    - 三种待处理情况
        - 无括号型：1、或1.
        - 小括号型：（1）（一）
        - 圈圈型：①
        - 需要标识非标题的短文本
    '''
    ind = 1 if company else 0
    try:
        if not len(short_text) > 3:  # 处理空字符串
            return None
        elif short_text[0] == '（':
            strs = [r'（\d?\w?）(\w+)', r'（\d?\w?）(\w+(有限)?公司)']
            m = re.match(strs[ind], short_text)
            title = m.group(1)
        elif short_text[1] in ('.', '、') or short_text[2] in ('.', '、'):
            strs = [r'[\d\w]{1,2}[.、](\w+)', r'[\d\w]{1,2}[.、](\w+(有限)?公司)']
            m = re.match(strs[ind], short_text)
            title = m.group(1)
        else:
            strs = [r'[①②③④⑤⑥⑦⑧⑨⑩](\w+)', r'[①②③④⑤⑥⑦⑧⑨⑩](\w+(有限)?公司)']
            m = re.match(strs[ind], short_text)
            title = m.group(1)
    except AttributeError:
        return None

    return title


# step3.2 抽取各小节正文中的多个竞争对手
def get_section_competitors(section):
    competitors = []
    # 只处理各个句子中顿号的各个公司
    for sent in section.split('。'):
        sent.replace('自有', '')
        if '包' in sent:
            m = re.finditer(r'(包括|包含)([\w\s、.]+)?等', sent)
        else:
            m = re.finditer(r'([含如有\s]){1,2}([\w\s、.]+)?等', sent)
        for subtext in m:
            try:
                if ('品牌' in sent or '竞争' in sent) and '产品' not in sent:
                    name_list = re.split('[、和如]', subtext.group(2))
                    competitors.extend([sub.strip() for sub in name_list])
            except Exception:
                print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Text Extraction: Error in subtext {subtext}')

    return list(set(competitors))


# step3.3 记录各小节的标题及抽取出的竞争关系
def record_competitors(section_dict, filename):
    print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Processing section: {section_dict["title"]} with {str(len(section_dict["competitors"]))} competitors')
    if os.path.exists(filename):
        with open(filename, 'r', encoding='utf-8') as f:
            data = json.loads(f.read())
    else:
        data = []
    # 追加新数据
    data.append(section_dict)
    with open(filename, 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=4, ensure_ascii=False)


# step3.4 基于各小节的抽取，去重并写出整个文档的抽取结果
def output_competitors(final_filename, temp_filename, stock_dict):
    # 区分有表格提取和无表格提取的情况
    if os.path.exists(final_filename):
        with open(final_filename, 'r', encoding='utf-8') as f:
            competitors = json.load(f)['competitors']
    else:
        competitors = []
    # 加入各章节的竞争对手
    with open(temp_filename, 'r', encoding='utf-8') as f2:
        content = json.loads(f2.read())
        # 获取detail中每个元素的competitors数据
        all_competitors = jsonpath(content, '$..competitors')
        for sub_compete in all_competitors:
            competitors.extend(sub_compete)
    # 补全stock_dict
    stock_dict['competitors'] = list(set(competitors))
    print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Merging {str(len(stock_dict["competitors"]))} competitors of stock: {stock_dict["stock_code"]}')
    with open(final_filename, 'w', encoding='utf-8') as f:
        json.dump(stock_dict, f, indent=4, ensure_ascii=False)


def extract_competition_part(pdf_name, stock_dict, compet_path):
    stock_code = stock_dict['stock_code']
    whole_text = ''
    try:
        with pdfplumber.open(pdf_name) as pdf:
            cataBegin, cataEnd = find_catalogue(pdf)
            print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Get catalog. Begin page:{str(cataBegin)}. End page:{str(cataEnd)}')
            # 传入目录部分获取竞争地位章节
            begin, end = get_compete_section(pdf.pages[cataBegin:cataEnd])
            print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Get competition section. Begin page:{str(begin)}. End page:{str(end)}')
            # 此前统计的所有页数均从1开始，而列表获取从0开始，所以end和begin都应减1
            competitors = []
            for page in pdf.pages[begin-1:end]:
                text = page.extract_text()
                # 去除页首和页尾
                lines = text.split('\n')[1:-1]
                whole_text += '\n'.join(lines)
                whole_text += '\n'
                tables = page.extract_tables()
                if tables:
                    for table in tables:
                        for col in table[0]:
                            if not col:
                                break
                            if '公司名称' in col or '竞争对手' in col or '企业名称' in col or '品牌名称' in col:
                                df = pd.DataFrame(table[1:], columns=table[0])
                                competitors.extend(df[col].tolist())
            if competitors:
                with open(f'{compet_path}{stock_code}_competitors.json', 'w', encoding='utf-8') as f:
                    stock_dict['competitors'] = competitors
                    json.dump(stock_dict, f, ensure_ascii=False, indent=4)
            return whole_text
    except Exception:
        return


def extract_competitors(pdf_name, stock_dict, text_path, compet_path):
    # 获取【竞争地位】部分的文本
    # 将该部分写出到文件
    stock_code = stock_dict['stock_code']
    text_filename = f'{text_path}{stock_code}_competiton_part.txt'
    if not os.path.exists(text_filename):
        whole_text = extract_competition_part(pdf_name, stock_dict, compet_path)
        # 考虑到页面衔接的问题，必须整个部分写出，再重新读入
        if whole_text:
            with open(text_filename, 'w', encoding='utf-8') as f:
                f.write(whole_text)
        else:
            print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Fail to get competiton part for stock: {stock_code}.')
    else:
        whole_text = True
    # 读入【竞争地位】部分
    # 进行关系抽取
    if whole_text:
        with open(text_filename, 'r', encoding='utf-8') as f:
            num = 0
            lines = f.readlines()
            for line in lines:
                if '竞争' in line:  # 去除非竞争的部分
                    get_all_competitors(lines[num:], stock_dict, compet_path)
                    break
                else:
                    num += 1


def write_fail_stock(stock_dict):
    # stock为命名元组形式
    filename = 'fail_record.csv'
    with open(filename, 'a', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        if not os.path.exists(filename):
            writer.writerow(stock_dict.keys())
        writer.writerow(stock_dict.values())


def main(pdf_path, text_path, compet_path):
    # 读入企业和公司数据
    all_data = pd.read_csv('company.csv', header=0, dtype=str)
    for ind, ind_data in all_data.groupby('industry_code'):
        ind_text_path = f'{text_path}{ind}/'
        ind_compet_path = f'{compet_path}{ind}/'
        generatePath([ind_text_path, ind_compet_path])
        print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Start processing industry: {ind}')
        fail_num = 0
        for stock in ind_data.itertuples(index=False):
            # 判断是否已处理过竞争关系
            if os.path.exists(f'{ind_compet_path}{stock.stock_code}_competitors.json'):
                print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Already processed stock: {str(stock.stock_code)}')
                continue
            else:   # 判断是否存在招股说明书
                pdf_list = glob.glob(f'{pdf_path}/{ind}/{stock.stock_code}_*.pdf')
                stock_dict = stock._asdict()
                if pdf_list:
                    print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Start extracting stock: {str(stock.stock_code)}')
                    extract_competitors(pdf_list[0], stock_dict, ind_text_path, ind_compet_path)
                else:
                    fail_num += 1
                    write_fail_stock(stock_dict)
        print(f'[{time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}]    Finish processing industry: {ind}. Fail: {str(fail_num)}/{str(len(ind_data))}')


# 查看目录是否存在，不存在则生成
def generatePath(path):
    if type(path) == str:
        if not os.path.exists(path):
            os.mkdir(path)
    elif type(path) == list:
        for p in path:
            if not os.path.exists(p):
                os.mkdir(p)


if __name__ == '__main__':
    pdf_path = '/data/prj2020/EnterpriseSpider/notice/prospectus/'
    text_path = './text/'
    compet_path = './competitors/'
    if os.path.exists('fail_record.csv'):
        os.remove('fail_record.csv')
    main(pdf_path, text_path, compet_path)