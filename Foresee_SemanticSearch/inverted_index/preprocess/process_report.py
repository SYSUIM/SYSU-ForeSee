import os
import json
import glob
import logging

log_name = './process.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


def preprocess_report():
    filelist = glob.glob('/data/prj2020/EnterpriseSpider/analysis/newdata/*/*/*_content.json')
    # filelist = ['Internet_article_content.json']
    with open('industry_content.txt', 'w', encoding='utf-8') as f:
        count = 1
        for file in filelist:
            logger.info(f'Processing {str(count)}/{str(len(filelist))} files ...')
            with open(file, 'r', encoding='utf-8') as f1:
                data = json.loads(f1.read())
                all_data = zip(data.keys(), data.values())
                for id, content in all_data:
                    f.write(id + '\t' + content + '\n')
            count += 1


if __name__ == '__main__':
    preprocess_report()