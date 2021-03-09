# -*- coding: utf-8 -*-
"""
Created on Sun Oct 18 10:44:53 2020
爬取上市公司研究报告的内容
"""

import os
import requests
from bs4 import BeautifulSoup
from fake_useragent import UserAgent
import pandas as pd
import csv
import time
from multiprocessing import Pool
import re

def get_header():
    ua = UserAgent(verify_ssl=False)
    return {'User-Agent': ua.random}
	
def get_proxy():
    proxypool_url = 'http://127.0.0.1:5555/random'
    proxies = {'http': 'http://' + requests.get(proxypool_url).text.strip()}
    return proxies
	
# 生成目录
def generatePath(path):
    if not os.path.exists(path):
        os.mkdir(path)

def get_compNews(compDf_all):

    for index, row in compDf_all.iterrows():       
        filename = ''
        industry_code = row['industry_code']
        stock_code = row['stock_code']
        news_title = row['research_title']
        news_link = row['research_link']
        try:
            r = requests.get(news_link, headers=get_header())
            r.encoding = r.apparent_encoding
            cont = r.text
            # 你的 IP 存在异常访问
            exist = re.findall(r'拒绝访问',cont)
            if exist != []:
                print(str(industry_code)+'\t'+str(stock_code)+'\t'+str(news_title)+'\t'+"拒绝访问")
                continue
            soup = BeautifulSoup(cont,'html.parser')
            indpath = f'./research/'+f'{industry_code}/'
            stdpath = indpath+f'{stock_code}/'
            if not os.path.exists(indpath):
                generatePath(indpath)
            if not os.path.exists(stdpath):
                generatePath(stdpath)
            filename = stdpath+f'{news_title}.txt'
            if os.path.exists(filename):
                continue
            else:
                article = soup.select('.blk_container p')
                if article == []:
                    time.sleep(50)
                with open(filename,'a',encoding='utf8') as f:
                    for p in article:
                        f.write(p.text)
                print(str(stock_code)+'\t'+str(news_title)+'\t'+"finish it")
        except:
            print(str(stock_code)+'\t'+str(news_title)+'\t'+"Connection refused by the server..")
            time.sleep(50)
            continue
    print('finish all research report download')
if __name__ == '__main__':
    datapath = '/data/prj2020/EnterpriseSpider/news/stock_research0202.csv'
    compDf_all = pd.read_csv(datapath, dtype=str)
    get_compNews(compDf_all)
    print('finish all process')  


