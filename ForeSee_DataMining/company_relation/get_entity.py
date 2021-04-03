from LAC import LAC
import pandas as pd
from pandas import DataFrame
import re
import csv 
import os
from multiprocessing import Pool
import  traceback
'''
从对应的新闻中读取抽取包括该股票的句子
每个在原先[sentence,relation,head,head_offset,tail,tail_offset]最后加上一个stock_code,stock_name
[path,stock_name,stock_code,industry_code]
'''

global lac
lac = LAC(mode = 'lac')
#加载定制词表
lac.load_customization('/data/prj2020/EnterpriseSpider/news/customEnty.txt', sep='\n')

def getSent(path,stock_name,stock_code,industry_code):
    replace=re.compile(r'\s+');
    with open(path, "r",encoding='utf-8') as f:    #打开文件
        text = f.read()   #读取文件
        text=re.sub(replace,'',text)
    senlist = re.split('。|？|!',text)
    senlist=[sent for sent in senlist if str(stock_name) in sent]
    sen_len = len(senlist)
    for sen in senlist:
        if len(sen)>100:
            sen = sen[:100]
    sen_dic = {
        'senlist':senlist,
        'stock_name':[stock_name]*sen_len,
        'stock_code':[stock_code]*sen_len,
        'industry_code':[industry_code]*sen_len
            }
    sen_df = pd.DataFrame(sen_dic)
    return sen_df

#sentence,relation,head,head_offset,tail,tail_offset
def getEnty(sent,stock_name,stock_code,industry_code,ind):
    sentence = []
    head = []
    tail = []
    head_offset = []
    tail_offset = []

    cnt = 0
    lac_result = lac.run(sent)


    word_list = lac_result[0]

    tags_list = lac_result[1]

    if tags_list.count('ORG') <2:
        return None
    sentence.append(sent)
    enty_ind1 = tags_list.index('ORG')
    enty1 = word_list[enty_ind1]
    head.append(enty1)
    
    head_offset.append(sent.find(enty1))
    enty_ind2 = tags_list.index('ORG',enty_ind1+1)
    enty2 = word_list[enty_ind2]
    
    #如果实体1和实体2一样，则判断实体3
    if enty2 == enty1:
        if tags_list.count('ORG') <3:
            return None
        else:
            enty_ind3 = tags_list.index('ORG',enty_ind2+1)
            enty3 = word_list[enty_ind3]
            tail.append(enty3)
            tail_offset.append(sent.find(enty3))
    else:
        tail.append(enty2)
        tail_offset.append(sent.find(enty2))
    '''
    ["sentence","head","head_offset","tail","tail_offset","stock_name","stock_code","industry_code"]
    '''
    content = [sentence[cnt],head[cnt],head_offset[cnt],tail[cnt],tail_offset[cnt],stock_name,stock_code,industry_code]
    cnt += 1
    outpath1 = f'/data/prj2020/EnterpriseSpider/news/data_enty/enty0203v1_'+f'{ind}.csv'
    with open(outpath1,'a+',encoding='utf-8') as csvf:
        writer = csv.writer(csvf)
        writer.writerow(content)
    res_dic = {
        "sentence":sentence,
        "head":head,
        "head_offset":head_offset,
        "tail":tail,
        "tail_offset":tail_offset,
        "stock_name":stock_name,
        "stock_code":stock_code,
        "industry_code":industry_code
        }
    res_df = pd.DataFrame(res_dic)
    print(res_df.iloc[0,:])
    outpath2 = f'/data/prj2020/EnterpriseSpider/news/data_enty/enty0203v1_'+f'{ind}_2.csv'
    res_df.to_csv(outpath2,mode='a', header=False,index = False)
    return res_df

def getAll(df,beg,end,ind):
    df = df.iloc[beg:end,:]
    df.index = range(len(df))
    # print("getAll----dflen-----"+str(dflen))
    file_list_all = list(df.iloc[:,0])
    # print(len(file_list))
    stock_name_all =list(df.iloc[:,1])
    # print(len(stock_name))
    stock_code_all =list(df.iloc[:,2])
    # print(len(stock_code))
    industry_code_all =list(df.iloc[:,3])
    # print(len(industry_code))    
    fcnt = 0
    flen = len(file_list_all)
    while(fcnt < flen):
        try:
            path = f'{file_list_all[fcnt]}'
            
            # sen_df表示一篇新闻 senlist表示一篇新闻里面分出的句子list
            sen_df = getSent(path,stock_name_all[fcnt],stock_code_all[fcnt],industry_code_all[fcnt])
            # res_df = getEnty(senlist,lac_result)
            senlist = list(sen_df['senlist'])
            stock_name = list(sen_df['stock_name'])
            stock_code = list(sen_df['stock_code'])
            industry_code = list(sen_df['industry_code'])
            senlen = len(senlist)
            scnt = 0
            while(scnt < senlen):
                getEnty(senlist[scnt],stock_name[scnt],stock_code[scnt],industry_code[scnt],ind)
                scnt += 1
            fcnt += 1
        except Exception as e:
            print("path======="+path)
            print(str(e))
            print(fcnt)
            traceback.print_exc()
            fcnt += 1
            continue

if __name__ == '__main__':
    file_path = 'allFileList0125v3.csv'
    df = pd.read_csv(file_path,dtype=str)
    p = Pool(20)
    dflen = df.shape[0]
    dfMargin = int(dflen/20)
    df.index = range(dflen)
    index = 0
    i = 0
    while(i < 20):
        if i == 0:
            p.apply_async(getAll,args=(df,index,dfMargin+index,i,))
            # print(i)
        elif i == 19:
            p.apply_async(getAll,args=(df,i*dfMargin+index,dflen-1,i,))
            # print(i)
        else:
            p.apply_async(getAll,args=(df,i*dfMargin+index,(i+1)*dfMargin+index,i,))
            # print(i)
        i += 1
    p.close()
    p.join()
    print('finish all process')  


