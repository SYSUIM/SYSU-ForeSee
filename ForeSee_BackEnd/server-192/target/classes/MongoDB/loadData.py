import json
import pymongo

# flushcol: 是否清空col，默认为True
def loadData(info, flushcol=True):
    myclient = pymongo.MongoClient("mongodb://localhost:27017/")
    db = myclient["ForeSee"]
    col = db[info["collection"]]
    if flushcol:
        col.delete_many({})
        print("successfully delete!")

    if col == "news":
        with open(info["path"], "r", encoding='utf8') as f:
	        jsonArray = json.loads(
                            f.read().replace('news_title','title').replace('news_time','date').replace('news_link','url').replace("'",''),
                            encoding='utf-8'
                        )
    else:
        with open(info["path"], "r", encoding='utf8') as f:
	        jsonArray = json.loads(f.read(), encoding='utf-8')
    
    res = col.insert_many(jsonArray)
    print(res)
    print("finished!")




if __name__ == '__main__':

    companyInfo = {
        "collection": "companyInfo",
        "path": "/data/prj2020/EnterpriseSpider/detail/ent_details.json",
    }
    industryInfo = {
        "collection": "industryInfo",
        "path": "/data/prj2020/EnterpriseSpider/detail/ind_details.json",
    }
    news = {
        "collection": "news",
        "path": "/data/prj2020/EnterpriseSpider/index/id/news_id.json",
    }
    notice = {
        "collection": "notice",
        "path": "/data/prj2020/EnterpriseSpider/Index/id/notice_id.json",
        "type": ""
    }
    report = {
        "collection": "report",
        "path": "/data/prj2020/EnterpriseSpider/analysis/Internet_article_infoall.json",
    }
    research = {
        "collection": "research",
        "path": "/data/prj2020/EnterpriseSpider/news/research_json/research_info.json",
    }

    industryVector = {
        "collection": "industryVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/industry_vectors.json",
    }

    companyVector = {
        "collection": "companyVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/stock_vectors.json",
    }

    newsVector = {
        "collection": "newsVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/news_vectors.json",
    }

    noticeVector = {
        "collection": "noticeVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/notice_vectors.json",
    }

    reportVector = {
        "collection": "reportVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/report_vectors.json",
    }

    # /data/ForeSee_BackEnd/server-192/src/main/resources/MongoDB
    info = companyInfo
    loadData(info, flushcol=True)