package com.ForeSee.ForeSee.dao.RedisDao;

import com.ForeSee.ForeSee.util.*;
import com.ForeSee.ForeSee.dao.RedisDao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.mongodb.MongoClient;
import org.json.JSONObject;
import com.ForeSee.ForeSee.dao.MongoDBDao.*;
import org.bson.Document;
import java.util.Arrays;
import com.ForeSee.ForeSee.dao.*;
/**
 * @author zhongshsh
 * @ClassName IndustryQuery
 * @Description 关于行业信息的相关检索
 */

@Slf4j
@Component
public class IndustryQuery {

    @Autowired
    JedisUtil jedisUtil;

    //记录时间，用于测试
    long startTime;
    long finishTime;

    //模糊查询
    @Autowired
    FuzzySearch fz;

    //http访问flask
    @Autowired
    HttpDao httpDao;

    private int sortNum = 50;


    /**
     * 组合query industry 策略的方法
     * @param query
     * @return industryCodeList
     */
    public List<String> queryService(String query, MongoClient mongoClient) {
        // redis查询返回industryCodeList
        List<String> indutryCodes = getIndustryBasedCore(query);
        indutryCodes.addAll(getIndustryByContent(query));
        // 如果检索不到结果
        if (indutryCodes.size() == 0) {
            indutryCodes = getIndustryOnlyVec(query);
            indutryCodes.add(0,"true");
        } else {
            indutryCodes.add(0,"false");
        }
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(indutryCodes)); 
        return result;
    }

    /**
     * 如果核心词检索不到，调用该方法直接使用向量，返回所有检索字段对应检索结果的stockCode列表
     * @param query
     * @return industryCodeList
     */
    public List<String> getIndustryOnlyVec(String query)
    {
        String info = "{\"query\":\"" + query + "\", " + "\"type\":\"industry\" }";
        // log.info(info);
        String res = httpDao.sortIds(info);
        List<String> ids = new ArrayList<>();
        ids.addAll(Arrays.asList(res.split(" ")));
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(ids)); 
        return result;
    }

    /**
     * 根据传入的industryCode/industryName，返回所有检索字段对应检索结果的industryCode
     * @param industryCode/industryName
     * @return industryCode
     */
    public String getIndustryCode(String key)
    {
        startTime = System.currentTimeMillis();
        
        Jedis jedis = jedisUtil.getClient();
        jedis.select(2);
        String industryCode = jedis.get(key);
        // log.info("DB 2: "+key+"; result: "+industryCode);
        jedis.close();
        jedis = null;
        finishTime = System.currentTimeMillis();
        log.info("RedisDao getIndustryCode process time:" + (finishTime - startTime));

        return industryCode;
    }


    /**
     * 根据传入的stockCodes，返回industryCode列表
     * @param stockCodes
     * @return industryCodeList
     */
    public List<String> getIndustryCodes(List<String> stockCodes)
    {
        startTime = System.currentTimeMillis();
        Jedis jedis= jedisUtil.getClient();
        List<String> res = new ArrayList<>();
        Iterator<String> queries = stockCodes.iterator();
        while (queries.hasNext())
        {
            String key = queries.next();
            jedis.select(2);
            try {
                if(jedis.exists(key)){
                    res.add(jedis.get(key));
                }
                
            } catch (Exception e) {
                System.out.println("Error in RedisDao getIndustryCodes");
                e.printStackTrace();
            }
        
        }
        jedis.close();
        jedis = null;
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); //去重（顺序不变）
        finishTime = System.currentTimeMillis();
        log.info("RedisDao getIndustryCodes process time:" + (finishTime - startTime));
        return result;  
    }

    /**
     * 根据传入的query，返回industryCode列表
     * @param words
     * @return industryCodeList
     */
    public List<String> getIndustryByContent(String query)
    {
        List<String> res = new ArrayList<>();
        if (query.length() == 0) return res;
        startTime = System.currentTimeMillis();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        Jedis jedis = jedisUtil.getClient();
        //不适用多线程，因为每个词次序代表了重要性
        //进行词匹配
        jedis.select(11);
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                res.addAll(fz.FuzzySearchList(key, 2));
                if(jedis.exists(key)){
                    res.addAll(jedis.smembers(key));
                }
                res.addAll(fz.FuzzySearchList(key, 11));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        jedis.close();
        jedis = null;
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); //去重（顺序不变）
        finishTime = System.currentTimeMillis();
        log.info("RedisDao getStockCodes process time:" + (finishTime - startTime));
        return result;
    }

    /**
     * 根据传入的query，返回模糊匹配的industryCode列表
     * @param words
     * @return industryCodeList
     */
    public List<String> getIndustryBasedCore(String query)
    {

        JSONObject jsonObject = new JSONObject();
        List<String> res = new ArrayList<>();
        // 实体提取
        try {
            String jsonResult = httpDao.getEntities(query, "industry");
            jsonObject =  new JSONObject(jsonResult);
            query = jsonObject.getString("core_ent");
            query = query.trim().replace("  ", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 模糊匹配
        if (query.length() == 0) return res;
        String queries[] = query.split(" ");
        int runSize = queries.length;
        // 不适用多线程，因为每个词次序代表了重要性
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            res.addAll(fz.FuzzySearchList(key, 2));

        }
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            res.addAll(fz.FuzzySearchList(key, 11));
            
        }
        // 去重（顺序不变）
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); 
        return res;
        
    }

    
    /**
     * 根据传入的query，返回模糊匹配的industryCode列表
     * @param words
     * @return industryCodeList
     */
    public List<String> getIndustry(String query, MongoClient mongoClient) {

        JSONObject jsonObject = new JSONObject();
        List<String> res = new ArrayList<>();
        // 实体提取
        try {
            String jsonResult = httpDao.getEntities(query, "industry");
            jsonObject =  new JSONObject(jsonResult);
            if (jsonObject.getString("core_ent").length() > 0) {
                query = jsonObject.getString("core_ent");
            } else if (jsonObject.getString("norm_ent").length() > 0){
                query = jsonObject.getString("norm_ent");
            } else {
                // query = jsonObject.getString("non_ent");
                query = "";
            }
            query = query.trim().replace("  ", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 模糊匹配
        if (query.length() == 0) return res;
        String queries[] = query.split(" ");
        int runSize = queries.length;
        //不适用多线程，因为每个词次序代表了重要性
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            res.addAll(fz.FuzzySearchList(key, 2));
        }
        //内容匹配
        try {
            query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent") + " " + jsonObject.getString("non_ent");
            query = query.trim().replace("  ", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> fcIds = getIndustryByContent(query);
        res.addAll(fcIds);
        res.remove("");
        //去重（顺序不变）
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); 
        return result; 
    }

}
