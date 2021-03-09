# -*- coding: utf-8 -*-


import os
import requests
from bs4 import BeautifulSoup
from fake_useragent import UserAgent
import pandas as pd
import csv
import time

def get_header():
    ua = UserAgent(verify_ssl=False)
    return {'User-Agent': ua.random}

#读取文件信息
def get_compIndex(filepath):
    compDf_all =  pd.read_csv(filepath, usecols=[0,1,3,5],dtype=str)
    return compDf_all

# 生成目录
def generatePath(path):
    if not os.path.exists(path):
        os.mkdir(path)

def get_compNews(compDf_all):
    requests.adapters.DEFAULT_RETRIES = 5
    for index, row in compDf_all.iterrows():
        filename = ''
        industry_code = row['industry_code']
        stock_code = row['stock_code']
        news_title = row['news_title']
        news_link = row['news_link']
        url = news_link[1:-1]
        try:
            r = requests.get(url, headers=get_header())
            r.encoding = r.apparent_encoding
            cont = r.text
            soup = BeautifulSoup(cont,'html.parser')
            indpath = f'./news/'+f'{industry_code}/'
            stdpath = indpath+f'{stock_code}/'
            if not os.path.exists(indpath):
                generatePath(indpath)
            if not os.path.exists(stdpath):
                generatePath(stdpath)
            filename = stdpath+f'{news_title}.txt'
            article = soup.select('#article_content p')[:-7]
            if article == []:
                article = soup.select('#artibody p')[:-1]
            with open(filename,'a') as f:
                for p in article:
                    f.write(p.text.strip())
        except:
            print("Connection refused by the server..")
            time.sleep(5)
            continue
    print('finish all news download')
if __name__ == '__main__':
    compDf_all  = get_compIndex('news_sample.csv')
    get_compNews(compDf_all)