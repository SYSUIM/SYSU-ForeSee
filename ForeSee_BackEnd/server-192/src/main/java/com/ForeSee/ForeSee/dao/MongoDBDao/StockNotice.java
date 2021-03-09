package com.ForeSee.ForeSee.dao.MongoDBDao;

import java.util.Iterator;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;
import com.mongodb.client.FindIterable;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;


@Slf4j
@Component
public class StockNotice {
    private static final int pageSize=10;
    private static final String tableName="notice";
    private static MongoCursor<Document> cursor;
    private static StringBuilder sb;
    private static MongoCollection<Document> collection;
    private static MongoCollection<Document> collectionTmp;
    private static int totalRecords;

     /**
     * 查询Notice表，根据stockCodes返回公告信息，每页10，按时间排序，添加企业信息，用于一框式检索倒推逻辑
     * @param stockCodes
     * @return 见/server-192/src/main/resources/FrontEndData/Query/companyNotice.json
     */
    public static String getNoticeBasedStockCodes(List<String> stockCodes, MongoClient client, String page) {
        totalRecords = stockCodes.size();
        Iterator<String> it = stockCodes.listIterator((Integer.parseInt(page)-1)*pageSize);
        String head="{\"page\": "+page+",\"totalRecords\":"+totalRecords+",\"notice\": [";
        sb = new StringBuilder(head);
        try {
            collection = client.getDatabase("ForeSee").getCollection(tableName);
            collectionTmp = client.getDatabase("ForeSee").getCollection("companyInfo");
            while (it.hasNext() ) {
                
                String code = it.next();
                Document originDoc = collection.find(eq("stock_code", code))
                                    .sort(Sorts.descending("notice_time")).iterator().next();
                Document companyDoc = collectionTmp.find(eq("companyInfo.stock_code", code)).first();
                originDoc.remove("_id");
                originDoc.remove("stock_code");
                originDoc.put("companyInfo", companyDoc.get("companyInfo"));
                sb.append(originDoc.toJson());
                sb.append(",");
                totalRecords ++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        return sb.toString().replace("'", "");
    }

     /**
     * 查询Notice表，根据noticeIds返回公告信息，每页10，单页按时间排序，添加企业信息，用于一框式检索结果展示
     * @param noticeIds
     * @return 见/server-192/src/main/resources/FrontEndData/Query/companyNotice.json
     */
    public static String getNoticeBasedQuery(List<String> noticeIds, MongoClient client, String page) {
        totalRecords = noticeIds.size();
        try{
            noticeIds = noticeIds.subList((Integer.parseInt(page)-1)*pageSize, Integer.parseInt(page)*pageSize);
        }catch (Exception e){
            noticeIds = noticeIds.subList((Integer.parseInt(page)-1)*pageSize, totalRecords);
        }
        List<Integer> idList = new ArrayList<>();
        CollectionUtils.collect(noticeIds, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Integer.valueOf(o.toString());
            }
        }, idList);
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        collectionTmp = client.getDatabase("ForeSee").getCollection("companyInfo");
        cursor = collection.find(in("notice_id", idList))
                .sort(Sorts.descending("notice_time"))
                .iterator();
        String head="{\"page\": "+page+",\"totalRecords\":"+totalRecords+",\"notice\": [";
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
        return sb.toString().replace("'", "");
    }

     /**
     * 查询Notice表，根据stockCode返回公告信息，每页10，按时间排序，用于企业详情页展示
     * @param stockCode
     * @return 见/server-192/src/main/resources/FrontEndData/BasicInfo/companyNotice.json
     */
    public static String getAllNotice(String stockCode,MongoClient client,String page) {
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        cursor = collection.find(eq("stock_code", stockCode))
                .sort(Sorts.descending("notice_time"))
                .iterator();
                
        String head="{\"page\": "+page+",\"notice\": [";
        sb = new StringBuilder(head);
        int p = Integer.parseInt(page),count=0;
        try {
            while (cursor.hasNext()) {
                Document originDoc = cursor.next();
                if (count >= (p-1)*pageSize && count < p*pageSize){
                    originDoc.remove("_id");
                    originDoc.remove("stock_code");
                    sb.append(originDoc.toJson()+",");
                }
                count ++;
            }
        }  catch (Exception e){
            e.printStackTrace();
        }

        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"totalRecords\":"+count+"}");
        return sb.toString();
    }

}