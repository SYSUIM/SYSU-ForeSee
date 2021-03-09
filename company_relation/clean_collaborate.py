# -*- coding: utf-8 -*-
"""
Created on Sun Feb 28 10:48:13 2021
将模型得到的合作关系数据和规则抽取得到的合作关系数据清洗 整合
"""


import pandas as pd
import re
import ast
from collections import Counter
import time   

#industry_code,stock_code,stock_name,collaborate
path1 = '/data/prj2020/EnterpriseSpider/news/relation/coll_0228v301.csv'
df1 = pd.read_csv(path1)
df1['stock_code'] = df1['stock_code'].astype(str).str.zfill(6)
path2 = '/data/prj2020/EnterpriseSpider/news/relation/collaboration_2021-02-28-19-51-27.csv'
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
        tmp_list1 = [list(df1[(df1['stock_code'] == curcode)]['collaborate'])[0]]
        tmp_list2 = [list(df2[(df2['stock_code'] == curcode)]['collaborate'])[0]]
        coll_list1 = []
        coll_list2 = []
        coll_list1.extend([ast.literal_eval(x) for x in tmp_list1])
        coll_list2.extend([ast.literal_eval(x) for x in tmp_list2])
        if(len(coll_list1)>0):
            new_coll_list1 = coll_list1[0]
            lencoll1 = len(new_coll_list1)
            # 用于对coll1进行处理
            ind1 = 0 
            while(ind1 < lencoll1):
                tmpwd = new_coll_list1[ind1]
                tmpwd = tmpwd.strip().replace(u'\u3000', u'').replace(u'\xa0', u'')
                if((len(tmpwd)> 1)& (len(tmpwd)<9)& ('公司'!= tmpwd)& (curname not in tmpwd)&(bool(re.search(r'[\d|\W|全国|国家|人大|交易所|交所|国务院|协会|组织|局|本公司|报|监|国家|央行|人民|部|世界|国际|政府]', tmpwd))==False)):
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
                if((len(tmpwd)> 1)& (len(tmpwd)<9)& ('公司'!= tmpwd)& (curname not in tmpwd)&(bool(re.search(r'[\d|\W|全国|国家|人大|交易所|交所|国务院|协会|组织|局|本公司|报|监|国家|央行|人民|部|世界|国际|政府]', tmpwd))==False)):
                    final_coll.append(tmpwd)
                ind2 += 1
        final_coll_list.append(dict(Counter(final_coll)))
        ind += 1
    except Exception as e:
        #因为df2中可能没有对应的stock_code 所以要进行处理
        print(str(curcode)+'\t'+'df2中可能没有对应的stock_code')
        tmp_list1 = [list(df1[(df1['stock_code'] == curcode)]['collaborate'])[0]]
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
                if((len(tmpwd)> 1)& (len(tmpwd)<9)& ('公司'!= tmpwd)& (curname not in tmpwd)&(bool(re.search(r'[\d|\W|全国|国家|人大|交易所|交所|国务院|协会|组织|局|本公司|报|监|国家|央行|人民|部|世界|国际|政府]', tmpwd))==False)):
                    final_coll.append(tmpwd)
                ind1 += 1
        final_coll_list.append(dict(Counter(final_coll)))
        ind += 1
df1['collaborate'] = final_coll_list
relation_date = time.strftime("%Y-%m-%d-%H-%M-%S", time.localtime()) 
csv_path = f'/data/prj2020/EnterpriseSpider/news/relation/collaboration_'+f'{relation_date}.csv'
json_path = f'/data/prj2020/EnterpriseSpider/news/relation/collaboration_'+f'{relation_date}.json'
df1.to_csv(csv_path,index = False,header =1)
df1.to_json(json_path,orient="records",force_ascii=False) 


        
