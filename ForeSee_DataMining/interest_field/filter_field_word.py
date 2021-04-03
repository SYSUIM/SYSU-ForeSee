# -*- coding: utf-8 -*-
"""
Created on Sun Feb 28 10:48:13 2021
对抽出来的领域词进行再次过滤
"""


import pandas as pd
from LAC import LAC
import re
import os


path1 = '/data/prj2020/EnterpriseSpider/news/research_field_0227v3_header.csv'
df1 = pd.read_csv(path1)
outpath1 = f'/data/prj2020/EnterpriseSpider/news/research_field_0228v3.csv'
outpath2 = f'/data/prj2020/EnterpriseSpider/news/research_field_0228v3.json'
#industry_code,stock_code,stock_name,company_name,research_time,research_field
tmp = list(df1['research_field'])
oldlist = []
oldlist.extend([eval(x) for x in tmp])
newlist = []

lenold = len(oldlist)
lac = LAC(mode = 'lac')
x = 0 #用于遍历原本的dic
while(x < lenold):
    try:
        klist = list(oldlist[x].keys())
        vlist = list(oldlist[x].values())
        ind = 0
        klen = len(klist)
        knewlist = []
        vnewlist = []
        while(ind < klen):
            tmpk = klist[ind]
            if((len(tmpk)> 1)& (len(tmpk)<9)& ('公司' not in tmpk)& ('销售' not in tmpk) & (bool(re.search(r'\d', tmpk))==False)):
                knewlist.append(tmpk)
                vnewlist.append(vlist[ind])
            ind += 1
        #lennew = len(newlist)
        klist_all = []
        vlist_all = []
        print(1)
        # lenres = len(lac_result)
        i = 0
        knlen = len(knewlist)
        while(i < knlen):
            tmpwd = knewlist[i]
            try:
                lac_result = lac.run(tmpwd)
                print(tmpwd)
                wrd = lac_result[0]
                tag = lac_result[1]
                if(('v' not in tag) &('ORG' not in tag)&('LOC' not in tag)&('TIME' not in tag)&('s' not in tag)&('d' not in tag)&('m' not in tag)):
                    klist_all.append(tmpwd)
                    vlist_all.append(vnewlist[i])
                i += 1
            except Exception as e:
                klist_all.append(tmpwd)
                vlist_all.append(vnewlist[i])
                print(e)
                # print(klist[i])
                i += 1
        x += 1
        new_dict = dict(zip(klist_all,vlist_all))
        newlist.append(new_dict)
    except Exception as e:
        newlist.append(oldlist[x])
        x += 1
df1['research_field'] = newlist
df1.to_csv(outpath1,index = False,header =True,encoding='utf-8_sig')
df1.to_json(outpath2,orient="records",force_ascii=False)