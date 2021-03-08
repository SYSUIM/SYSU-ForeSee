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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

@Slf4j
public class StockNews {
    
    private static final String tableName="news";
    private static StringBuilder sb;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> collectionTmp;
    private static MongoCursor<Document> cursor;
    private static final int pageSize=10;
    private static int totalRecords;


    /**
     * 查询News表，根据stockCodes返回每个stockCode最新的一条新闻信息，每页10，按时间排序，添加企业信息，用于一框式检索结果展示
     * @param stockCodes
     * @return 见/server-192/src/main/resources/FrontEndData/Query/companyNews.json
     */
    public static String getNewsBasedStockCodes(List<String> stockCodes, MongoClient client, String page) {
        
        totalRecords = stockCodes.size();
        try{
            stockCodes = stockCodes.subList((Integer.parseInt(page)-1)*pageSize, Integer.parseInt(page)*pageSize);
        }catch (Exception e){
            stockCodes = stockCodes.subList((Integer.parseInt(page)-1)*pageSize, totalRecords);
        }
        Iterator<String> it = stockCodes.iterator();
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        collectionTmp = client.getDatabase("ForeSee").getCollection("companyInfo");
        String head="{\"page\": "+page+",\"news\": [", stockCode;
        sb = new StringBuilder(head);
        try {
            while (it.hasNext()) {
                Document originDoc = collection.find(in("stock_code", it.next())).sort(Sorts.descending("date")).first();
                Document companyDoc = collectionTmp.find(eq("companyInfo.stock_code", originDoc.get("stock_code"))).first();
                originDoc.remove("_id");
                originDoc.remove("stock_code");
                originDoc.put("companyInfo", companyDoc.get("companyInfo"));
                sb.append(originDoc.toJson());
                sb.append(",");
            }
        } catch (Exception e){
            log.info("Something Wrong in getNewsBasedQuery news_id");
        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]"+",\"totalRecords\":"+totalRecords+"}");
        log.info("has already queried companyNews from MongoDB based newsIds");
        return sb.toString().replace("'", "");
    }

    /**
     * 查询News表，根据newsIds返回stockCodes，用于一框式检索倒推逻辑
     * @param newsIds
     * @return  List<String> stockCodes
     */
    public static List<String> getStockCodes(List<String> newsIds, MongoClient client) {
        List<String> stockCodes = new ArrayList<>();
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        List<Integer> idList = new ArrayList<>();
        CollectionUtils.collect(newsIds, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Integer.valueOf(o.toString());
            }
        }, idList);
        cursor = collection.find(in("news_id", idList))
                    .limit(pageSize).iterator();
        while (cursor.hasNext()) {
            Document originDoc = cursor.next();
            stockCodes.add((String)originDoc.get("stock_code"));
        }
        
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(stockCodes));
        return result;
    }

    /**
     * 查询News表，根据newsIds返回新闻信息，每页10，按时间排序，添加企业信息，用于一框式检索结果展示
     * @param newsIds
     * @return 见/server-192/src/main/resources/FrontEndData/Query/companyNews.json
     */
    public static String getNewsBasedQuery(List<String> newsIds, MongoClient client, String page) {
        totalRecords = newsIds.size();
        try{
            newsIds = newsIds.subList((Integer.parseInt(page)-1)*pageSize, Integer.parseInt(page)*pageSize);
        }catch (Exception e){
            newsIds = newsIds.subList((Integer.parseInt(page)-1)*pageSize, totalRecords);
        }
        List<Integer> idList = new ArrayList<>();
        CollectionUtils.collect(newsIds, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Integer.valueOf(o.toString());
            }
        }, idList);
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        collectionTmp = client.getDatabase("ForeSee").getCollection("companyInfo");
        cursor = collection.find(in("news_id", idList))
                .sort(Sorts.descending("date"))
                .iterator();
        String head="{\"page\": "+page+",\"totalRecords\":"+totalRecords+",\"news\": [";
        sb = new StringBuilder(head);
        try {
            while (cursor.hasNext()) {
                Document originDoc = cursor.next();
                Document companyDoc = collectionTmp.find(eq("companyInfo.stock_code", originDoc.get("stock_code"))).first();
                originDoc.remove("_id");
                originDoc.remove("stock_code");
                originDoc.put("companyInfo", companyDoc.get("companyInfo"));
                sb.append(originDoc.toJson());
                sb.append(",");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        log.info("has already queried companyNews from MongoDB based newsIds");
        return sb.toString().replace("'", "");
    }


     /**
     * 查询News表，根据stockCode返回新闻信息，每页10，按时间排序，用于企业详情页展示
     * @param stockCode
     * @return 见/server-192/src/main/resources/FrontEndData/BasicInfo/companyNews.json
     */
    public static String getAllNews(String stockCode, MongoClient client, String page) {
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        MongoCursor<Document> cursor = collection.find(eq("stock_code", stockCode))
                .sort(Sorts.descending("date"))
                .iterator();
        String head="{\"page\": "+page+",\"news\": [";
        sb = new StringBuilder(head);
        int p = Integer.parseInt(page);
        totalRecords=0;
        int bPage = (p-1)*pageSize, ePage = p*pageSize;
        try {
            while (cursor.hasNext()) {
                Document originDoc = cursor.next();
                if (totalRecords >= bPage && totalRecords < ePage){
                    originDoc.remove("_id");
                    originDoc.remove("stock_code");
                    sb.append(originDoc.toJson()+",");
                }
                totalRecords ++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"totalRecords\":"+totalRecords+"}");
       
        return sb.toString().replace("'", "");
    }

}
