import json
import pandas as pd
import logging
import os
import glob

log_name = './news.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


def main(news_text_path, news_path, output_path):
    df = pd.read_csv('../company.csv', header=0, dtype='str')
    groups = df.groupby('industry_code')
    id = 1
    whole_data = []
    for ind, ind_data in groups:
        if not os.path.exists(f'{output_path}/sub/{ind}/'):
            os.mkdir(f'{output_path}/sub/{ind}')
        logger.info(f'Start to build industry: {ind}')
        all_data = []
        for stock in ind_data.itertuples(index=False):
            stock_data = []
            stock_code = stock.stock_code
            logger.info(f'Processing stock: {stock_code}')
            filename = f'{news_path}{ind}/{stock_code}.json'
            with open(filename, 'r', encoding='utf-8') as f:
                data = json.load(f)[0]['news']
            for news in data:
                item = {}
                title, link = news['news_title'], news['news_link']
                txt_name = f'{news_text_path}{ind}/{stock_code}/{title}.txt'
                if (isinstance(title, str) and isinstance(link, str) and os.path.exists(txt_name)):
                    if os.path.getsize(txt_name) > 0:
                        logger.info(f'Id {str(id)} for stock {stock_code} news_title: {title}')
                        item['news_id'] = id
                        item['stock_code'] = stock_code
                        item['stock_name'] = stock.stock_name
                        item['company_name'] = stock.company_name
                        item.update(news)
                        if len(item) > 0:
                            all_data.append(item)
                            stock_data.append(item)
                        id += 1
            if len(stock_data) > 0:
                with open(f'{output_path}/sub/{ind}/{stock_code}.json', 'w', encoding='utf-8') as f:
                    json.dump(stock_data, f, ensure_ascii=False, indent=4)
        with open(f'{output_path}{ind}.json', 'w', encoding='utf-8') as f:
            json.dump(all_data, f, ensure_ascii=False, indent=4)
        whole_data.extend(all_data)
    with open(f'news_id.json', 'w', encoding='utf-8') as f:
        json.dump(whole_data, f, ensure_ascii=False, indent=4)


def replenish_empty(news_path, output_path):
    df = pd.read_csv('../company.csv', header=0, dtype='str')
    groups = df.groupby('industry_code')
    whole_data = []
    for ind, ind_data in groups:
        logger.info(f'Start to build industry: {ind}')
        for stock in ind_data.itertuples(index=False):
            stock_code = stock.stock_code
            with open(f'{output_path}/sub/{ind}/{stock_code}.json', 'r', encoding='utf-8') as now_f:
                data = json.load(now_f)
            if not len(data) > 0:
                logger.info(f'Replenish stock: {stock_code}')
                with open(f'{news_path}{ind}/{stock_code}.json', 'r', encoding='utf-8') as old_f:
                    raw_data = json.load(old_f)
                    for item in raw_data[0]['news']:
                        this = {}
                        this['news_id'] = -1
                        this.update(item)
                        whole_data.append(this)
    with open(f'{output_path}/lack.json', 'w', encoding='utf-8') as f:
        json.dump(whole_data, f, ensure_ascii=False, indent=4)
    logger.info(f'Finish replenishing')


def merge(input_path):
    filelist = glob.glob(input_path + '/*.json')
    whole_data = []
    count = 0
    for filename in sorted(filelist):
        print('processing ' + filename[-11:-5])
        with open(filename, 'r', encoding='utf-8') as f:
            data = json.load(f)
            whole_data.extend(data)
        count += len(data)
    with open('news_id.json', 'w', encoding='utf-8') as f:
        json.dump(whole_data, f, ensure_ascii=False, indent=4)
    print(str(count) + ' in total!')


if __name__ == '__main__':
    news_txt_path = '/data/prj2020/EnterpriseSpider/news/news/'
    news_json_path = '/data/prj2020/EnterpriseSpider/news/news_json/'
    output_path = '/data/prj2020/EnterpriseSpider/index/id/news_id'
    # main(news_txt_path, news_json_path, output_path)
    replenish_empty(news_json_path, output_path)
    merge(output_path)
