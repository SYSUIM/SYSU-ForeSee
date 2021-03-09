# -*- coding: utf-8 -*-
"""
Created on Sun Feb 28 10:48:13 2021
"""


import pandas as pd
#from LAC import LAC
import re
#import os
import ast
from collections import Counter
import time   
#import numpy as np

path1 = '/data/prj2020/EnterpriseSpider/news/relation/competition_2021-02-28-19-51-27.csv'
df1 = pd.read_csv(path1)
df1['stock_code'] = df1['stock_code'].astype(str).str.zfill(6)
#industry_code,stock_code,company_name,stock_name,compete
path2 = '/data/prj2020/EnterpriseSpider/news/relation/rule_competitors.csv'
df2 = pd.read_csv(path2)
df2['stock_code'] = df2['stock_code'].astype(str).str.zfill(6)

ind = 0 #用于遍历股票代码
stock_code_list = list(df1['stock_code'])
lenstk = len(stock_code_list)
final_coll_list = []
stock_name_list = list(df1['stock_name'])

while(ind < lenstk):
    curcode = stock_code_list[ind]
    curname = stock_name_list[ind]
    final_coll = []
    tmp_list1 = []
    tmp_list2 = []
    try:
        tmp_list1 = [list(df1[(df1['stock_code'] == curcode)]['compete'])[0]]
        tmp_df2 = df2[(df2['stock_code'] == curcode)]
        tmp_list2 = list(tmp_df2.groupby(['stock_code'],as_index=True)["compete"].apply(list))
        coll_list1 = []
        coll_list2 = tmp_list2
        coll_list1.extend([ast.literal_eval(x) for x in tmp_list1])
#        coll_list2.extend([ast.literal_eval(x) for x in tmp_list2])
        if(len(coll_list1)>0):
            new_coll_list1 = coll_list1[0]
            lencoll1 = len(new_coll_list1)
            # 用于对coll1进行处理
            ind1 = 0 
            while(ind1 < lencoll1):
                tmpwd = new_coll_list1[ind1]
                tmpwd = tmpwd.strip().replace(u'\u3000', u'').replace(u'\xa0', u'')
                if((len(tmpwd)> 1)& (len(tmpwd)<9)& (curname not in tmpwd)& ('公司'!= tmpwd)&('央行' not in tmpwd)&('本公司' not in tmpwd)& ('国家' not in tmpwd)& ('全国' not in tmpwd)& ('人大' not in tmpwd)& ('协会' not in tmpwd)& ('组织' not in tmpwd)& ('国际' not in tmpwd)& ('世界' not in tmpwd)& ('国务' not in tmpwd)& ('证监' not in tmpwd)& ('交所' not in tmpwd)& ('交易所' not in tmpwd)&(bool(re.search(r'[\d|\W|局|报|监|部|政|党]', tmpwd))==False)):
                    final_coll.append(tmpwd)
                ind1 += 1
        if(len(coll_list2) > 0):
            new_coll_list2 = coll_list2[0]
            lencoll2 = len(new_coll_list2)
            # 用于对coll2进行处理
            ind2 = 0 
            while(ind2 < lencoll2):
                tmpwd = new_coll_list2[ind2]
                tmpwd = tmpwd.strip().replace(u'\u3000', u'').replace(u'\xa0', u'')
                if((len(tmpwd)> 1)& (len(tmpwd)<9)& (curname not in tmpwd)&('公司'!= tmpwd)&('央行' not in tmpwd)&('本公司' not in tmpwd)& ('国家' not in tmpwd)& ('全国' not in tmpwd)& ('人大' not in tmpwd)& ('协会' not in tmpwd)& ('组织' not in tmpwd)& ('国际' not in tmpwd)& ('世界' not in tmpwd)& ('国务' not in tmpwd)& ('证监' not in tmpwd)& ('交所' not in tmpwd)& ('交易所' not in tmpwd)&(bool(re.search(r'[\d|\W|局|报|监|部|政|党]', tmpwd))==False)):
                    final_coll.append(tmpwd)
                ind2 += 1
        final_coll_list.append(dict(Counter(final_coll)))
        ind += 1
    except Exception as e:
        #因为df2中可能没有对应的stock_code 所以要进行处理
        print(str(curcode)+'\t'+'df2中可能没有对应的stock_code')
        tmp_list1 = [list(df1[(df1['stock_code'] == curcode)]['compete'])[0]]
        coll_list1 = []
        coll_list1.extend([ast.literal_eval(x) for x in tmp_list1])
        if(len(coll_list1)>0):
            new_coll_list1 = coll_list1[0]
            lencoll1 = len(new_coll_list1)
            # 用于对coll1进行处理
            ind1 = 0 
            while(ind1 < lencoll1):
                tmpwd = new_coll_list1[ind1]
                tmpwd = tmpwd.strip().replace(u'\u3000', u'').replace(u'\xa0', u'')
                if((len(tmpwd)> 1)& (len(tmpwd)<9)& (curname not in tmpwd)& ('公司'!= tmpwd)&('央行' not in tmpwd)&('本公司' not in tmpwd)& ('国家' not in tmpwd)& ('全国' not in tmpwd)& ('人大' not in tmpwd)& ('协会' not in tmpwd)& ('组织' not in tmpwd)& ('国际' not in tmpwd)& ('世界' not in tmpwd)& ('国务' not in tmpwd)& ('证监' not in tmpwd)& ('交所' not in tmpwd)& ('交易所' not in tmpwd)&(bool(re.search(r'[\d|\W|局|报|监|部|政|党]', tmpwd))==False)):
                    final_coll.append(tmpwd)
                ind1 += 1
        final_coll_list.append(dict(Counter(final_coll)))
        ind += 1

df1['compete'] = final_coll_list
relation_date = time.strftime("%Y-%m-%d-%H-%M-%S", time.localtime()) 
csv_path = f'competition_'+f'{relation_date}.csv'
json_path = f'competition_'+f'{relation_date}.json'
df1.to_csv(csv_path,index = False,header =1)
df1.to_json(json_path,orient="records",force_ascii=False) 


