package com.ForeSee.ForeSee.service;

import com.ForeSee.ForeSee.dao.MongoDBDao.*;
import com.ForeSee.ForeSee.dao.RedisDao.*;
import com.ForeSee.ForeSee.util.MongoConn;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author zhongshsh
 * @ClassName QueryService
 * @Description 检索数据的获取
 * @create 2021-03-02
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

    /**
     * 先将检索词传给redis，让redis找出这个公告的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getNoticeQuery(String query, String page){
        // redis查询返回stockCodeList
        List<String> noticeIds = noticeQ.getNoticeIds(query);
        log.info("Notice Query matching result: " + noticeIds);
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
        }finally {
            mongoClient.close();
        }
        return noticeInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个资讯的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getReportQuery(String query, String page){
        // redis查询返回stockCodeList
        List<String> reportIds = reportQ.getReportIds(query);
        log.info("Report Query matching result: " + reportIds);
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
        } finally {
            mongoClient.close();
        }
        return reportInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个新闻的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getNewsQuery(String query, String page){
        // redis查询返回newsIds
        List<String> newsIds = newsQ.getNewsIds(query);
        log.info("News Query matching result: " + newsIds);
        // mongodb方法
        MongoClient mongoClient=null;
        String newsInfo;
        try {
            mongoClient = MongoConn.getConn();
            
            if (newsIds.size()==0){
                // 根据企业索引倒推
                List<String> stockCodes = companyQ.getStockCodes(query);
                newsInfo = StockNews.getNewsBasedStockCodes(stockCodes, mongoClient, page);
            } else {
                newsInfo = StockNews.getNewsBasedQuery(newsIds, mongoClient, page);
            }
        }finally {
            mongoClient.close();
        }
        return newsInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个行业的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getIndustryQuery(String query){
        List<String> industryCodes = industryQ.getIndustryCodes(query);

        log.info("Industry matching result: " + industryCodes);
        // mongodb方法
        MongoClient mongoClient=null;
        String industryInfo;
        try {
            mongoClient = MongoConn.getConn();
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
        }finally {
            mongoClient.close();
        }
        return industryInfo;
    }

    /**
     * 先将检索词传给redis，让redis找出这个公司的代号，再用mongodb查出他的信息
     * @param query 检索词
     * @return
     */
    public String getCompanyQuery(String query){
        // redis查询返回stockCodeList
        List<String> stockCodes = companyQ.getStockCodes(query);
        log.info("Company fuzzy matching result: " + stockCodes);
        
        // mongodb方法
        MongoClient mongoClient=null;
        String companyInfo;
        
        try {
            mongoClient = MongoConn.getConn();
            // 如果检索不到结果
            if (stockCodes.size()==0) {
                // redis查询返回newsIds
                List<String> newsIds = newsQ.getNewsIds(query);
                // 根据news倒推stockCodes
                stockCodes = StockNews.getStockCodes(newsIds, mongoClient);
            }
            companyInfo = CompanyInfo.getCompanyInfo(stockCodes, mongoClient);
        }finally {
            mongoClient.close();
        }
        return companyInfo;
    }

}
