import json
import pymongo

# /data/ForeSee_BackEnd/server-192/src/main/resources/MongoDB/
myclient = pymongo.MongoClient("mongodb://localhost:27017/")
db = myclient["ForeSee"]
col = db["industryInfo"]
# result = col.find_one({"stock_code":"300656"})
result = col.find_one()

print(result)
# f = open('../../../../../logs/tmp.json', 'w')
# result.pop('_id')
# result = json.dumps(result)
# f.write(result)
# print(col.find_one())

# # l = ['600029', '600897', '600004', '601111', '600591', '601021', '600009', '900945', '000089', '600115', '002928', '600221', '000099', '603885']

# 导出标题为空的notice Info
# d = {}
# for s in col.find({'notice_title':''}):
#     d[s.get('stock_code')] = 0

# print(len(list(d.keys())))

# with open('stock_code.txt','w') as f:
#     f.writelines(str(list(d.keys())))