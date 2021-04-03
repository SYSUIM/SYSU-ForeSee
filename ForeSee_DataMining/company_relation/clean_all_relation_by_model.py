# -*- coding: utf-8 -*-
"""
Created on Wed Jan 27 16:31:55 2021
实现封装，自动清洗出两种关系的数据
"""

import pandas as pd
import csv
from collections import Counter
import time   
import numpy as np

def getCollaboration(df_col,company_name,industry_code,stock_code,stock_name,relation_date):
    df_col_head = list(df_col['head'].drop_duplicates())
    #抽出所有合作者
    lencol = len(df_col_head)
    company_collaborate = []
    industry_code_all = []
    stock_code_all = []
    company_name_all = []
    stock_name_all = []
    x = 0 #用于遍历
    while(x < lencol):
        head = df_col_head[x]
        index = company_name[company_name.str.find(head) != -1].index
        indlen = len(index)
        if indlen == 0:
            index = company_name[stock_name.str.find(head) != -1].index
            indlen = len(index)
        # tails = dict(Counter(list(df_col[(df_col['head'] == head)]['tail'])))
        # company_collaborate.append(tails)
        x += 1
        if indlen > 0:
            ind = index[0]
            stock_code_all.append(stock_code[ind])
            industry_code_all.append(industry_code[ind])
            company_name_all.append(company_name[ind])
            stock_name_all.append(stock_name[ind])
            # tails = dict(Counter(list(df_col[(df_col['head'] == head) & (df_col['tail'] != head) & (~ df_col['tail'].str.contains(stock_name[ind])) & (~ df_col['tail'].str.contains(company_name[ind]))]['tail'])))
            tails = list(df_col[(df_col['head'] == head) & (df_col['tail'] != head) & (df_col['tail']!=(stock_name[ind])) & (df_col['tail']!=(company_name[ind]))]['tail'])
            company_collaborate.append(tails)
        else:
            stock_code_all.append(np.nan)
            industry_code_all.append(np.nan)
            company_name_all.append(head)
            stock_name_all.append(np.nan)
            company_collaborate.append([])
    
    collaboration = {
             'industry_code':industry_code_all,
             'stock_code':stock_code_all,
             'company_name': company_name_all,
             'stock_name':stock_name_all,
             'collaborate':company_collaborate
            }
    collaboration_df = pd.DataFrame(collaboration)
    collaboration_df = collaboration_df.dropna(axis=0,how='any')
    #下面尝试将相同stock code的合并
    collaboration_df = collaboration_df.sort_values(by=['stock_code'],ascending=True)
    collaboration_df = collaboration_df.drop_duplicates(subset=['stock_code'], keep='first', inplace=False)
    # col = collaboration_df.groupby(['stock_code'],as_index=True)["collaborate"].apply(list)
    col = collaboration_df.groupby(['stock_code'],as_index=True)["collaborate"].apply(lambda x:np.concatenate(list(x)))
    col = [x.tolist() for x in col]
    collaboration_df['collaborate'] = col
    csv_path = f'/data/prj2020/EnterpriseSpider/news/relation/collaboration_'+f'{relation_date}.csv'
    json_path = f'/data/prj2020/EnterpriseSpider/news/relation/collaboration_'+f'{relation_date}.json'
    collaboration_df.to_csv(csv_path,index = False,header =1)
    collaboration_df.to_json(json_path,orient="records",force_ascii=False) 

