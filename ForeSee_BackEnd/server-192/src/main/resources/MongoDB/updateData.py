import json
import pymongo


def updateData(info):
    myclient = pymongo.MongoClient("mongodb://localhost:27017/")
    db = myclient["ForeSee"]
    col = db[info["collection"]]
    result = col.update_one(filter={info["findIndex"] : info["findValue"]}, update={"$set": {info["changeIndex"]: info["changeValue"]}})
    # print(result.matched_count)  # 1
    print(result.modified_count)  # 1
    




if __name__ == '__main__':

    
    info = {
        "collection": "companyInfo",
        "findIndex": "companyInfo.stock_code",
        "findValue": "300149",
        "changeIndex": "companyInfo.logo",
        "changeValue": "https://img.qichacha.com/Product/6b9dda6c-6cea-4347-9728-602a8f042a90.jpg"
    }
    
    updateData(info)

    # /data/ForeSee_BackEnd/server-192/src/main/resources/MongoDB