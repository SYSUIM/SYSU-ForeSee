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
import java.util.LinkedHashSet;

import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

@Slf4j
public class StockResearch {
    
    private static final String tableName="research";
    private static StringBuilder sb;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> collectionTmp;
    private static MongoCursor<Document> cursor;
    private static final int pageSize=10;
    private static int totalRecords;

    

    /**
     * 查询Research表，根据stockCode返回研究报告信息，每页10，按时间排序，用于企业详情页展示
     * @param stockCode
     * @return 见/server-192/src/main/resources/FrontEndData/BasicInfo/companyResearch.json
     */
    public static String getAllResearch(String stockCode, MongoClient client, String page) {
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        MongoCursor<Document> cursor = collection.find(eq("stock_code", stockCode))
                .sort(Sorts.descending("research_time"))
                .iterator();
        String head="{\"page\": "+page+",\"research\": [";
        sb = new StringBuilder(head);
        int p = Integer.parseInt(page), count=0;
        while (cursor.hasNext()) {
            
            Document originDoc = cursor.next();
            if (count >= (p-1)*pageSize && count < p*pageSize){
                originDoc.remove("_id");
                originDoc.remove("stock_code");
                originDoc.remove("industry_code");
                sb.append(originDoc.toJson()+",");
            }
            count ++;
        }
        
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"totalRecords\":"+count+"}");
       
        return sb.toString();
    }

       

}
