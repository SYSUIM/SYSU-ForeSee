import json
from jsonpath import jsonpath
import pandas as pd
import logging
import os

log_name = './notice.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


def main(original_path, output_path, final_output_path):
    df = pd.read_csv('../company.csv', header=0, dtype='str')
    groups = df.groupby('industry_code')
    total_notice = []
    id = 1
    for ind, ind_data in groups:
        if not os.path.exists(f'{output_path}/sub/{ind}/'):
            os.mkdir(f'{output_path}/sub/{ind}')
        logger.info(f'Start to build industry: {ind}')
        current_ind_notice = []
        for stock in ind_data.itertuples(index=False):
            stock_code = stock.stock_code
            filename = f'{original_path}{ind}/{stock_code}.json'
            stock_notice = []
            with open(filename, 'r', encoding='utf-8') as f:
                data = json.load(f)
                data = data['notice_info']
                if not data:
                    continue
                notice_num = 0
                for num, item in enumerate(data):
                    this = {}
                    if not item['notice_title']:
                        continue
                    this['notice_id'] = id
                    this['stock_code'] = stock_code
                    this.update(item)
                    stock_notice.append(this)
                    notice_num += 1
                    id += 1
                current_ind_notice.extend(stock_notice)
                logger.info(f'Finish building id for stock: {stock_code}. {str(notice_num)} notice in total!')
                with open(f'{output_path}sub/{ind}/{stock_code}.json', 'w', encoding='utf-8') as f:
                    json.dump(stock_notice, f, ensure_ascii=False, indent=4)
        with open(f'{output_path}{ind}.json', 'w', encoding='utf-8') as f:
            json.dump(current_ind_notice, f, ensure_ascii=False, indent=4)
        total_notice.extend(current_ind_notice)
    # output to final file
    with open(f'{final_output_path}notice_id.json', 'w', encoding='utf-8') as f:
        json.dump(total_notice, f, ensure_ascii=False, indent=4)
    logger.info(f'Total notice_num: {str(id - 1)}')


if __name__ == '__main__':
    original_path = '/data/prj2020/EnterpriseSpider/notice/data/'
    output_path = 'notice_id/'
    final_output_path = './'
    main(original_path, output_path, final_output_path)
