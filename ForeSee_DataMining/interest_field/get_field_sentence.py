# -*- coding: utf-8 -*-
"""
Created on Wed Feb 24 14:19:50 2021
抽取感兴趣的领域所在的句子
"""
import pandas as pd
from pandas import DataFrame
import re
import os
import csv

#获得公司列表
def get_compIndex(filepath):
    compDf_all =  pd.read_csv(filepath)
    companyList = list(set(list(compDf_all['stock_code'])))
    print("完成get_compIndex")
    return compDf_all,companyList

def get_compNews(stockCode,compDf):
    '''
    stockCode--公司股票号码
    compDf --该股票号码对应的所有公司研究报告信息
    '''
    
    stock_code = stockCode
    industry_code = list(compDf['industry_code'])[0]
    stock_name = list(compDf['stock_name'])[0]
    company_name = list(compDf['company_name'])[0]
    research_title = list(compDf['research_title'])
    research_time = list(compDf['research_time'])
    respath = f'/data/prj2020/EnterpriseSpider/news/research/'
    lenres = len(research_title)
    i = 0
    stock_code_all = []
    stock_name_all = []
    industry_code_all = []
    field_sent_all = []
    comp_name_all = []
    research_time_all = []
    label_all = []
    outpath1 = f'/data/prj2020/EnterpriseSpider/news/research_field_sent_0227v1.csv'

    while(i < lenres):
        path = respath + f'{industry_code}/'+f'{stock_code}/'+f'{research_title[i]}.txt'
        try:
            with open(path, "r",encoding='utf-8') as f:    #打开文件
                text = f.read()   #读取文件
            #定位到有顿号的句子
            senlist = re.split('[，,；;。！？\n\'\"]',text)
            find_sent=[sent for sent in senlist if '、' in sent]
            
            find_sent_len = len(find_sent)
            stock_name_all.extend([stock_name]*find_sent_len)
            industry_code_all.extend([industry_code]*find_sent_len)
            comp_name_all.extend([company_name]*find_sent_len)
            stock_code_all.extend([stock_code]*find_sent_len)
            research_time_all.extend([research_time[i]]*find_sent_len)
            label_all.extend([0]*find_sent_len)
            field_sent_all.extend(find_sent)
            i += 1        
        except Exception as e:
            print(str(stock_code)+"\t"+str(industry_code)+"\t"+"path="+str(path))
            # print(str(e))
            i += 1

        #"industry_code","stock_code","stock_name","company_name",research_time,"field_list"
    res_dict = {
         'industry_code':industry_code_all,
         'stock_code':stock_code_all,
         'stock_name':stock_name_all,
         'company_name':comp_name_all,
         'research_time':research_time_all,
         'field_sentence': field_sent_all,
         'label': label_all  
            }
    res_df = pd.DataFrame(res_dict)
    res_df.field_sentence.replace('\s+','',regex=True,inplace=True) 
    res_df.to_csv(outpath1,mode='a',index = False,header =0,encoding='utf-8_sig')

    print("完成"+str(stock_code)+"\t"+str(industry_code)+"\t"+"感兴趣领域句子抽取")
    # return industry_code,stock_code,stock_name,company_name,field_list

def get_allNews(filepath):
    '''
    companyList 包含所有股票号码的列表
    '''
    compDf_all,companyList = get_compIndex(filepath)
    indlen = len(companyList)
    index = 0 #用于遍历 companyList

    
    #所有公司的所有合作公司
    while(index < indlen):
        stockCode = companyList[index]
        compDf = compDf_all[compDf_all['stock_code'] == stockCode]
        get_compNews(stockCode,compDf)
        
        print("完成"+str(stockCode)+"\t"+"的研究领域抽取")
        index = index + 1

    print("完成所有信息")

if __name__ == '__main__':
    filepath = '/data/prj2020/EnterpriseSpider/news/stock_research0202v1_2015.csv'
    get_allNews(filepath)