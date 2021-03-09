# -*- coding: utf-8 -*-
# Written by panzy

import sys
import json
import pandas as pd

ent_details = []
fileNotFoundList = []

dir_path = '/data/prj2020/EnterpriseSpider/'
ent_ind_path = dir_path + 'basic/stock_index.csv'

# 获取数据库所有企业行业对应表
ent_ind_df = pd.read_csv(ent_ind_path)

# for i in range(2):
for i in range(len(ent_ind_df)):
    # 定义单家公司的字典
    ent_detail = {}
    # 公司基本信息
    ent_companyInfo_detail = {}
    # 公司新闻
    # ent_news_detail = []
    # 公告信息
    # ent_notice_detail = []
    # 公司表格信息
    ent_tableData_detail = []
    # 公司表格展开信息
    ent_tableData2_detail = []
    # 时间序列数据
    ent_profit_detail = {}
    # 公司联系方式
    ent_contact_detail = {}
    # 公司地理信息
    ent_geo_detail = [{}]
    # 行业饼图数据
    ent_pieIndustry_detail = [{}]
    # 公司合作关系
    ent_cooperate_detail = {}
    # 公司竞争关系
    ent_compete_detail = {}
    # 公司雷达图
    ent_radar_detail = {}
    # 公司词云
    # 不需要处理，直接读取文件
    # 公司领域
    ent_interest_detail = []

    industry_code = ent_ind_df.loc[i, 'industry_code']
    stock_code = str(ent_ind_df.loc[i, 'stock_code']).zfill(6)

    print('PROCESS: ' + stock_code)
    # /data/prj2020/EnterpriseSpider/basic/BasicInfoFile/BK0420/600115.json
    ent_basic_info_path = dir_path + 'basic/BasicInfoFile/' + industry_code + '/' + stock_code + '.json'
    # /data/prj2020/EnterpriseSpider/geoInfo/geoData/BK0420.json
    ent_geo_info_path = dir_path + '/geoInfo/geoData/' + industry_code + '.json'
    # /data/prj2020/EnterpriseSpider/profit/chart/BK0420/000089.json
    ent_profit_info_path = dir_path + 'profit/chart/' + industry_code + '/' + stock_code + '.json'
    # /data/prj2020/EnterpriseSpider/profit/stock_distribution_info_index.json
    ent_pieIndustry_info_path = '/data/prj2020/EnterpriseSpider/profit/stock_distribution_info_index.json'
    # /data/prj2020/EnterpriseSpider/profit/stock_radar_info_index.json
    ent_radar_info_path = '/data/prj2020/EnterpriseSpider/profit/stock_radar_info_index.json'
    # /data/prj2020/EnterpriseSpider/news/relation/collaboration_2021-02-03-23-54-05.json
    ent_cooperate_info_path = '/data/prj2020/EnterpriseSpider/news/relation/collaboration_2021-03-02-09-31-27.json'
    # /data/prj2020/EnterpriseSpider/news/relation/competition_2021-02-04-11-32-59.json
    ent_compete_info_path = '/data/prj2020/EnterpriseSpider/news/relation/competition_2021-03-02-15-18-59.json'
    # /data/prj2020/EnterpriseSpider/wordCloud/data/BK0420/000099.json
    ent_wordCloud_path = '/data/prj2020/EnterpriseSpider/wordCloud/data/' + industry_code + '/' + stock_code + '.json'
    # /data/prj2020/EnterpriseSpider/news/research_field/research_field_0304v2.json
    ent_interest_path = '/data/prj2020/EnterpriseSpider/news/research_field/research_field_0304v2.json'

    try:
        with open(ent_wordCloud_path, 'r') as ent_wordCloud_file:
            ent_wordCloud_json = json.load(ent_wordCloud_file)
            ent_detail['wordCloud'] = ent_wordCloud_json
    except FileNotFoundError as e:
        fileNotFoundList.append(ent_wordCloud_path + ' not FOUND!')

    with open(ent_basic_info_path, 'r') as ent_basic_info_file:
        ent_basic_info_json = json.load(ent_basic_info_file)
        if ent_basic_info_json['stock_code'] != stock_code:
            print('文件内stock_code与文件名stock_code不匹配!')
            sys.exit(1)

        ent_companyInfo_detail['former_name'] = ent_basic_info_json['entName']
        ent_companyInfo_detail['describe'] = ent_basic_info_json['describe']
        ent_companyInfo_detail['logo'] = ent_basic_info_json['entLogo']
        ent_companyInfo_detail['stock_code'] = stock_code

        ent_tableData_detail.append({
            "name": "公司名称",
            "value": ent_basic_info_json['entName']
        })
        ent_tableData_detail.append({
            "name": "股票代码",
            "value": stock_code
        })
        ent_tableData_detail.append({
            "name": "成立日期",
            "value": ent_basic_info_json['startDate']
        })
        ent_tableData_detail.append({
            "name": "登记机关",
            "value": ent_basic_info_json['authority']
        })
        ent_tableData_detail.append({
            "name": "法定代表人",
            "value": ent_basic_info_json['legalPerson']
        })
        ent_tableData_detail.append({
            "name": "注册资本",
            "value": ent_basic_info_json['regCapital']
        })
        ent_tableData_detail.append({
            "name": "工商注册号",
            "value": ent_basic_info_json['licenseNumber']
        })
        # 展开部分
        ent_tableData2_detail.append({
            "name": "企业类型",
            "value": ent_basic_info_json['entType']
        })
        ent_tableData2_detail.append({
            "name": "组织机构代码",
            "value": ent_basic_info_json['orgNo']
        })
        ent_tableData2_detail.append({
            "name": "纳税人识别号",
            "value": ent_basic_info_json['regNo']
        })
        ent_tableData2_detail.append({
            "name": "营业期限",
            "value": ent_basic_info_json['openTime']
        })
        ent_tableData2_detail.append({
            "name": "经营范围",
            "value": ent_basic_info_json['scope']
        })

        ent_contact_detail  = {
            "address": ent_basic_info_json['regAddr'],
            "office": ent_basic_info_json['office'],
            "fax": ent_basic_info_json['fax'],
            "email": ent_basic_info_json['email']
        }

        ent_detail['companyInfo'] = ent_companyInfo_detail
        ent_detail['tableData'] = ent_tableData_detail
        ent_detail['contact'] = ent_contact_detail
        ent_detail['tableData2'] = ent_tableData2_detail

    with open(ent_geo_info_path, 'r') as ent_geo_info_file:
        ent_geo_info_json = json.load(ent_geo_info_file)
        for ent in ent_geo_info_json:
            if ent['stock_code'] == stock_code:
                ent_geo_detail[0]['lng'] = ent['lng']
                ent_geo_detail[0]['lat'] = ent['lat']
                ent_geo_detail[0]['company_name'] = ent['company_name']
                ent_geo_detail[0]['stock_code'] = ent['stock_code']
                break

        ent_detail['geo'] = ent_geo_detail

    with open(ent_profit_info_path, 'r') as ent_profit_info_file:
        ent_profit_info_json = json.load(ent_profit_info_file)
        ent_profit_detail['time'] = ent_profit_info_json['time']
        ent_profit_detail['income'] = ent_profit_info_json['income']
        ent_profit_detail['expense'] = ent_profit_info_json['expense']
        ent_profit_detail['profit'] = ent_profit_info_json['profit']
        ent_profit_detail['total_profit'] = ent_profit_info_json['total_profit']
        ent_profit_detail['net_profit'] = ent_profit_info_json['net_profit']

        ent_detail['profit'] = ent_profit_detail

    with open(ent_pieIndustry_info_path, 'r') as ent_pieIndustry_info_file:
        ent_pieIndustry_info_json = json.load(ent_pieIndustry_info_file)
        ent_pieIndustry_detail = ent_pieIndustry_info_json[stock_code]['pieIndustry']

        ent_detail['pieIndustry'] = ent_pieIndustry_detail

    with open(ent_radar_info_path, 'r') as ent_radar_info_file:
        ent_radar_info_json = json.load(ent_radar_info_file)
        ent_radar_detail['company'] = ent_radar_info_json[stock_code]['company']
        ent_radar_detail['avg'] = ent_radar_info_json[stock_code]['avg']

        ent_detail['radar'] = ent_radar_detail

    with open(ent_cooperate_info_path, 'r') as ent_cooperate_info_file:
        ent_cooperate_info_json = json.load(ent_cooperate_info_file)
        for temp in ent_cooperate_info_json:
            if temp['stock_code'] == stock_code:
                ent_cooperate_detail['name'] = temp['stock_name']
                ent_cooperate_detail['collaborate'] = []
                for key, value in temp['collaborate'].items():
                    collaborate = {
                        'name': key,
                        'symbolSize': value
                    }
                    ent_cooperate_detail['collaborate'].append(collaborate)
                break

        ent_detail['cooperate'] = ent_cooperate_detail

    with open(ent_compete_info_path, 'r') as ent_compete_info_file:
        ent_compete_info_json = json.load(ent_compete_info_file)
        for temp in ent_compete_info_json:
            if temp['stock_code'] == stock_code:
                ent_compete_detail['name'] = temp['stock_name']
                ent_compete_detail['compete'] = []
                for key, value in temp['compete'].items():
                    compete = {
                        'name': key,
                        'symbolSize': value
                    }
                    ent_compete_detail['compete'].append(compete)
                break

        ent_detail['compete'] = ent_compete_detail

    with open(ent_interest_path, 'r') as ent_interest_info_file:
        ent_interest_info_json = json.load(ent_interest_info_file)
        for temp in ent_interest_info_json:
            if str(temp['stock_code']) == stock_code:
                ent_interest_annual_detail = {}
                #print(ent_interest_annual_detail['series'])
                ent_interest_annual_detail = {
                    'year': temp['research_time'],
                    'industry_code': temp['industry_code'],
                    'stock_code': temp['stock_code'],
                    'company_name': temp['company_name']
                }
                ent_interest_annual_detail['series'] = []

                for key, value in temp['research_field'].items():
                    interest = {
                        'name': key,
                        'symbolSize': value
                    }
                    ent_interest_annual_detail['series'].append(interest)

                ent_interest_detail.append(ent_interest_annual_detail)
            else: continue

        ent_detail['interest'] = ent_interest_detail

    ent_details.append(ent_detail)

# print(ent_details)
print(fileNotFoundList)

json_str = json.dumps(ent_details)
with open('./ent_details.json', 'w') as json_file:
    json_file.write(json_str)
