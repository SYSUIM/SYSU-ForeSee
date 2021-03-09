# -*- coding: utf-8 -*-



import requests
import re
from fake_useragent import UserAgent
import json
import csv
import time
import pandas as pd

def get_header():
    ua = UserAgent(verify_ssl=False)
    return {'User-Agent': ua.random}

def get_proxy():
    proxypool_url = 'http://127.0.0.1:5555/random'
    proxies = {'http': 'http://' + requests.get(proxypool_url).text.strip()}
    return proxies

def get_compNews(stock_code,stock_name,short_name,stock_exchange):
    page = 1
    resData = pd.DataFrame(columns=('news_title','news_time','news_link'))
    news_title = []
    news_time = []
    news_link =[]
    requests.adapters.DEFAULT_RETRIES = 5
    while(True):
        baseUrl = 'https://vip.stock.finance.sina.com.cn/corp/view/vCB_AllNewsStock.php?symbol='+str(stock_exchange)+str(stock_code)+'&Page='+str(page)
        try:
            r = requests.get(baseUrl, headers=get_header())
            #cont -- content
            cont = r.text
            exist = re.findall(r'暂时没有数据！',cont)
            if exist == []:
                tmpDic = {}
                plink = r'<a target=\'_blank\' href=(.*?)>'
                link = re.findall(plink,cont)
                ptitle = r'<a target=\'_blank\' href=.*?>([\s\S]*?)</a>'
                title = re.findall(ptitle,cont)
                ptime = r'\&nbsp;\&nbsp;\&nbsp;\&nbsp;(\d{4}-\d{2}-\d{2})\&nbsp;'
                times = re.findall(ptime,cont)
                news_title.extend(title)
                news_time.extend(times)
                news_link.extend(link)
                page += 1
                # time.sleep(300)
            else:
                break
        except:
            print("Connection refused by the server..")
            time.sleep(5)
            continue

    listlen = len(news_time)

    #对信息进行处理
    tmpDic = {
             "news_title":news_title, 
             "news_time":news_time,
             "news_link":news_link
             }
    newsdf = pd.DataFrame(tmpDic)
    newsdf = newsdf.sort_values(by = "news_time",ascending = True)
    #将一家公司的所有新闻转成一个list
    news = [newsdf.to_dict(orient="records")]
    #一家公司的所有信息
    resDic_comp = {
            "stock_code":stock_code,
            "company_name":stock_name,
            "stock_name":short_name,
            "news":news, 
            }
    #一家公司所有信息转成一个df
    resDf_comp = pd.DataFrame(resDic_comp)

    return resDf_comp

def get_indryNews(industryCode,compDf):
    '''
    industryCode--行业号码
    compList --该行业号码对应的所有公司stockcode
    '''
    #resDf_indry用于存储一个行业的所有公司信息的df
    resDf_indry = pd.DataFrame()

    #一个行业的所有公司信息合成一个df
    for index, row in compDf.iterrows():
        stock_code = row['stock_code']
        stock_name = row['company_name']
        short_name = row['short_name']
        stockExchange = row['stock_exchange']
        resDf_comp = get_compNews(stock_code,stock_name,short_name,stockExchange)
        resDf_indry = pd.concat([resDf_indry,resDf_comp],axis=0)
    #返回一个行业的所有公司的所有信息
    print("完成一个行业的所有公司的所有信息")
    return resDf_indry

#core function
def get_allNews(filepath,savePath):
    '''
    industryList 包含所有行业号码的列表
    '''
    compDf_all,industryList = get_compIndex(filepath)
    flag= 0
    indlen = len(industryList)
    index = 0 #用于遍历industryList
    
    #所有行业的所有新闻
    while(index < indlen):
        industryCode = industryList[index]
        compDf = compDf_all[compDf_all['industry_code'] == industryCode].iloc[:,1:]
        resDf_indry = get_indryNews(industryCode,compDf)
        flag = index
        get_jsonFile(industryCode,resDf_indry,savePath,flag,indlen)
        index = index + 1
        # time.sleep(3)
        # print(index)
    print("完成所有信息")

def get_jsonFile(industryCode,resDf_indry,savePath,flag,indlen):
    #flag判断是否是第几个行业
    resInd_list = [resDf_indry]
    resNews_all = {"industry_code":industryCode,"all_newsinfo":resInd_list}
    resNews_all_df = pd.DataFrame(resNews_all)
    resNews_all_js = resNews_all_df.to_json(orient="records",force_ascii=False)
    fh = open(savePath, 'a')
    if(flag == 0):
        fh.write(resNews_all_js[:-1])
        fh.write(',')
    elif (flag == indlen-1):
        fh.write(resNews_all_js[1:])
        fh.close()
    else:
        fh.write(resNews_all_js[1:-1])
        fh.write(',')
        fh.close()
    print("完成\t"+str(industryCode)+"json写入")

def get_compIndex(filepath):
    compDf_all =  pd.read_csv(filepath)
    comp_ind = compDf_all.drop_duplicates('industry_code')
    industryList = list(comp_ind['industry_code'])
    return compDf_all,industryList




# if __name__ == '__main__':
#     get_allNews('companyIndex.csv','news.json')

