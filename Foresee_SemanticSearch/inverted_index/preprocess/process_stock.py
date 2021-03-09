import os
import re
import json
import glob
import logging
import pandas as pd

log_name = './stock.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


def name_based_index():
    """能够通过股票名/公司名检索到股票代码"""
    with open(f'stock_tokenized.txt', 'w', encoding='utf-8') as f:
        df = pd.read_csv('../../company.csv', dtype='str')
        for ind, ind_data in df.groupby('industry_code'):
            logger.info(f'Processing industry: {ind}')
            for stock in ind_data.itertuples(index=False):
                # 分别以股票名和公司名建索引
                stock_code = stock.stock_code
                stock_name = ''.join(stock.stock_name.split())
                company_name = ''.join(stock.company_name.split())
                if 'Ａ' in stock_name:
                    stock_name = re.sub('Ａ', 'A', stock.stock_name)
                if 'Ｂ' in stock.stock_name:
                    stock_name = re.sub('Ｂ', 'B', stock.stock_name)
                raw_set = set([stock_name, company_name])
                # 集合求并集
                stored = set()
                for word in raw_set:
                    if word.strip() and word not in ['*', '(', ')']:
                        word = ''.join(word.split()).lower()
                        for i in range(len(word)):
                            for j in range(i + 1,  len(word) + 1):
                                this_word = word[i:j].lstrip(')').lower()
                                if this_word not in ['*', '(', ')'] and this_word not in stored:
                                    f.write(this_word + '\t' + stock.stock_code + '\n')
                                    stored.add(this_word)
                logger.info(f'Processed stock: {stock_code}. Token num: {len(stored)}')


def data_based_index():
    """能够通过股票的属性值检索到股票代码"""
    indlist = glob.glob('/data/prj2020/EnterpriseSpider/basic/BasicInfoFile/BK*/')
    with open(f'stock_tokenized.txt', 'a', encoding='utf-8') as key_f, open(f'stock_preprocess.txt', 'w', encoding='utf-8') as pre_f:
        for ind in sorted(indlist):
            ind_code = ind[-7:-1]
            logger.info(f'Processing industry: {ind_code}')
            filelist = glob.glob(f'/data/prj2020/EnterpriseSpider/basic/BasicInfoFile/{ind_code}/*.json')
            for file in filelist:
                with open(file, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    pre_f.write(data['stock_code'] + '\t')
                    stock_code = data['stock_code']
                    logger.info(f'Processing stock: {stock_code}')
                    for key, value in data.items():
                        if isinstance(value, str):
                            value = ''.join(value.split('\n')).lower()
                            if 'Ａ' in value:
                                value = re.sub('Ａ', 'A', value)
                            if 'Ｂ' in value:
                                value = re.sub('Ｂ', 'B', value)
                        if key in ['industry_code', 'stock_code', 'entLogo', 'regCapital', 'annualDate', 'openTime', 'realCapital', 'openStatus']:
                            continue
                        if key in ['entName', 'district', 'entType', 'regAddr'] and value != '-':   # 双份输出
                            key_f.write(value + '\t' + data['stock_code'] + '\n')
                            pre_f.write(value)
                        elif key in ['describe', 'scope'] and value != '-':
                            pre_f.write(value)
                        else:
                            if value != '-' and '*' not in value:
                                if isinstance(value, list):
                                    for v in value:
                                        key_f.write(v + '\t' + data['stock_code'] + '\n')
                                else:
                                    key_f.write(value + '\t' + data['stock_code'] + '\n')
                    pre_f.write('\n')


if __name__ == '__main__':
    name_based_index()
    data_based_index()
