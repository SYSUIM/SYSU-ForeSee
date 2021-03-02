package com.ForeSee.ForeSee.dao.MongoDBDao;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import com.alibaba.fastjson.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mongodb.client.model.Filters.in;

@Slf4j
public class CompanyInfo {

    private static final String tableName="companyInfo";
    private static MongoCollection<Document> collection;
    private static StringBuilder sb;

    /**
     * 使用表companyInfo，返回参数列表中的企业的基本信息，用于一框式检索展示企业简要信息
     * @param stockCodes
     * @return [companyInfo, companyInfo]，详见project/server-192/src/main/resources/FrontEndData/Query/companyInfo.json
     */
    public static String getCompanyInfo(List<String> stockCodes, MongoClient client) {

        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb = new StringBuilder("[");
        MongoCursor<Document> cursor = collection.find(in("companyInfo.stock_code", stockCodes)).iterator();
        while (cursor.hasNext()) {
            Document originDoc = (Document) cursor.next().get("companyInfo");
            sb.append(originDoc.toJson());
            sb.append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        log.info("has already queried companyInfo from MongoDB based stockCodes");
        return sb.toString();
    }

    
    /**
     * 使用表companyInfo，返回单个企业的基本信息，用于展示企业详情信息
     * @param stockCode
     * @return {}，详见/server-192/src/main/resources/FrontEndData/BasicInfo/companyInfo.json
     */
    public static String getCompanyInfo(String stockCode,MongoClient client) {

        collection = client.getDatabase("ForeSee").getCollection(tableName);
        sb= new StringBuilder();
        Document originDoc = collection.find(in("companyInfo.stock_code", stockCode)).first();
        originDoc.remove("_id");
        sb.append(originDoc.toJson());
        log.info("has already queried companyInfo from MongoDB based "+stockCode);
        return sb.toString();
    }

}
