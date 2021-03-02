import json
import pymongo
myclient=pymongo.MongoClient("mongodb://localhost:27017/")
db=myclient["ForeSee"]
col=db["industryInfo"]
s=""
print("read s")
with open(r"/data/prj2020/EnterpriseSpider/detail/ind_details.json","r",encoding="utf8") as f:
	s=f.read()
print("s has been read")

jsonArray=json.loads(s,encoding='utf-8')
# print(jsonArray[1])
res=col.insert_many(jsonArray)
print(res)
# print("s has been split")
# for json in jsonArray:
# 	industry_code=json["industry_code"]
# 	print("-------------now industry_code=",str(industry_code),"-----------------")
# 	stockArray=json["all_newsinfo"]
# 	for stock in stockArray:
# 		newsArray=stock["news"]
# 		if not newsArray:
# 			newsArray=[{}]
# 		info={"industry_code":industry_code,"stock_code":stock["stock_code"]}
# 		for new in newsArray:
# 			new.update(info)
# 		res=col.insert_many(newsArray)
# 		print(res)
# 		with open(r"/home/user02/mysql/mongodb/NewsLog.txt","a",encoding='utf8') as Log:
# 			Log.write("stock_code="+str(industry_code)+"res="+str(res)+"\n")
			
		
