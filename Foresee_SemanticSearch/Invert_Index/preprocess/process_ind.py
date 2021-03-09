import pandas as pd
import os
import re
import logging

log_name = './industry_index.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()

# 存储每个行业码已经对应过的词，不记录重复对应
# 以防提高细粒度无意义词的词频
ind_hash = {}


def content_based_index():
    """基于行业名称建细粒度索引"""
    df = pd.read_excel('../industry.xlsx', header=0)
    with open('industry_tokenized.txt', 'w', encoding='utf-8') as name_f, open('industry_preprocess.txt', 'w', encoding='utf-8') as pre_f:
        for ind_data in df.itertuples(index=False):
            ind = ind_data.industry_code.strip()
            ind_hash[ind] = []
            pre_f.write(ind + '\t' + ''.join(ind_data.introduction.strip().split('\n')) + '\n')
            word = ind_data.industry_name.strip()
            for i in range(len(word)):
                for j in range(i + 1,  len(word) + 1):
                    this_word = word[i:j].lower()
                    if this_word not in ['*', '(', ')'] and this_word not in ind_hash[ind]:
                        name_f.write(this_word + '\t' + ind + '\n')
                        ind_hash[ind].append(this_word)


def stock_based_index():
    """建行业下各股票名/公司名的索引，使得股票能检索到自己所在的行业"""
    with open(f'industry_tokenized.txt', 'a', encoding='utf-8') as f:
        df = pd.read_csv('../../company.csv', dtype='str')
        for ind, ind_data in df.groupby('industry_code'):
            logger.info(f'Processing industry: {ind}')
            for stock in ind_data.itertuples(index=False):
                # 分别以股票名和公司名建索引
                stock_name = ''.join(stock.stock_name.split())
                company_name = ''.join(stock.company_name.split())
                if 'Ａ' in stock_name:
                    stock_name = re.sub('Ａ', 'A', stock.stock_name)
                if 'Ｂ' in stock.stock_name:
                    stock_name = re.sub('Ｂ', 'B', stock.stock_name)
                raw_set = set([stock_name, company_name])
                # 集合求并集
                for word in raw_set:
                    if word.strip():
                        word = ''.join(word.split()).lower()
                        for i in range(len(word)):
                            for j in range(i + 1,  len(word) + 1):
                                this_word = word[i:j].lstrip(')').strip().strip('\t').lower()
                                if this_word not in ['*', '(', ')'] and this_word not in ind_hash[ind]:
                                    f.write(this_word + '\t' + ind.strip('\t') + '\n')
                                    ind_hash[ind].append(this_word)


if __name__ == '__main__':
    content_based_index()
    stock_based_index()