// 返回行业资讯信息,每页10条
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
import java.util.List;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
/*
{
  "page": 1,
  "totalRecords": 1073,
  "information": [
      {},{}
   ]
}
*/

@Slf4j
public class IndustryReport {
    private static final String tableName="report";
    private static final int pageSize = 10;
    private static MongoCollection<Document> collection;
    private static StringBuilder sb;
    private static MongoCursor<Document> cursor;
    private static MongoCollection<Document> collectionTmp;
    private static int totalRecords;

    /**
     * 查询Report表，根据一系列industryCodes返回资讯信息，每页10，每个行业返回最新的一条report，用于一框式检索的倒推逻辑
     * @param industryCodes
     * @return 见/server-192/src/main/resources/FrontEndData/Query/industryReport.json
     */
    public static String getReportBasedIndustryCodes(List<String> industryCodes, MongoClient client, String page) {
        totalRecords = industryCodes.size();
        Iterator<String> it = industryCodes.listIterator((Integer.parseInt(page)-1)*pageSize);
        String head="{\"page\": "+page+",\"totalRecords\":"+totalRecords+",\"information\": [";
        sb = new StringBuilder(head);
        int count = 0;
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        collectionTmp = client.getDatabase("ForeSee").getCollection("industryInfo");
        while (it.hasNext() && count<pageSize) {
            count ++;
            String code = it.next();
            Document originDoc = collection.find(eq("industry", code))
                                .sort(Sorts.descending("pub_date")).iterator().next();
            Document industryDoc = collectionTmp.find(eq("IndustryInfo.industry_code", originDoc.get("industry"))).first();
            originDoc.remove("_id");
            originDoc.remove("industry");
            originDoc.put("IndustryInfo", industryDoc.get("IndustryInfo"));
            sb.append(originDoc.toJson());
            sb.append(",");
        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        log.info("has already queried industryReport from MongoDB based industryCodes");
        return sb.toString();
    }

    /**
     * 查询Report表，根据一系列reportIds返回资讯信息，每页10，单页内的资讯按时间排序，用于一框式检索
     * @param reportIds
     * @return 见/server-192/src/main/resources/FrontEndData/Query/industryReport.json
     */
    public static String getReportBasedQuery(List<String> reportIds, MongoClient client, String page) {
        totalRecords = reportIds.size();
        try{
            reportIds = reportIds.subList((Integer.parseInt(page)-1)*pageSize, Integer.parseInt(page)*pageSize);
        }catch (Exception e){
            reportIds = reportIds.subList((Integer.parseInt(page)-1)*pageSize, totalRecords);
        }
        
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        collectionTmp = client.getDatabase("ForeSee").getCollection("industryInfo");
        cursor = collection.find(in("id", reportIds))
                .sort(Sorts.descending("date"))
                .iterator();
        String head="{\"page\": "+page+",\"totalRecords\":"+totalRecords+",\"information\": [";
        sb = new StringBuilder(head);

        List<String> ids = new ArrayList<String>();
        String id = "";  // 解决因为一条资讯对应多家行业的重复问题
        while (cursor.hasNext()) {
            Document originDoc = cursor.next();
            id = ""+originDoc.get("id");
            if (ids.contains(id)) {
                continue;
            } else {
                ids.add(id);
                Document industryDoc = collectionTmp.find(eq("IndustryInfo.industry_code", originDoc.get("industry"))).first();
                originDoc.remove("_id");
                originDoc.remove("industry");
                originDoc.put("IndustryInfo", industryDoc.get("IndustryInfo"));
                sb.append(originDoc.toJson());
                sb.append(",");
            }

        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * 查询Report表，根据industryCode返回资讯信息，每页10，按时间排序，用于行业详情页展示
     * @param industryCode
     * @return 见/server-192/src/main/resources/FrontEndData/BasicInfo/industryReport.json
     */
    public static String getIndustryReport(String industryCode, MongoClient client, String page){
        totalRecords = 0;
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        cursor = collection.find(eq("industry", industryCode))
                .sort(Sorts.descending("pub_date"))
                .iterator();

        String head="{\"page\": "+page+",\"information\": [";
        sb = new StringBuilder(head);
        int p = Integer.parseInt(page);
        int bPage = (p-1)*pageSize, ePage = p*pageSize;
        while (cursor.hasNext()) {
            
            Document originDoc = cursor.next();
            if (totalRecords >= bPage && totalRecords < ePage){
                originDoc.remove("_id");
                originDoc.remove("industry");
                sb.append(originDoc.toJson()+",");
            }
            totalRecords ++;
        }
        if (sb.length() > head.length()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"totalRecords\":"+totalRecords+"}");

        return sb.toString();
    }
}


