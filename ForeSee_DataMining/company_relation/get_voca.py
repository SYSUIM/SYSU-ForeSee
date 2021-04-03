# -*- coding: utf-8 -*-
"""
Created on Wed Feb  3 18:04:27 2021
完善词表
"""

import pandas as pd

#对应格式为名称,公司名称,公司介绍,工商,地址,工商注册id,成立时间,法人代表,注册资金,统一信用代码,网址

stock = pd.read_csv('/data/prj2020/EnterpriseSpider/basic/stock_index.csv')
comp = stock['company_name']
short = stock['short_name']

f = open("customEnty0203.txt",'a')
length = len(comp)
i = 0
while(i<length):
    f.write(comp[i])
    f.write("/ORG")
    f.write('\n')
    f.write(short[i])
    f.write("/ORG")
    f.write('\n')
    i+=1
    
path2 = '/data/prj2020/EnterpriseSpider/news/analysis/FinancialDatasets/data/gongshang10K.csv'
df = pd.read_csv(path2)
comp = df['公司名称']
short = df['名称']

f = open("customEnty0203.txt",'a')
length = len(comp)
i = 0
while(i<length):
    f.write(comp[i])
    f.write("/ORG")
    f.write('\n')
    f.write(short[i])
    f.write("/ORG")
    f.write('\n')
    i+=1


