import re
import os
import logging
import jieba
import json
from LAC import LAC
import pandas as pd
from gensim import models, corpora
from utils.get_stopwords import stopwords

jieba.load_userdict('utils/stock_vocab.txt')
tfidf_model = models.TfidfModel.load(f'model/stock.tfidf')
dictionary = corpora.Dictionary.load('prepared/dict/stock.dict')
lac = LAC(mode='lac')
# log
log_name = f'./log/core_entity.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()

jieba.load_userdict('utils/index_vocab.txt')


def get_top_org(word, filter=False):
    if filter:
        corpus = dictionary.doc2bow([word])
    else:
        corpus = dictionary.doc2bow([sub for sub in jieba.cut_for_search(word) if sub not in stopwords])
    # 找到最top的词
    result = sorted(tfidf_model[corpus], key=lambda x: x[1], reverse=True)
    if len(result) == 0:
        return None
    this_word = dictionary[result[0][0]]
    type = lac.run(this_word)[1][0]
    if type == 'ORG' and this_word != word:
        return this_word
    elif filter and type in ['ORG', 'PER']:
        return this_word
    else:
        return None


def main():
    df = pd.read_csv('company.csv', dtype='str')
    total = []
    for ind, ind_data in df.groupby('industry_code'):
        logger.info(f'Processing industry: {ind}')
        ind_name = ind_data.iloc[0, 1].strip()
        total.append(ind_name)
        for word in [ind_name[:2], ind_name[2:]]:
            if word != '行业':
                total.append(word)
        for stock in ind_data.itertuples(index=False):
            stock_code = stock.stock_code
            logger.info(f'Processing stock: {stock_code}')
            stock_name = ''.join(stock.stock_name.split())
            company_name = ''.join(stock.company_name.split())
            if 'Ａ' in stock_name:
                stock_name = stock_name.replace('Ａ', 'A')
            if 'Ｂ' in stock.stock_name:
                stock_name = stock_name.replace('Ｂ', 'B')
            total.append(stock_name)
            total.append(company_name)
            strip_name = re.sub('(股份)?有限公司', '', company_name)
            if stock_name != strip_name:
                total.append(strip_name)
            # 获取首批关键词
            for word in [stock_name, company_name, strip_name]:
                key = get_top_org(word)
                if key:
                    total.append(key)
                    logger.info(f'Word: {word} get keyword {key}!')
            # 过滤候选集并提取关键词
            candidate = []
            sim_name = re.sub(r'(燃气|.{1}业|建设|科技|智能|电器|电子|生物|传媒|医疗|软件|技术|银行|证券|制药|药业|航空|高速|电力|股份|能源|集团|物流|国际|环保|路桥|置业|[AB]股?|食品|通信|控股)$', '', stock_name)
            if len(sim_name) < len(stock_name):
                candidate.append(sim_name)
            if 'ST' in stock_name:
                candidate.append(re.sub(r'\*?ST', '', stock_name))
            for word in candidate:
                key = get_top_org(word, filter=True)
                if key:
                    total.append(key)
                    logger.info(f'Word: {word} is a keyword!')
    # output
    logger.info('Outputing ...')
    with open('utils/core_entity.txt', 'w', encoding='utf-8') as f:
        core = sorted(list(set(total)))
        json.dump(core, f, ensure_ascii=False, indent=4)
    logger.info('Finish')


if __name__ == '__main__':
    main()
