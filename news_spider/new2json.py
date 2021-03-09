# -*- coding: utf-8 -*-
"""
Created on Sat Oct 10 19:48:39 2020

将服务器上news.csv 转成json 然后行业一层目录 股票一个json文件
"""


import json
import csv
import pandas as pd
import os

# 生成目录
def generatePath(path):
    if not os.path.exists(path):
        os.mkdir(path)

def get_compNews(csv_df,industry_code,stock_code,company_name,stock_name):
    csv_df = csv_df[(csv_df['stock_code'] == stock_code)]
    news_title = csv_df['news_title']
    news_time = csv_df['news_time']
    news_link =csv_df['news_link']
    #上述操作完成后应该'news_title','news_time','news_link'存储了那家公司所有新闻信息
    #对信息进行处理
    tmpDic = { "news_title":news_title, 
             "news_time":news_time,
             "news_link":news_link
             }
    newsdf = pd.DataFrame(tmpDic)
    newsdf = newsdf.sort_values(by = "news_time",ascending = False)
    #将一家公司的所有新闻转成一个list
    news = [newsdf.to_dict(orient="records")]
    #一家公司的所有信息
    resDic_comp = {
            "industry_code":industry_code,
            "stock_code":stock_code,
            "stock_name":stock_name,
            "company_name":company_name,
            "news":news, 
            }
    #一家公司所有信息转成一个df
    resDf_comp = pd.DataFrame(resDic_comp)
    indpath = f'./json/'+f'{industry_code}/'
    stdpath = indpath+f'{stock_code}'+'.json'
    if not os.path.exists(indpath):
        generatePath(indpath)
    if os.path.exists(stdpath):
        return
    else:
        resNews_all_js = resDf_comp.to_json(orient="records",force_ascii=False)
        fh = open(stdpath, 'a')
        fh.write(resNews_all_js)
    print("完成"+str(stock_code))

if __name__ == '__main__':
    stock = pd.read_csv('stock_ind_2.csv',dtype=str)
    csvpath = 'news_sample_500.csv'
    df = pd.read_csv(csvpath,dtype=str)
    company_name = stock['company_name']
    industry_code = stock['industry_code']
    stock_code = stock['stock_code']
    st_dic = {
        'industry_code':industry_code,
        'stock_code':stock_code,
        'company_name':company_name,
        'stock_name':stock['short_name']
    }
    st_df = pd.DataFrame(st_dic).drop_duplicates()
    for index, row in st_df.iterrows():
        csv_df = df[(df['stock_code'] == str(row['stock_code']))]
        get_compNews(csv_df,row['industry_code'],row['stock_code'],row['company_name'],row['stock_name'])
    print('finish_all')