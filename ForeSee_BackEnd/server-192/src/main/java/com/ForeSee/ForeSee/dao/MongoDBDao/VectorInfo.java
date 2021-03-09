package com.ForeSee.ForeSee.dao.MongoDBDao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import lombok.extern.slf4j.Slf4j;
import java.util.Iterator;

@Slf4j
public class VectorInfo {
    private static String tableName;
    private static StringBuilder sb;
    private static MongoCollection<Document> collection;

    /**
     * 使用表industryVector
     * @param 
     * @return 
     */
    public static String getIndustryVector(List<String> industryCodes, MongoClient client){
        tableName = "industryVector";
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        Iterator<String> it = industryCodes.iterator();

        while (it.hasNext()) {
            String code = it.next();
            Document originDoc = collection.find(eq("id", code)).first();
            if (originDoc.toJson() != null) sb.append((String) originDoc.get("vector")+",");
        }
        if (sb.length() > 1) {
            // 存疑，多线程访问时爆数组越界
            try {
                sb.deleteCharAt(sb.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 使用表companyVector
     * @param 
     * @return 
     */
    public static String getCompanyVector(List<String> stockCodes, MongoClient client){
        tableName = "companyVector";
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        Iterator<String> it = stockCodes.iterator();

        while (it.hasNext()) {
            String code = it.next();
            Document originDoc = collection.find(eq("id", code)).first();
            if (originDoc.toJson() != null) sb.append((String) originDoc.get("vector").toString()+",");
        }
        if (sb.length() > 1) {
            // 存疑，多线程访问时爆数组越界
            try {
                sb.deleteCharAt(sb.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 使用表newsVector
     * @param 
     * @return 
     */
    public static String getNewsVector(List<String> newsIds, MongoClient client){
        tableName = "newsVector";
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        Iterator<String> it = newsIds.iterator();

        while (it.hasNext()) {
            String code = it.next();
            Document originDoc = collection.find(eq("id", code)).first();
            if (originDoc.toJson() != null) sb.append((String) originDoc.get("vector")+",");
        }
        if (sb.length() > 1) {
            // 存疑，多线程访问时爆数组越界
            try {
                sb.deleteCharAt(sb.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     * 使用表noticeVector
     * @param 
     * @return 
     */
    public static String getNoticeVector(List<String> noticeIds, MongoClient client){
        tableName = "noticeVector";
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        Iterator<String> it = noticeIds.iterator();

        while (it.hasNext()) {
            String code = it.next();
            Document originDoc = collection.find(eq("id", code)).first();
            if (originDoc.toJson() != null) sb.append((String) originDoc.get("vector")+",");
        }
        if (sb.length() > 1) {
            // 存疑，多线程访问时爆数组越界
            try {
                sb.deleteCharAt(sb.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 使用表reportVector
     * @param 
     * @return 
     */
    public static String getReportVector(List<String> reportIds, MongoClient client){
        tableName = "reportVector";
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        Iterator<String> it = reportIds.iterator();

        while (it.hasNext()) {
            String code = it.next();
            Document originDoc = collection.find(eq("id", code)).first();
            if (originDoc.toJson() != null) sb.append((String) originDoc.get("vector")+",");
        }
        if (sb.length() > 1) {
            // 存疑，多线程访问时爆数组越界
            try {
                sb.deleteCharAt(sb.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }


}
