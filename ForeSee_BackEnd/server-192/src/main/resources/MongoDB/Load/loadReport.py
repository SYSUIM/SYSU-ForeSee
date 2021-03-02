import json
import pymongo
from functools import reduce

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
db = myclient["ForeSee"]
col = db["industryNews"]
col = db["report"]

# /data/ForeSee_BackEnd/server-192/src/main/resources/MongoDBDesign/load
# 补充的数据 /data/prj2020/EnterpriseSpider/analysis/reMapArticle.json
# 原本的数据 /data/prj2020/EnterpriseSpider/analysis/Internet_article_infoall.json
with open(r"/data/prj2020/EnterpriseSpider/analysis/reMapArticle.json", "r", encoding = "utf8") as f:
	s = f.read()

print("s has been read")
data_list = json.loads(s, encoding = 'utf-8')

with open(r"/data/prj2020/EnterpriseSpider/analysis/Internet_article_infoall.json", "r", encoding = "utf8") as f:
	s = f.read()

print("s has been read")
data_list = data_list + json.loads(s, encoding = 'utf-8')

run_function = lambda x, y: x if y in x else x + [y]
jsonArray = reduce(run_function, [[], ] + data_list)
print('data load')
res = col.insert_many(jsonArray)
print(res)


