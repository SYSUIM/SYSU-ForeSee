# -*- coding: utf-8 -*-
"""
Created on Wed Feb 24 14:19:50 2021
根据文本分类得到感兴趣的领域所在的句子，并进行领域词抽取
"""
import pandas as pd
from pandas import DataFrame
import re
import os
import csv
from collections import Counter
import numpy as np

#获得公司列表
def get_compIndex(filepath):
    compDf_all =  pd.read_csv(filepath)
    compDf_all = compDf_all[(compDf_all['label'] == 1)]
    compDf_all['research_time'] = compDf_all['research_time'].str[:4]
    compDf_all = compDf_all[compDf_all['research_time']<'2021']
    compDf_all['stock_code'] = compDf_all['stock_code'].astype(str).str.zfill(6)
    companyList = list(set(list(compDf_all['stock_code'])))
    print("完成get_compIndex")
    return compDf_all,companyList

def get_compNews(stockCode,researchTime,compDf,outpath1):
    '''
    stockCode--公司股票号码
    compDf --该股票号码对应的所有公司研究报告信息
    '''
    #resDf_indry用于存储一个行业的所有公司信息的df
    stock_code = stockCode
    research_time = researchTime
    industry_code = list(compDf['industry_code'])[0]
    stock_name = list(compDf['stock_name'])[0]
    company_name = list(compDf['company_name'])[0]
    field_sentence = list(compDf['field_sentence'])

    len_sen = len(field_sentence)

    i = 0
    field_tmp = []

    # outpath1 = f'/data/prj2020/EnterpriseSpider/news/research_field_0226v1.csv'

    while(i < len_sen):
        
        try:
            text = field_sentence[i]
            #找出感兴趣的领域
            
            tmp_list = []
            if '等' in text:
                tmp_list = re.split('[、，,；。\'\"《》\[\]（）:：在和向如到及与以为的从等]', text)[1:-1]
            else:
                tmp_list = re.split('[、，,；。\'\"《》\[\]（）:：在和向如到及与以为的从]', text)[1:]

            field_tmp.extend(tmp_list)
            
            i += 1        
        except Exception as e:
            print(str(stock_code)+"\t"+str(industry_code)+"\t"+"sentence="+str(text))

            i += 1
    field_all = dict(Counter(list(field_tmp)))
    #"industry_code","stock_code","stock_name","company_name",research_time,"research_field"
    res_dict = {
         'industry_code':[industry_code],
         'stock_code':[stock_code],
         'stock_name':[stock_name],
         'company_name':[company_name],
         'research_time':[research_time],
         'research_field': [field_all]
            }
    res_df = pd.DataFrame(res_dict)
    
    res_df.to_csv(outpath1,mode='a',index = False,header =0,encoding='utf-8_sig')

    print("完成"+str(stock_code)+"\t"+str(industry_code)+"\t"+"感兴趣领域抽取")
    # return industry_code,stock_code,stock_name,company_name,field_list

def get_allNews(filepath,outpath1,outpath2):
    '''
    companyList 包含所有股票号码的列表
    '''
    compDf_all,companyList = get_compIndex(filepath)
    indlen = len(companyList)
    index1 = 0 #用于遍历 companyList
    
    
    #所有公司的所有合作公司
    while(index1 < indlen):
        stockCode = companyList[index1]
        # compDf = compDf_all[(compDf_all['stock_code'] == stockCode) & (compDf_all['label'] == 1)]
        compDf = compDf_all[(compDf_all['stock_code'] == stockCode)]
        index2 = 0 #用于遍历 timeList
        #每个公司研究报告的时间
        timeList = list(set(list(compDf['research_time'])))
        tlen = len(timeList)
        while(index2 < tlen):
            tmpTime = timeList[index2]
            timeDf = compDf[compDf['research_time'] == tmpTime]
            get_compNews(stockCode,tmpTime,timeDf,outpath1)
            index2 += 1
        print("完成"+str(stockCode)+"\t"+"的研究领域抽取")
        index1 = index1 + 1
    resDf_comp = pd.read_csv(outpath1)
    resDf_comp.columns = ["industry_code","stock_code","stock_name","company_name","research_time","research_field"]
    resDf_comp.to_json(outpath2,orient="records",force_ascii=False)
    print("完成所有信息")

if __name__ == '__main__':
    #industry_code,stock_code,stock_name,company_name,research_time,field_sentence,label
    sent_path = '/data/prj2020/EnterpriseSpider/news/research_field_sent_0227v1.csv'
    #field_sentence,label
    pred_path = '/data/prj2020/EnterpriseSpider/news/pred0227v1.csv'
    df1 = pd.read_csv(sent_path)
    df2 = pd.read_csv(pred_path)
    df1['label'] = df2['label']
    res_path = '/data/prj2020/EnterpriseSpider/news/predict_research_field_sent_0227v1.csv'
    df1.to_csv(res_path,index=False,header=True)
    outpath1 = f'/data/prj2020/EnterpriseSpider/news/research_field_0227v3.csv'
    outpath2 = f'/data/prj2020/EnterpriseSpider/news/research_field_0227v3.json'
    get_allNews(res_path,outpath1,outpath2)
    