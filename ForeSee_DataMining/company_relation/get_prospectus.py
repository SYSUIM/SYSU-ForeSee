import os
import csv
import logging
import requests
import traceback
import pandas as pd
from bs4 import BeautifulSoup


log_name = './viewPros.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()

headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36'}


def run(input_file, download_path):
    all_data = pd.read_csv(input_file, header=0, dtype='str')
    # 获取每支股票的招股说明书
    count = 1
    for ind, ind_data in all_data.groupby('industry_code'):
        ind_path = download_path + f'{ind}/'
        generatePath(ind_path)
        logger.info(f'Start crawling industry: {ind}, Count:{count}')
        for stock in ind_data.itertuples(index=False):
            file_name = f'{ind_path}/{stock.stock_code}.pdf'
            if os.path.exists(file_name):
                continue
            else:
                logger.info(f'Start crawling stock: {str(stock.stock_code)}')
                _getProsepctus(ind, stock.stock_code, ind_path)
        logger.info(f'Finish crawling industry: {ind}')
        count += 1


def _getProsepctus(ind, stock_code, download_path):
    title, link = _getDownloadLink(ind, stock_code)
    if os.path.exists(f'{download_path}{stock_code}_{title}.pdf'):
        logger.info(f'Have crawn prospectus for stock: {stock_code}')
        return
    if title and link:
        _getDownload(title, link, stock_code, download_path)


def _getDownloadLink(ind, stock_code):
    url = f'http://stock.jrj.com.cn/share,{stock_code},zgsms.shtml'
    try:
        # 获取招股说明书的相关列表
        r = requests.get(url, headers=headers)
        r.raise_for_status()
        r.encoding = r.apparent_encoding
        soup = BeautifulSoup(r.text, 'html.parser')
        soup = soup.find_all(name='ul', attrs={"class": "newlist"})[0]
        all_a = soup.find_all(name='a')
        flag = True     # 检测是否所有项都只是摘要
        if len(all_a) > 1:
            for item in all_a:
                if "摘要" not in item['title'] and '申报稿' not in item['title']:
                    title = _getTitle(item['title'])
                    link = item['href']
                    flag = False
        elif len(all_a) > 0 and flag:
            title = _getTitle(all_a[0]['title'])
            link = all_a[0]['href']
        else:
            logger.error(f'No prospectus for industry: {ind}, stock: {stock_code}')
            return None, None
        return title, link
    except Exception:
        traceback.print_exc()
        logger.error(f'Fail to get industry: {ind}, stock: {stock_code}')


def _getTitle(content):
    if '：' in content:
        title = content.split('：')[-1]
    elif '意向' in content:
        title = '招股意向书'
    elif '说明' in content:
        title = '招股说明书'
    return title


def _getDownload(title, link, stock_code, download_path):
    base_url = 'http://stock.jrj.com.cn'
    # 获取一个招股说明书的对应页面
    try:
        r = requests.get(base_url + link, headers=headers)
        r.raise_for_status()
        r.encoding = r.apparent_encoding
        # 解析得到附件下载链接
        soup = BeautifulSoup(r.text, 'html.parser')
        download = soup.find_all(name='span', attrs={"class": "down"})[0]
        download_link = download.a['href']
        _downloadNotice(download_path, f'{stock_code}_{title}', download_link)
    except IndexError:
        logger.info(f'No appendix for Stock: {stock_code}!')
    except Exception:
        traceback.print_exc()


def _downloadNotice(download_path, title, link):
    filename = f'{download_path}{title}.pdf'
    logger.info(f'Downloading stock: {title[:6]}!')
    r = requests.get(link, headers=headers)
    with open(filename, 'wb') as f:
        f.write(r.content)


# 查看目录是否存在，不存在则生成
def generatePath(path):
    if not os.path.exists(path):
        os.mkdir(path)


if __name__ == '__main__':
    # 设置相关路径
    input_file = 'company.csv'
    download_path = './prospectus/'
    generatePath(download_path)
    run(input_file, download_path)
    # _getProsepctus('BK0438', '002910', download_path)