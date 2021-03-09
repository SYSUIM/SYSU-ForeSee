import os
import re
import json
import logging
import pandas as pd

log_name = './notice_process.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


def preprocess_notice():
    df = pd.read_csv('../../company.csv', dtype='str')
    with open('notice_tokenized.txt', 'w', encoding='utf-8') as name_f, open('notice_preprocess.txt', 'w', encoding='utf-8') as pre_f:
        for ind, ind_data in df.groupby('industry_code'):
            logger.info(f'Processing industry: {ind}')
            for stock in ind_data.itertuples(index=False):
                stock_code = stock.stock_code
                id_list = []
                filename = f'../../id/notice_id/sub/{ind}/{stock_code}.json'
                if not os.path.exists(filename):
                    logger.info(f'Lack of notice. stock: {stock_code}')
                    continue
                with open(filename, 'r', encoding='utf-8') as f1:
                    data = json.load(f1)
                    # 先完成预处理
                    for item in data:
                        if not item['notice_title']:
                            print(filename)
                            continue
                        title = item['notice_title']
                        if 'Ａ' in title:
                            title = re.sub('Ａ', 'A', title)
                        if 'Ｂ' in title:
                            title = re.sub('Ｂ', 'B', title)
                        pre_f.write(str(item['notice_id']) + '\t' + title + '\n')
                        id_list.append(str(item['notice_id']))
                    # 分别以股票名和公司名建索引
                    stock_name = ''.join(stock.stock_name.split())
                    company_name = ''.join(stock.company_name.split())
                    if 'Ａ' in stock_name:
                        stock_name = re.sub('Ａ', 'A', stock.stock_name)
                    if 'Ｂ' in stock.stock_name:
                        stock_name = re.sub('Ｂ', 'B', stock.stock_name)
                    raw_set = set([stock_name, company_name])
                    # 集合求并集
                    # 记录当前股票存过的词
                    stored = set()
                    for word in raw_set:
                        if word.strip() and word not in ['*', '(', ')']:
                            word = ''.join(word.split()).lower()
                            for i in range(len(word)):
                                for j in range(i + 1,  len(word) + 1):
                                    this_word = word[i:j].lstrip(')').strip().strip('\t').lower()
                                    if this_word not in stored and this_word not in ['*', '(', ')']:
                                        name_f.write(this_word + '\t' + ' '.join(id_list) + '\n')
                                        stored.add(this_word)
                    logger.info(f'Processed stock: {stock_code}. Token num: {len(stored)}')


if __name__ == '__main__':
    preprocess_notice()