def getCompetition(df_comp,company_name,industry_code,stock_code,stock_name,relation_date):
    df_comp_head = list(df_comp['head'].drop_duplicates())
    lencomp = len(df_comp_head)
    industry_code_all = []
    stock_code_all = []
    company_name_all = []
    stock_name_all = []
    company_compete = []
    x = 0
    while(x < lencomp):
        head = df_comp_head[x]
        index = company_name[company_name.str.find(head) != -1].index
        indlen = len(index)
        if indlen == 0:
            index = company_name[stock_name.str.find(head) != -1].index
            indlen = len(index)        
        # tails = dict(Counter(list(df_comp[(df_comp['head'] == head)]['tail'])))
        # company_compete.append(tails)
        x += 1
        if indlen > 0:
            ind = index[0]
            stock_code_all.append(stock_code[ind])
            industry_code_all.append(industry_code[ind])
            company_name_all.append(company_name[ind])
            stock_name_all.append(stock_name[ind])
            # tails = dict(Counter(list(df_comp[(df_comp['head'] == head) & (df_comp['tail'] != head) & (df_comp['tail']!=(stock_name[ind])) & (df_comp['tail']!=(company_name[ind]))]['tail'])))
            tails = list(df_comp[(df_comp['head'] == head) & (df_comp['tail'] != head) & (df_comp['tail']!=(stock_name[ind])) & (df_comp['tail']!=(company_name[ind]))]['tail'])
            company_compete.append(tails)
        else:
            stock_code_all.append(np.nan)
            industry_code_all.append(np.nan)
            company_name_all.append(head)
            stock_name_all.append(np.nan)
            company_compete.append([])
    competition = {
             'industry_code':industry_code_all,
             'stock_code':stock_code_all,
             'company_name': company_name_all,
             'stock_name':stock_name_all,
             'compete':company_compete
            }
    
    competition_df = pd.DataFrame(competition)
    competition_df = competition_df.dropna(axis=0,how='any')
    #下面尝试将相同stock code的合并
    competition_df = competition_df.sort_values(by=['stock_code'],ascending=True)
    competition_df = competition_df.drop_duplicates(subset=['stock_code'], keep='first', inplace=False)
    # col = competition_df.groupby(['stock_code'],as_index=True)["compete"].apply(list)
    col = competition_df.groupby(['stock_code'],as_index=True)["compete"].apply(lambda x:np.concatenate(list(x)))
    col = [x.tolist() for x in col]
    # competition_df['compete'] = list(col)
    competition_df['compete'] = col
    csv_path = f'/data/prj2020/EnterpriseSpider/news/relation/competition_'+f'{relation_date}.csv'
    json_path = f'/data/prj2020/EnterpriseSpider/news/relation/competition_'+f'{relation_date}.json'
    competition_df.to_csv(csv_path,index = False,header =1)
    competition_df.to_json(json_path,orient="records",force_ascii=False) 

def getRelation(data_path):
    df_all = pd.read_csv(data_path,dtype=str)
    df_all = df_all[(df_all['relation'] != 'None')]
    df_all = df_all.drop(labels=['head_offset','tail_offset'], axis=1)
    #df_col = df_all[(df_all['relation'] == '合作')].drop_duplicates(subset=['head','tail'], keep='first', inplace=False)
    df_all = df_all[df_all.apply(lambda x: x['tail'] not in x['head'], axis=1)]
    df_all = df_all[df_all.apply(lambda x: x['head'] not in x['tail'], axis=1)]
    df_col = df_all[(df_all['relation'] == '合作')]
    df_comp = df_all[(df_all['relation'] == '竞争')]
    #匹配head的行业代码和企业代码
    code_all = pd.read_csv('/data/prj2020/EnterpriseSpider/basic/stock_index.csv',dtype=str).drop('stock_exchange',axis=1)
    company_name = code_all['company_name']
    industry_code =  code_all['industry_code']
    stock_code =  code_all['stock_code']
    stock_name = code_all['short_name']
    relation_date = time.strftime("%Y-%m-%d-%H-%M-%S", time.localtime()) 
    getCollaboration(df_col,company_name,industry_code,stock_code,stock_name,relation_date)
    getCompetition(df_comp,company_name,industry_code,stock_code,stock_name,relation_date)   

if __name__ == '__main__':
    # data_path = '/data/prj2020/EnterpriseSpider/news/analysis/deepke/res_sample0227_v1.csv'
    data_path = '/data/prj2020/EnterpriseSpider/news/analysis/deepke/demo.csv'
    getRelation(data_path)
