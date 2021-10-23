import json
import pymongo
import glob

# flushcol: 是否清空col，默认为True
def loadData(info, flushcol=True):
    myclient = pymongo.MongoClient("mongodb://localhost:27017/")
    db = myclient["ForeSee"]
    col = db[info["collection"]]
    if flushcol:
        col.delete_many({})
        print("successfully delete!")
        
    if info["type"] == "file":

        if col == "news":
            with open(info["path"], "r", encoding='utf8') as f:
                jsonArray = json.loads(
                                f.read().replace('news_title','title').replace('news_time','date').replace('news_link','url').replace("'",''),
                                encoding='utf-8'
                            )
        else:
            with open(info["path"], "r", encoding='utf8') as f:
                jsonArray = json.loads(f.read().replace("aid", "id"), encoding='utf-8')
        
        res = col.insert_many(jsonArray)
        print(res)
    
    else:
        files = glob.glob(info["path"])
        for p in files:
            with open(p, "r", encoding='utf8') as f:
                jsonArray = json.loads(f.read().replace("aid", "id"), encoding='utf-8')
            res = col.insert_many(jsonArray)
            print(res)

    print("finished!")




if __name__ == '__main__':

    companyInfo = {
        "collection": "companyInfo",
        "path": "/data/prj2020/EnterpriseSpider/detail/ent_details.json",
        "type": "file"
    }
    industryInfo = {
        "collection": "industryInfo",
        "path": "/data/prj2020/EnterpriseSpider/detail/ind_details.json",
        "type": "file"
    }
    news = {
        "collection": "news",
        "path": "/data/prj2020/EnterpriseSpider/index/id/news_id.json",
        "type": "file"
    }
    notice = {
        "collection": "notice",
        "path": "/data/prj2020/EnterpriseSpider/Index/id/notice_id.json",
        "type": "file"
    }
    report = {
        "collection": "report",
        # "path": "/data/prj2020/EnterpriseSpider/analysis/reMapArticle0314v0.json",
        "path": "/data/prj2020/EnterpriseSpider/analysis/Internet_article_infoall0311v0.json",
        "type": "file"
    }
    research = {
        "collection": "research",
        "path": "/data/prj2020/EnterpriseSpider/news/research_json/research_info.json",
        "type": "file"
    }

    industryVector = {
        "collection": "industryVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/industry_vectors.json",
        "type": "file"
    }

    companyVector = {
        "collection": "companyVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/stock_vectors.json",
        "type": "file"
    }

    newsVector = {
        "collection": "newsVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/news/news*",
        "type": "dir"
    }

    noticeVector = {
        "collection": "noticeVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/notice_vectors.json",
        "type": "file"
    }

    reportVector = {
        "collection": "reportVector",
        "path": "/data/prj2020/EnterpriseSpider/searchEngine/prepared/vectors/report_vectors.json",
        "type": "file"
    }
    test = {
        "collection": "report",
        "path": "/data/prj2020/EnterpriseSpider/analysis/allData.json",
        "type": "file"
    }
    # /data/ForeSee_BackEnd/server-192/src/main/resources/MongoDB
    info = report
    loadData(info, flushcol=False)
