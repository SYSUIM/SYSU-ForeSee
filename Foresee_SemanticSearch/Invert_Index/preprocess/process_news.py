import os
import json
import glob
import logging
import pandas as pd
from jsonpath import jsonpath
from multiprocessing import Pool

log_name = './process.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


def get_text(ind_dir, id, stock_code, title):
    txt_name = f'/data/prj2020/EnterpriseSpider/news/news/{ind_dir}/{stock_code}/{title}.txt'
    with open(txt_name, 'r', encoding='utf-8') as f:
        text = ''.join([line.strip('\n') for line in f.readlines()])
    return text.replace('\t', '')


def single_process(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        data = json.load(f)
    logger.info(f'Processing industry: {filename[-11:-5]}   total:{str(len(data))}')
    with open(f'sub/{filename[-11:-5]}.txt', 'w', encoding='utf-8') as sub_f:
        for item in data:
            id, stock_code = item['news_id'], item['stock_code']
            text = get_text(filename[-11:-5], id, stock_code, item['news_title'])
            sub_f.write(str(id) + '\t' + text + '\n')


def main():
    file_list = glob.glob('../../id/news_id/BK*.json')
    p = Pool(20)
    for filename in sorted(file_list):
        p.apply_async(single_process, args=(filename,))
    p.close()
    p.join()


def news_tokenized(news_id_path):
    with open(f'news_tokenized.txt', 'w', encoding='utf-8') as f:
        df = pd.read_csv('../../company.csv', dtype='str')
        for ind, ind_data in df.groupby('industry_code'):
            logger.info(f'Processing industry: {ind}')
            for stock in ind_data.itertuples(index=False):
                stock_code = stock.stock_code
                logger.info(f'Processing stock: {stock_code}')
                # preprocess name
                stock_name = ''.join(stock.stock_name.split())
                company_name = ''.join(stock.company_name.split())
                if 'Ａ' in stock_name:
                    stock_name = stock_name.replace('Ａ', 'A')
                if 'Ｂ' in stock.stock_name:
                    stock_name = stock_name.replace('Ｂ', 'B')
                # get id list
                filename = f'{news_id_path}/{ind}/{stock_code}.json'
                if os.path.exists(filename):
                    with open(filename, 'r', encoding='utf-8') as f1:
                        data = json.load(f1)
                        id_list = jsonpath(data, '$..news_id')
                    if not id_list:
                        continue
                    for this_id in id_list:
                        f.write(stock_name + '\t' + str(this_id) + '\n')
                        f.write(company_name + '\t' + str(this_id) + '\n')


def get_title(news_id_path):
    with open(f'news_title.txt', 'w', encoding='utf-8') as f:
        df = pd.read_csv('../../company.csv', dtype='str')
        for ind, ind_data in df.groupby('industry_code'):
            logger.info(f'Processing industry: {ind}')
            for stock in ind_data.itertuples(index=False):
                stock_code = stock.stock_code
                with open(f'{news_id_path}/{ind}/{stock_code}.json') as f1:
                    data = json.load(f1)
                    logger.info(f'Processing stock: {stock_code} total: {str(len(data))}')
                    for item in data:
                        text = item['news_title'].replace('\t', '').replace('\n', '')
                        f.write(str(item['news_id']) + '\t' + text + '\n')
        logger.info(f'Finish!')


if __name__ == '__main__':
    news_id_path = '/data/prj2020/EnterpriseSpider/index/id/news_id/sub'
    # news_tokenized(news_id_path)
    # main()
    get_title(news_id_path)
