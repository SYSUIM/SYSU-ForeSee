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
 * @ClassName InfoService
 * @Description 静态数据的获取
 * @create 2021-03-02
 */

@Slf4j
@Service
public class InfoService {
    @Autowired
    CompanyQuery companyQ;
    @Autowired
    IndustryQuery industryQ;

    /**
     * 根据公司的代号检索mongodb，查出它的基本信息
     * @param stockCode 公司代号
     * @return
     */
      public String getCompanyInfo(String stockCode){
        MongoClient mongoClient=null;
        StringBuffer sb;
        try {
            mongoClient = MongoConn.getConn();
            sb = new StringBuffer();
            String companyInfo = CompanyInfo.getCompanyInfo(stockCode, mongoClient);
            sb.append(companyInfo);
        }finally {
            mongoClient.close();
        }
        return sb.toString();
    }
    
    /**
     * 根据行业代号查询行业基本信息
     * @param industryCode 行业
     * @return
     */
    public String getIndustryInfo(String industryCode){
        MongoClient mongoClient=null;
        StringBuilder sb=new StringBuilder();
        try {
            mongoClient = MongoConn.getConn();
            sb.append(IndustryInfo.getIndustryInfo(industryCode,mongoClient));
        }finally {
            mongoClient.close();
        }
        return sb.toString();
    }
    
    /**
     * 返回企业公告，由新到旧，每页20条
     * @param stockCode
     * @param page
     * @return
     */
    public String getNotice(String stockCode, String page){
        MongoClient mongoClient=null;
        StringBuilder sb=new StringBuilder();
        try{
            mongoClient=MongoConn.getConn();
            sb.append(StockNotice.getAllNotice(stockCode,mongoClient,page));
        }finally{
            mongoClient.close();
        }
        return sb.toString();
    }

    /**
     * 返回企业研究报告，由新到旧，每页20条
     * @param stockCode
     * @param page
     * @return
     */
    public String getResearch(String stockCode, String page){
        MongoClient mongoClient=null;
        StringBuilder sb=new StringBuilder();
        try{
            mongoClient=MongoConn.getConn();
            sb.append(StockResearch.getAllResearch(stockCode,mongoClient,page));
        }finally{
            mongoClient.close();
        }
        return sb.toString();
    }

    /**
     * 返回单个stockCode的News:如果是stock，由新到旧，每页20条；如果是industry，返回每个企业最新的一条，每页3条。
     * @param stockCode
     * @param page
     * @return
     */
    public String getCompanyNews(String stockCode, String page){
        
        MongoClient mongoClient=null;
        StringBuilder sb=new StringBuilder();
        try{
            mongoClient=MongoConn.getConn();
            sb.append(StockNews.getAllNews(stockCode,mongoClient,page));
            
        }finally{
            mongoClient.close();
        }
        
        return sb.toString();
    }

    /**
     * 返回单个industryCode的News:返回每个企业最新的一条，每页3条。
     * industryCode的News通过stockCode获取，因此需要使用RedisDao先获得行业的企业信息
     * industryCode可以是industryCode/industryName
     * @param industryCode
     * @param page
     * @return
     */
    public String getIndustryNews(String industryCode, String page){
        MongoClient mongoClient=null;
        StringBuilder sb=new StringBuilder();
        try{
            List<String> stockCodes = companyQ.getStockCodeBasedIndustry(industryCode);
            mongoClient=MongoConn.getConn();
            sb.append(IndustryNews.getAllNews(stockCodes, mongoClient, page));
        }finally{
            mongoClient.close();
        }
        
        return sb.toString();
    }


    /**
     * 返回行业资讯，由新到旧，每页20条
     * industryCode可以是industryCode/industryName，因此需要通过RedisDao转成industryCode再到mongoDB查询
     * @param industryCode
     * @param page
     * @return
     */
    public String getReport(String industryCode,String page){
        MongoClient mongoClient=null;
        StringBuilder sb=new StringBuilder();
        try{
            mongoClient=MongoConn.getConn();
            industryCode=industryQ.getIndustryCode(industryCode);
            sb.append(IndustryReport.getIndustryReport(industryCode,mongoClient,page));
        }finally{
            mongoClient.close();
        }
        return sb.toString();
    }


}
