import os
import json
import os
import json
import pymongo
myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["ForeSee"]
mycol = mydb["companyInfo"]

def file_name(file_dir:str):
	dirs=os.listdir(file_dir)
	files=[]
	for adir in dirs:
		files+=[file_dir+adir+"/"+x for x in os.listdir(file_dir+adir+"/")]
	return files
# files=file_name(r"../../TestData")
files=["/data/prj2020/EnterpriseSpider/detail/ent_details.json"]

for afile in files:
	with open(afile,"r",encoding='utf8') as f:
		array=f.read()
	jsonArray = json.loads(array, encoding='utf-8')
	# for i in jsonArray:
	# 	i = json.dumps(i)
	# 	i = json.loads(i)
	# 	res=mycol.insert_one(i)
	res=mycol.insert_many(jsonArray)
	
	print(res)
