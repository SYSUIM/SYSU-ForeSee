package com.ForeSee.ForeSee.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@CrossOrigin("*")
@RestController
public class InfoController {
    @Autowired
    RestTemplate restTemplate;

    @Value("${http.REST_URL_PREFIX}")
    String REST_URL_PREFIX;

    /**
     * 根据关键词检索关系
     * @param query 关键词
     * @return
     */
    @GetMapping("/relation/{query}")
    public String getRelationQuery(@PathVariable("query")String query){
        log.info("Receive getRelationQuery request:" + query);
        String url = REST_URL_PREFIX + "/relation/" + query;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据关键词检索行业内容
     * @param query 关键词
     * @return
     */
    @GetMapping("/industryQuery/{query}")
    public String getIndustryQuery(@PathVariable("query")String query){
        log.info("Receive getCompanyInfo request:" + query);
        String url = REST_URL_PREFIX + "/industryQuery/" + query;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据关键词检索企业内容
     * @param query 关键词
     * @return
     */
    @GetMapping("/companyQuery/{query}/{page}")
    public String getCompanyQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("Receive getCompanyInfo request:" + query + " page=" + page);
        String url = REST_URL_PREFIX + "/companyQuery/" + query + "/" + page;;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据关键词检索资讯内容
     * @param query 关键词
     * @return
     */
    @GetMapping("/reportQuery/{query}/{page}")
    public String getReportQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("Receive reportQuery request:" + query+" page="+page);
        String url = REST_URL_PREFIX + "/reportQuery/" + query+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据关键词检索新闻内容
     * @param query 关键词
     * @return
     */
    @GetMapping("/newsQuery/{query}/{page}")
    public String getNewsQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("Receive newsQuery request:" + query+" page="+page);
        String url = REST_URL_PREFIX + "/newsQuery/" + query+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据关键词检索公告内容
     * @param query 关键词
     * @return
     */
    @GetMapping("/noticeQuery/{query}/{page}")
    public String getNoticeQuery(@PathVariable("query")String query,@PathVariable("page")String page){
        log.info("Receive noticeQuery request:" + query+" page="+page);
        String url = REST_URL_PREFIX + "/noticeQuery/" + query+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据stockCode检索某一页的Notice
     * @param stockCode page
     * @return
     */
    @GetMapping("/notice/{stockCode}/{page}")
    public String getAllNotice(@PathVariable("stockCode")String stockCode,@PathVariable("page")String page){
        log.info("Receive getAllNotice request:" + stockCode+" page="+page);
        String url = REST_URL_PREFIX + "/notice/" + stockCode+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据stockCode返回某一页的研究报告
     * @param stockCode
     * @param page
     * @return
     */
    @GetMapping("/research/{stockCode}/{page}")
    public String getAllResearch(@PathVariable("stockCode")String stockCode,@PathVariable("page")String page){
        log.info("Receive getAllResearch request:" + stockCode+" page="+page);
        String url = REST_URL_PREFIX + "/research/" + stockCode+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }


    /**
     * 根据industryCode返回某一页的News
     * @param industryCode
     * @param page
     * @return
     */
    @GetMapping("/industryNews/{industryCode}/{page}")
    public String getIndustryReports(@PathVariable("industryCode")String industryCode,@PathVariable("page")String page){
        log.info("Receive  getIndustryReports industryCode: " + industryCode+" page:"+page);
        String url = REST_URL_PREFIX + "/industryNews/" + industryCode+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据stockCode检索某一页的News
     * @param stockCode page
     * @return
     */
    @GetMapping("/companyNews/{stockCode}/{page}")
    public String getAllNews(@PathVariable("stockCode")String stockCode,@PathVariable("page")String page){
        log.info("Receive getAllNews request:" + stockCode+" page="+page);
        String url = REST_URL_PREFIX + "/companyNews/" + stockCode+"/"+page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 根据industryCode返回某一页的资讯
     * @param industryCode
     * @param page
     * @return
     */
    @GetMapping("/report/{industryCode}/{page}")
    public String getIndustryIndex(@PathVariable("industryCode")String industryCode, @PathVariable("page")String page){

        log.info("Receive getIndustryIndex request:" + industryCode +" page=" + page);

        String url = REST_URL_PREFIX + "/report/" + industryCode + "/" + page;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 检索某个企业的所有相关信息
     * @param stockCode 股票代码
     * @return
     */
    @GetMapping("/companyInfo/{stockCode}")
    public String getAllInfo(@PathVariable("stockCode")String stockCode){
        log.info("Receive getAllInfo request:" + stockCode);
        String url = REST_URL_PREFIX + "/companyInfo/" + stockCode;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

    /**
     * 检索某个行业的相关信息
     * @param industryCode 行业代号
     * @return
     */
    @GetMapping("/industryInfo/{industryCode}")
    public String getIndustryInfo(@PathVariable("industryCode")String industryCode) {
        log.info("Receive getIndustryInfo request:" + industryCode);
        String url = REST_URL_PREFIX + "/industryInfo/" + industryCode;
        String result = restTemplate.getForObject(url, String.class);
        log.info("Result: " + result);
        return result;
    }

}
