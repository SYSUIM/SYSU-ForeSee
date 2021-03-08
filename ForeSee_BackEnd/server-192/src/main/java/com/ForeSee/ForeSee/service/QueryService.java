package com.ForeSee.ForeSee.service;

import com.ForeSee.ForeSee.dao.MongoDBDao.*;
import com.ForeSee.ForeSee.dao.RedisDao.*;
import com.ForeSee.ForeSee.dao.Neo4jDao.*;
import com.ForeSee.ForeSee.util.*;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.json.JSONObject;
import java.util.Arrays;
import org.bson.Document;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * @author zhongshsh
 * @ClassName QueryService
 * @Description 检索数据的获取
 */

@Slf4j
@Service
public class QueryService {
    @Autowired
    CompanyQuery companyQ;
    @Autowired
    IndustryQuery industryQ;
    @Autowired
    NoticeQuery noticeQ;
    @Autowired
    NewsQuery newsQ;
    @Autowired
    ReportQuery reportQ;
    @Autowired
    RelationQuery relationQ;
    @Autowired
    RuleMatchDao rule;
    @Autowired
    HttpUtil httpUtil;


    /**
     * 先将检索词传给正则方法，抽取实体和关系；再到图数据库，找出相关关系
     * @param query 检索词
     * @return
     */
    public String getRelationQuery(String query){
        String relationInfo;
        try {
            // 正则匹配
            List<String> target = rule.getRelation(query);
            // neo4j查询
            relationInfo = relationQ.getRelation(target);
        }catch (Exception e){
            e.printStackTrace();
            relationInfo = "[]";
        }
        return relationInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个公告的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getNoticeQuery(String query, String page){
        // 实体提取
        String jsonResult=getEntities(query, "notice");
        JSONObject jsonObject= new JSONObject(jsonResult);
        query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent") + " " + jsonObject.getString("non_ent");
        // redis查询返回noticeIds，notice本身是标题检索，因此不需要重排
        List<String> noticeIds = noticeQ.getNoticeIds(query);
        log.info("Notice Query matching result number: " + noticeIds.size());
        // mongodb方法
        MongoClient mongoClient=null;
        String noticeInfo;
        try {
            mongoClient = MongoConn.getConn();
            // 如果检索不到结果
            if (noticeIds.size()==0) {
                // 根据企业索引倒推
                List<String> stockCodes = companyQ.getStockCodes(query);
                if (stockCodes.size()==0){
                    // 根据新闻索引倒推
                    List<String> newsIds = newsQ.getNewsIds(query);
                    // 根据news倒推stockCodes
                    stockCodes = StockNews.getStockCodes(newsIds, mongoClient);
                }
                // 根据industryCodes检索出report内容
                noticeInfo = StockNotice.getNoticeBasedStockCodes(stockCodes, mongoClient, page);
            } else {
                noticeInfo = StockNotice.getNoticeBasedQuery(noticeIds, mongoClient, page);
            }
        }catch (Exception e){
            e.printStackTrace();
            noticeInfo = "[]";
        }
        mongoClient.close();
        return noticeInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个资讯的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getReportQuery(String query, String page){
        // 实体提取
        String jsonResult=getEntities(query, "report");
        JSONObject jsonObject= new JSONObject(jsonResult);
        query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent") + " " + jsonObject.getString("non_ent");
        // redis查询返回stockCodeList
        List<String> reportIds = reportQ.getReportIds(query);
        log.info("Report Query matching result number: " + reportIds.size());
        // mongodb方法
        MongoClient mongoClient=null;
        String reportInfo;
        try {
            mongoClient = MongoConn.getConn();
            // 如果检索不到结果
            if (reportIds.size()==0) {
                // 根据企业索引倒推
                List<String> stockCodes = companyQ.getStockCodes(query);
                if (stockCodes.size()==0){
                    // 根据新闻索引倒推
                    List<String> newsIds = newsQ.getNewsIds(query);
                    // 根据news倒推stockCodes
                    stockCodes = StockNews.getStockCodes(newsIds, mongoClient);
                }
                // 根据stockCodes到redis DB2中检索出对应的industryCodes
                List<String> industryCodes = industryQ.getIndustryCodes(stockCodes);
                // 根据industryCodes检索出report内容
                reportInfo = IndustryReport.getReportBasedIndustryCodes(industryCodes, mongoClient, page);
            } else {
                reportInfo = IndustryReport.getReportBasedQuery(reportIds, mongoClient, page);
            }
        } catch (Exception e){
            e.printStackTrace();
            reportInfo = "[]";
        }
        mongoClient.close();
        return reportInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个新闻的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getNewsQuery(String query, String page){
        // 实体提取
        String jsonResult=getEntities(query, "news");
        JSONObject jsonObject= new JSONObject(jsonResult);
        query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent");
        // redis查询返回newsIds
        List<String> newsIds = newsQ.newsFuzzySearch(query);
        String newsInfo;
        MongoClient mongoClient=null;
        try {
            
            mongoClient = MongoConn.getConn();
            // 返回的id数比需求页数少
            if (newsIds.size() < Integer.parseInt(page)*10) {
                query = query + " " + jsonObject.getString("non_ent");
                List<String> fNewsIds = newsQ.getNewsIds(query);
                // 精排
                try {
                    List<String> subNewsIds = fNewsIds.subList(0, 200);
                    Document dm = new Document();
                    dm.put("id", subNewsIds.toString());
                    dm.put("queryVec", jsonObject.getString("query_vec"));
                    String vec = VectorInfo.getNewsVector(subNewsIds, mongoClient);
                    dm.put("vectors", vec);
                    newsIds.addAll(Arrays.asList(sortIds(dm.toJson()).split(" ")));
                }catch (Exception e){
                    e.printStackTrace();
                }
                newsIds.addAll(fNewsIds);
            }
            if (newsIds.size()==0){
                // 根据企业索引倒推
                List<String> stockCodes = companyQ.getStockCodes(query);
                newsInfo = StockNews.getNewsBasedStockCodes(stockCodes, mongoClient, page);
            } else {
                newsInfo = StockNews.getNewsBasedQuery(newsIds, mongoClient, page);
            }
        }catch (Exception e){
            e.printStackTrace();
            newsInfo = "[]";
        }
        
        mongoClient.close();
        return newsInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个行业的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getIndustryQuery(String query){
        // 实体提取
        String jsonResult=getEntities(query, "industry");
        JSONObject jsonObject= new JSONObject(jsonResult);
        query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent");
        List<String> industryCodes = industryQ.industryFuzzySearch(query);
        // mongodb方法
        MongoClient mongoClient=null;
        String industryInfo;
        try {
            mongoClient = MongoConn.getConn();
            query = query + " " + jsonObject.getString("non_ent");
            List<String> fcIds = industryQ.getIndustryCodes(query);
            // 精排
            try {
                List<String> subIds = fcIds.subList(0, 200);
                Document dm = new Document();
                dm.put("id", subIds.toString());
                dm.put("queryVec", jsonObject.getString("query_vec"));
                String vec = VectorInfo.getNewsVector(subIds, mongoClient);
                dm.put("vectors", vec);
                industryCodes.addAll(Arrays.asList(sortIds(dm.toJson()).split(" ")));
            }catch (Exception e){
                e.printStackTrace();
            }
            industryCodes.addAll(fcIds);
            // 如果检索不到结果
            if (industryCodes.size()==0) {
                // redis查询返回newsIds
                List<String> newsIds = newsQ.getNewsIds(query);
                // 根据news倒推stockCodes
                List<String> stockCodes = StockNews.getStockCodes(newsIds, mongoClient);
                // 根据stockCodes到redis DB2中检索出对应的industryCodes
                industryCodes = industryQ.getIndustryCodes(stockCodes);
            }
            industryInfo = IndustryInfo.getIndustryInfo(industryCodes, mongoClient);
        }catch (Exception e){
            e.printStackTrace();
            industryInfo = "[]";
        }
        mongoClient.close();
        return industryInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个公司的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getCompanyQuery(String query, String page){
        // 实体提取
        String jsonResult=getEntities(query, "stock");
        JSONObject jsonObject= new JSONObject(jsonResult);
        query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent");

        // redis查询返回stockCodeList
        List<String> stockCodes = companyQ.companyFuzzySearch(query);
        // mongodb方法
        MongoClient mongoClient=null;
        String companyInfo;
        try {
            mongoClient = MongoConn.getConn();
            // 返回的id数比需求页数少
            if (stockCodes.size() < Integer.parseInt(page)*6) {
                query = query + " " + jsonObject.getString("non_ent");
                List<String> fcIds = industryQ.getIndustryCodes(query);
                // 精排
                try {
                    List<String> subIds = fcIds.subList(0, 200);
                    Document dm = new Document();
                    dm.put("id", subIds.toString());
                    dm.put("queryVec", jsonObject.getString("query_vec"));
                    String vec = VectorInfo.getNewsVector(subIds, mongoClient);
                    dm.put("vectors", vec);
                    stockCodes.addAll(Arrays.asList(sortIds(dm.toJson()).split(" ")));
                }catch (Exception e){
                    e.printStackTrace();
                }
                stockCodes.addAll(fcIds);
            }
            // 如果检索不到结果
            if (stockCodes.size()==0) {
                // redis查询返回newsIds
                List<String> newsIds = newsQ.getNewsIds(query);
                // 根据news倒推stockCodes
                stockCodes = StockNews.getStockCodes(newsIds, mongoClient);
            }
            companyInfo = CompanyInfo.getCompanyInfo(stockCodes, mongoClient, page);
        }catch (Exception e){
            e.printStackTrace();
            companyInfo = "[]";
        }
        mongoClient.close();
        return companyInfo;
    }

    /**
     * 将value和type传给模型，返回实体提取后的数据
     * @param 
     * @return
     */
    public String getEntities(String values, String type){
        String url = "http://192.168.1.107:7788/qu?query=";
        try {
            url = url + URLEncoder.encode(values,"utf-8") + "&type=" + type;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpUtil.sendGet(url);
    }

    /**
     * 将id和词向量传给模型，返回精排之后的id
     * @param 
     * @return
     */
    public String sortIds(String values){
        String url = "http://192.168.1.107:7789/rank";
        return httpUtil.sendPost(url, values);
    }

}
