import json
import pymongo

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
db = myclient["ForeSee"]
col = db["research"]

with open(r"/data/prj2020/EnterpriseSpider/news/research_json/research_info.json","r",encoding = "utf8") as f:
	s = f.read()

print("s has been read")
jsonArray = json.loads(s,encoding = 'utf-8')
res = col.insert_many(jsonArray)
print(res)

