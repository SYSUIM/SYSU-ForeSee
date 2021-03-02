package com.ForeSee.ForeSee.controller;

import com.ForeSee.ForeSee.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zhongshsh
 * @ClassName QueryController
 * @Description 检索数据的获取
 * @create 2021-03-02
 */


@Slf4j
@RestController
public class QueryController {
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
    
}

