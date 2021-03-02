import os
import pymongo
import json

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
db = myclient["ForeSee"]
col = db["notice"]


with open("/data/prj2020/EnterpriseSpider/Index/id/notice_id.json", 'r', encoding='utf-8') as f:
	data = f.read()
jsonArray = json.loads(data,encoding='utf-8')
print("data has loaded")
log = col.insert_many(jsonArray)
print(log)