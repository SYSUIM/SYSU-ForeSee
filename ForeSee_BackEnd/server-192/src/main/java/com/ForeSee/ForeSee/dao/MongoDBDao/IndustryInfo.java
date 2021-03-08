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
public class IndustryInfo {
    private static final String tableName="industryInfo";
    private static StringBuilder sb;
    private static MongoCollection<Document> collection;

    /**
     * 使用表industryInfo，返回参数列表中的行业的基本信息，用于一框式检索展示行业简要信息
     * @param industryCodes
     * @return [industryInfo, industryInfo]，详见project/server-192/src/main/resources/FrontEndData/Query/industryInfo.json
     */
    public static String getIndustryInfo(List<String> industryCodes, MongoClient client){
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        Iterator<String> it = industryCodes.iterator();

        while (it.hasNext()) {
            String code = it.next();
            Document originDoc = collection.find(eq("IndustryInfo.industry_code", code)).first();
            originDoc = (Document) originDoc.get("IndustryInfo");
            if (originDoc.toJson() != null) sb.append(originDoc.toJson()+",");
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
     * 使用表industryInfo，返回单个行业的基本信息，用于展示行业详情信息
     * @param industryCode
     * @return {}，详见/server-192/src/main/resources/FrontEndData/BasicInfo/industryInfo.json
     */
    public static String getIndustryInfo(String industryCode, MongoClient client){
        MongoCollection<Document> collection = client.getDatabase("ForeSee").getCollection(tableName);
        MongoCursor cursor = null;
        StringBuilder sb = new StringBuilder();
        try {
            cursor = collection.find(eq("IndustryInfo.industry_code", industryCode)).iterator();
            if (cursor.hasNext()) {
                Document tmp = (Document) cursor.next();
                tmp.remove("_id");
                sb.append(tmp.toJson());
            } else {
                cursor = collection.find(eq("IndustryInfo.industry", industryCode)).iterator();
                Document tmp = (Document) cursor.next();
                tmp.remove("_id");
                sb.append(tmp.toJson());
            }
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
