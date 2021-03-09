package com.ForeSee.ForeSee.dao.MongoDBDao;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import java.util.Iterator;
// import org.springframework.data.mongodb.MongoDatabaseFactory;

@Slf4j
public class CompanyInfo {

    private static final String tableName="companyInfo";
    private static MongoCollection<Document> collection;
    private static StringBuilder sb;
    private static int totalRecords;
    private static int pageSize=6;

    /**
     * 使用表companyInfo，返回参数列表中的企业的基本信息，用于一框式检索展示企业简要信息
     * @param stockCodes
     * @return [companyInfo, companyInfo]，详见project/server-192/src/main/resources/FrontEndData/Query/companyInfo.json
     */
    public static String getCompanyInfo(List<String> stockCodes, MongoClient client, String page) {
        totalRecords = stockCodes.size();
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("{\"page\":"+page+",\"totalRecords\":"+totalRecords+",\"company\":[");
        try{
            stockCodes = stockCodes.subList((Integer.parseInt(page)-1)*pageSize, Integer.parseInt(page)*pageSize);
        }catch (Exception e){
            stockCodes = stockCodes.subList((Integer.parseInt(page)-1)*pageSize, totalRecords);
        }
        Iterator<String> it = stockCodes.iterator();
        while (it.hasNext()) {
            String code = it.next();
            // 有it.next()为空的情况，需要排查
            try {
                Document originDoc = (Document) collection.find(eq("companyInfo.stock_code", code)).first().get("companyInfo");
                if (originDoc.toJson() != null) sb.append(originDoc.toJson()+",");
            }catch (Exception e){
                continue;
            }
            
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}");
        return sb.toString();
    }

    
    /**
     * 使用表companyInfo，返回单个企业的基本信息，用于展示企业详情信息
     * @param stockCode
     * @return {}，详见/server-192/src/main/resources/FrontEndData/BasicInfo/companyInfo.json
     */
    public static String getCompanyInfo(String stockCode,MongoClient client) {
        // MongoDatabaseFactory 
        collection = client.getDatabase("ForeSee").getCollection(tableName);
        // collection = client.getMongoDatabase("ForeSee").getCollection(tableName);
        sb= new StringBuilder();
        Document originDoc = collection.find(in("companyInfo.stock_code", stockCode)).first();
        originDoc.remove("_id");
        sb.append(originDoc.toJson());
        return sb.toString();
    }

}
