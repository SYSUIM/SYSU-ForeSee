package com.ForeSee.ForeSee.controller;

import com.ForeSee.ForeSee.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zhongshsh
 * @ClassName InfoController
 * @Description 静态数据的获取
 * @create 2021-03-02
 */

@Slf4j
@RestController
public class InfoController {

    @Autowired
    QueryService queryService;
    // http://121.46.19.26:8288/ForeSee

    /**
     * 根据关键词检索新闻
     * @param query 关键词
     * @return
     */
    @GetMapping("/newsQuery/{query}/{page}")
    public String getNewsQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("getNewsQuery query: " + query);
        String newsInfo = queryService.getNewsQuery(query, page);
        return newsInfo;
    }

    /**
     * 根据关键词检索公告
     * @param query 关键词
     * @return
     */
    @GetMapping("/noticeQuery/{query}/{page}")
    public String getNoticeQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("getNoticeQuery query: " + query);
        String noticeInfo = queryService.getNoticeQuery(query, page);

        return noticeInfo;
    }

    /**
     * 根据关键词检索资讯
     * @param query 关键词
     * @return
     */
    @GetMapping("/reportQuery/{query}/{page}")
    public String getReportQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("getReportQuery query: " + query);
        String reportInfo = queryService.getReportQuery(query, page);
        return reportInfo;
    }

    /**
     * 根据关键词检索行业
     * @param query 关键词
     * @return
     */
    @GetMapping("/industryQuery/{query}")
    public String getIndustryQuery(@PathVariable("query")String query){
        log.info("getIndustryQuery query: " + query);
        String industryInfo = queryService.getIndustryQuery(query);
        // log.info("Result: " + industryInfo);
        return industryInfo;
    }

    /**
     * 根据关键词检索企业
     * @param query 关键词
     * @return
     */
    @GetMapping("/companyQuery/{query}")
    public String getCompanyQuery(@PathVariable("query")String query){
        log.info("getCompanyQuery query: " + query);
        String companyInfo = queryService.getCompanyQuery(query);
        // log.info("Result: " + companyInfo);
        return companyInfo;
    }

    @Autowired
    InfoService infoService;
    // http://121.46.19.26:8288/ForeSee

    /**
     * 检索某个公司的静态数据
     * @param stockCode 公司代号
     * @return
     */
    @GetMapping("/companyInfo/{stockCode}")
    public String getCompanyInfo(@PathVariable("stockCode")String stockCode){
        log.info("getCompanyInfo stockCode: "+stockCode);
        String allInfo = infoService.getCompanyInfo(stockCode);
        // log.info("Result: " + allInfo);
        return allInfo;
    }

    /**
     * 根据行业代号查询简介、股票url、地理位置，即行业静态数据
     * @param industryCode 行业代号
     * @return
     */
    @GetMapping("/industryInfo/{industryCode}")
    public String getIndustryInfo(@PathVariable("industryCode")String industryCode){
        log.info("getIndustryInfo industryCode/name: " + industryCode);
        String industryInfo = infoService.getIndustryInfo(industryCode);
        // log.info("Result: " + industryInfo);
        return industryInfo;
    }


    /**
     * 根据stockCode返回News
     * @param stockCode page
     * @return
     */
    @GetMapping("/companyNews/{stockCode}/{page}")
    public String getCompanyNews(@PathVariable("stockCode")String stockCode,@PathVariable("page")String page){
        log.info("getCompanyNews Code: " + stockCode+" page:"+page);
        String companyNews = infoService.getCompanyNews(stockCode, page);
        // log.info("Result: " + companyInfo);
        return companyNews;
    }

    /**
     * 根据industryCode返回News
     * @param industryCode page
     * @return
     */
    @GetMapping("/industryNews/{industryCode}/{page}")
    public String getIndustryNews(@PathVariable("industryCode")String industryCode,@PathVariable("page")String page){
        log.info("getIndustryNews Code/Name: " + industryCode+" page:"+page);
        String industryNews = infoService.getIndustryNews(industryCode, page);
        // log.info("Result: " + companyInfo);
        return industryNews;
    }


    /**
     * 根据stockCode返回某一页的Notice 公告
     * @param stockCode
     * @param page
     * @return
     */
    @GetMapping("/notice/{stockCode}/{page}")
    public String getAllNotice(@PathVariable("stockCode")String stockCode,@PathVariable("page")String page){
        log.info("getAllNotice stockCode: " + stockCode+" page:"+page);
        String noticeInfo = infoService.getNotice(stockCode, page);
        return noticeInfo;
    }

    /**
     * 根据stockCode返回某一页的Research 公告
     * @param stockCode
     * @param page
     * @return
     */
    @GetMapping("/research/{stockCode}/{page}")
    public String getAllResearch(@PathVariable("stockCode")String stockCode,@PathVariable("page")String page){
        log.info("getAllResearch stockCode: " + stockCode+" page:"+page);
        String researchInfo = infoService.getResearch(stockCode, page);
        return researchInfo;
    }

    /**
     * 根据行业代号查询资讯
     * @param industryCode 行业代号
     * @return
     */
    @GetMapping("/report/{industryCode}/{page}")
    public String getIndustryReport(@PathVariable("industryCode")String industryCode, @PathVariable("page")String page){
        log.info("getIndustryReport Code/Name: " + industryCode + " page:" + page);
        String reportInfo = infoService.getReport(industryCode, page);
        return reportInfo;
    }
   
}

