# -*- coding: utf-8 -*-
"""
Created on Thu Feb 18 16:37:33 2021
根据句子中是否含有合作等表示合作关系的词语抽取合作者
"""
from LAC import LAC
import pandas as pd
from pandas import DataFrame
import re
import os
import csv 
# from collections import Counter
import numpy as np

#获得公司列表
def get_compIndex(filepath):
    compDf_all =  pd.read_csv(filepath,dtype=str)
    companyList = list(set(list(compDf_all['stock_code'])))
    print("完成get_compIndex")
    return compDf_all,companyList

def get_allNews(filepath,lac):
    '''
    companyList 包含所有股票号码的列表
    '''
    compDf_all,companyList = get_compIndex(filepath)
    indlen = len(companyList)
    index = 0 #用于遍历 companyList
    
    stock_name_all = []
    industry_code_all = []
    colla_list_all = []
    outpath1 = f'/data/prj2020/EnterpriseSpider/news/relation/coll_0228v3.csv'
    with open(outpath1,"a",encoding='utf-8') as csvfile: 
        writer = csv.writer(csvfile)
        #先写入columns_name
        writer.writerow(["industry_code","stock_code","stock_name","collaborate"])
    
    #所有公司的所有合作公司
    while(index < indlen):
        stockCode = companyList[index]
        compDf = compDf_all[compDf_all['stock_code'] == stockCode]

        stock_code1,stock_name1,industry_code1,colla_list1 = get_compNews(stockCode,compDf,lac)
        stock_name_all.append(stock_name1)
        industry_code_all.append(industry_code1)
        colla_list_all.append(colla_list1)
        content = [industry_code1,stock_code1,stock_name1,colla_list1]
        # print("开始准备写入第一个公司的关系")
        with open(outpath1,'a+',encoding='utf-8') as csvf:
            writer = csv.writer(csvf)
            writer.writerow(content)
        print("完成"+str(stockCode)+"\t"+"的关系抽取")
        index = index + 1

    res_dict = {
         'industry_code':industry_code_all,
         'stock_code':companyList,
         'stock_name':stock_name_all,
         'collaborate':colla_list_all             
            }
    res_df = pd.DataFrame(res_dict)
    res_df.to_csv('/data/prj2020/EnterpriseSpider/news/relation/coll_0228v301.csv',index = False,header =1)
    res_df.to_json('/data/prj2020/EnterpriseSpider/news/relation/coll_0228v301.json',orient="records",force_ascii=False)       
    print("完成所有信息")

def get_compNews(stockCode,compDf,lac):
    '''
    stockCode--公司股票号码
    compDf --该股票号码对应的所有公司新闻信息
    '''
    #resDf_indry用于存储一个行业的所有公司信息的df
    stock_code = stockCode
    industry_code = list(compDf['industry_code'])[0]
    stock_name = list(compDf['stock_name'])[0]
    newspath = list(compDf['path'])
    collaborate = []
    lennews = len(newspath)
    i = 0
    while(i < lennews):
        path = newspath[i]
        try:
            with open(path, "r",encoding='utf-8') as f:    #打开文件
                text = f.read()   #读取文件
            pattern = re.compile('[，。;；][^，。；\n]*[支持|携手|牵手|战略合作|签订|投资|签署|购买|合作|供应链|收购|子公司|母公司|客户|顾客][^，。；\n]*[，。；]')
            find = re.findall(pattern, text)
            finlen = len(find)
            ind = 0
            while(ind < finlen):
                lac_result = lac.run(find[ind])
                word_list = lac_result[0]
                tag_list = lac_result[1]
                cnt = tag_list.count('ORG')
                if cnt == 0:
                    ind += 1
                    continue
                if cnt == 1:
                    tmp = word_list[tag_list.index('ORG')]
                    if((tmp != stock_name)& (bool(re.search(r'\d', tmp))==False)& ('央行' not in wd)& ('国家' not in wd)& ('证监' not in wd)& ('交所' not in wd)):
                        collaborate.append(tmp)
                    ind += 1
                    continue
                if cnt >= 2:
                    taglen = len(tag_list)
                    x = 0
                    while(x < taglen):
                        wd = word_list[x]
                        if ((tag_list[x] == 'ORG') & (bool(re.search(r'\d', wd))==False)& (wd != stock_name)& ('央行' not in wd)& ('国家' not in wd)& ('证监' not in wd)& ('交所' not in wd)):
                            collaborate.extend([wd])
                        x += 1
                ind += 1
                # print("完成"+str(stock_code)+"\t"+str(industry_code)+"\t"+str(path)+"合作公司抽取")
            i += 1
        except Exception as e:
            print(str(stock_code)+"\t"+str(industry_code)+"\t"+"path="+str(path))
            print(str(e))
            i += 1

    #对数据进行清洗
    collaborate = [x for x in collaborate if x != stock_name]
    # colla_list = [dict(Counter(list(collaborate)))]
    colla_list = collaborate
    print("完成"+str(stock_code)+"\t"+str(industry_code)+"\t"+"合作公司抽取")
    return stock_code,stock_name,industry_code,colla_list

if __name__ == '__main__':
    global lac
    lac = LAC(mode = 'lac')

    filepath = '/data/prj2020/EnterpriseSpider/news/allFileList0125v3.csv'
    get_allNews(filepath,lac)




