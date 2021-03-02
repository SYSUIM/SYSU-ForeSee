package com.ForeSee.ForeSee.dao.MongoDBDao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class IndustryNews {
    
    private static final String tableName="news";
    private static StringBuilder sb;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> collectionTmp;
    private static final int pageSize=3;
    private static int totalRecords=0;
   

    /**
     * 查询News表，拿出每个stockcode最新的一条新闻，用于行业详情页的新闻展示，每页pageSize条
     * 需要添加必要的企业信息，用于新闻头部展示
     * @param stockCodes
     * @return 见/server-192/src/main/resources/FrontEndData/BasicInfo/industryNews.json
     */
    public static String getAllNews(List<String> stockCodes, MongoClient client, String page) {
        totalRecords = stockCodes.size();
        Iterator<String> it = stockCodes.listIterator((Integer.parseInt(page)-1)*pageSize);
        String head="{\"page\": "+page+",\"totalRecords\":"+totalRecords+",\"news\": [";
        sb = new StringBuilder(head);
        int count = 0;
    
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        collectionTmp = client.getDatabase("ForeSee").getCollection("companyInfo");
        while (it.hasNext() && count<pageSize) {
            count ++;
            String code = it.next();
            Document originDoc = collection.find(eq("stock_code", code))
                                .sort(Sorts.descending("date")).iterator().next();
            Document companyDoc = collectionTmp.find(eq("companyInfo.stock_code", code)).first();
            originDoc.remove("_id");
            originDoc.remove("stock_code");
            originDoc.put("companyInfo", companyDoc.get("companyInfo"));
            sb.append(originDoc.toJson());
            sb.append(",");
        }
        
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        log.info("has already queried industryNews from MongoDB");
        return sb.toString();
    }

   

}
