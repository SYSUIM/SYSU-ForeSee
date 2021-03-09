# -*- coding: utf-8 -*-
"""
Created on Sun Jan 31 16:41:04 2021

"""
import requests
import re
from fake_useragent import UserAgent
import json
import csv
import time
import pandas as pd
import os

# 生成目录
def generatePath(path):
    if not os.path.exists(path):
        os.mkdir(path)

def get_header():
    ua = UserAgent(verify_ssl=False)
    return {'User-Agent': ua.random}

def get_proxy():
    proxypool_url = 'http://127.0.0.1:5555/random'
    proxies = {'http': 'http://' + requests.get(proxypool_url).text.strip()}
    return proxies

def get_compReasearch(outpath,industry_code,stock_code,stock_name,company_name,stock_exchange):
    page = 1
    resData = pd.DataFrame(columns=("industry_code","stock_code","stock_name","company_name", \
                        "stock_exchange",'research_title','research_time','research_link'))
    research_title = []
    research_time = []
    research_link =[]
    fcnt = 0
    # requests.adapters.DEFAULT_RETRIES = 5
    while(True):
        baseUrl = 'http://stock.finance.sina.com.cn/stock/go.php/vReport_List/kind/search/index.phtml?symbol='+str(stock_code)+'&t1=all&p='+str(page)
        try:
            r = requests.get(baseUrl, headers=get_header())
            #cont -- content
            cont = r.text
            exist = re.findall(r'没有找到相关内容..',cont)
            if exist == []:
                tmpDic = {}
                plink = r'<a target=\"_blank\" title=\".*?\" href=\"(.*?)\">'
                link = re.findall(plink,cont)
                ptitle = r'<a target=\"_blank\" title=\"(.*?)\" href=.*?>'
                title = re.findall(ptitle,cont)
                ptime = r'<a target=\"_blank\" title=\".*?\" href=.*?>[\s\S]*?<td>(\d{4}-\d{2}-\d{2})</td>'
                times = re.findall(ptime,cont)
                research_title.extend(title)
                research_time.extend(times)
                research_link.extend(link)
                page += 1
                # time.sleep(300)
            else:
                break
        except:
            print(str(stock_code)+'-----------'+str(page)+'-----'+str(baseUrl))
            time.sleep(5)
            fcnt += 1
            if (fcnt >=3 ):
                page += 1
                fcnt = 0
            continue
    #上述操作完成后应该'news_title','news_time','news_link'存储了那家公司所有新闻信息
    listlen = len(research_time)
#    stock_name = [stockName]*listlen
    #对信息进行处理
    tmpDic = {
             "industry_code":[industry_code]*listlen,
             "stock_code":[stock_code]*listlen,
             "stock_name":[stock_name]*listlen,
             "company_name":[company_name]*listlen,
             "stock_exchange":[stock_exchange]*listlen,
             "research_title":research_title, 
             "research_time":research_time,
             "research_link":research_link
             }
    resdf = pd.DataFrame(tmpDic)
    resdf['research_link'] = 'http:' + resdf['research_link'].astype('str')
    resdf = resdf.sort_values(by = "research_time",ascending = False)
    resdf.to_csv(outpath,mode='a', header=False,index = False)

    jsonDic = { "research_title":research_title, 
             "research_time":research_time,
             "research_link":research_link
             }
    jsdf = pd.DataFrame(jsonDic)
    jsdf['research_link'] = 'http:' + jsdf['research_link'].astype('str')
    jsdf = jsdf.sort_values(by = "research_time",ascending = False)
    #将一家公司的所有研究报告转成一个list
    reps = [jsdf.to_dict(orient="records")]
    #一家公司的所有信息
    resDic_comp = {
            "industry_code":industry_code,
            "stock_code":stock_code,
            "stock_name":stock_name,
            "company_name":company_name,
            "stock_exchange":stock_exchange,
            "research":reps, 
            }
    #一家公司所有信息转成一个df
    resDf_comp = pd.DataFrame(resDic_comp)
    indpath = f'./research_json/'+f'{industry_code}/'
    stdpath = indpath+f'{stock_code}'+'.json'
    if not os.path.exists(indpath):
        generatePath(indpath)
    if os.path.exists(stdpath):
        return
    else:
        resDf_comp.to_json(stdpath,orient="records",force_ascii=False)
    print("完成"+str(stock_code))


if __name__ == '__main__':
    stock = pd.read_csv('/data/prj2020/EnterpriseSpider/basic/stock_index.csv',dtype=str)
    outpath = '/data/prj2020/EnterpriseSpider/news/stock_research0202v1.csv'
    for index, row in stock.iterrows():
        # get_compReasearch(industry_code,stock_code,stock_name,company_name,stock_exchange)
        get_compReasearch(outpath,row['industry_code'],row['stock_code'],row['short_name'],row['company_name'],row['stock_exchange'])
    print('finish_all')
