# -*- coding: utf-8 -*-
"""
Created on Fri Mar  5 09:08:54 2021
根据research_field_0304v2.csv
求出竞争领域
"""



import csv
import pandas as pd


#先把一家公司所有年份研究领域合并
def get_field_list(orgpath,outpath):
    orgdf = pd.read_csv(orgpath,dtype=str)
    orgdf['stock_code'] = orgdf['stock_code'].astype(str).str.zfill(6)
    std_code_list = list(set(list(orgdf['stock_code'])))
    
    #把string 转dict
    tmp = list(orgdf['research_field'])
    newlist = []
    newlist.extend([eval(x) for x in tmp])
    field_list = []
    field_list.extend([list(x.keys()) for x in newlist])
    orgdf['research_field'] = field_list
    
    #industry_code,stock_code,stock_name,company_name,research_time,research_field
    industry_code_all = []
    stock_code_all = []
    stock_name_all = []
    company_name_all = []
    research_field_all = []
    std_code_len = len(std_code_list)
    i = 0 #用于遍历stock
    while(i < std_code_len):
        tmp_code = std_code_list[i]
        tmp_field = []
        tmp_df = orgdf[orgdf['stock_code']==tmp_code]
        industry_code_all.append(list(tmp_df['industry_code'])[0])
        stock_code_all.append(tmp_code)
        stock_name_all.append(list(tmp_df['stock_name'])[0])
        company_name_all.append(list(tmp_df['company_name'])[0])
        tmp_list = list(tmp_df['research_field'])
        tmp_list_len = len(tmp_list)
        x = 0
        while(x < tmp_list_len):
            tmp_field.extend(tmp_list[x])
            x += 1
        tmp_field = list(set(tmp_field))
        research_field_all.append(tmp_field)
        i += 1
    res_dict = {
             'industry_code':industry_code_all,
             'stock_code':stock_code_all,
             'stock_name':stock_name_all,
             'company_name':company_name_all,
             'research_field': research_field_all
                }
    res_df = pd.DataFrame(res_dict)
    
    res_df.to_csv(outpath,index = False,header =True,encoding='utf-8_sig')
    return std_code_list,res_df




#下面把那个公司所在行业的其他全部公司的研究领域求交集
def get_compete_field(std_code_list,res_df,csvpath,jsonpath):
    stock_all_list1 = []
    stock_all_list2 = []
    stock_name1_all = []
    stock_name2_all = []
    weight_all = []
    inter_field_all = []
    industry_code_all = []
    all_df = res_df
    std_list = std_code_list
    #retB = list(set(listA).intersection(set(listB)))
    name_list = list(all_df['stock_name'])
    ind_list = list(all_df['industry_code'])
    field_list = list(all_df['research_field'])
    #industry_code,stock_code,stock_name,company_name,research_time,research_field
    std_code_len = len(std_list)
    i = 0 #用于遍历stock
    while(i < std_code_len):
        std_code = std_list[i]
        std_name = name_list[i]
        ind_code = ind_list[i]
        cur_field = field_list[i]
        inter_field = []
        tmp_df = all_df[(all_df['industry_code']==ind_code)&(all_df['stock_code']!=std_code)]
        tmp_df_field = list(tmp_df['research_field'])
        tmp_df_std_code = list(tmp_df['stock_code'])
        tmp_df_std_name = list(tmp_df['stock_name'])
        tmp_df_field_len = len(tmp_df_field)
        x = 0
        while(x < tmp_df_field_len):
            compte_field = tmp_df_field[x]
            inter_field = list(set(cur_field).intersection(set(compte_field)))
            if(len(inter_field) > 0):
                stock_all_list1.append(std_code)
                stock_all_list2.append(tmp_df_std_code[x])
                stock_name1_all.append(std_name)
                stock_name2_all.append(tmp_df_std_name[x])
                inter_field_all.append(inter_field)
                industry_code_all.append(ind_code)
                weight_all.append(len(inter_field))
            x += 1
        i += 1
    
    res2_dict = {
                 'industry_code':industry_code_all,
                 'stock_code':stock_all_list1,
                 'stock_name':stock_name1_all,
                 'compete_code':stock_all_list2,
                 'compete_name':stock_name2_all,
                 'weight':weight_all,
                 'compete_field': inter_field_all
                    }
    res_df2 = pd.DataFrame(res2_dict)
    
    res_df2.to_csv(csvpath,index = False,header =True,encoding='utf-8_sig')
    res_df2.to_json(jsonpath,orient="records",force_ascii=False)

orgpath = 'research_field_0304v2.csv'
outpath = 'research_field_list0305.csv'
std_code_list,res_df = get_field_list(orgpath,outpath)
outpath2 = 'compete_field0305.csv'
outpath3 = 'compete_field0305.json'
get_compete_field(std_code_list,res_df,outpath2,outpath3)











