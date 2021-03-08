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
        if(queries.hasNext())
        {
            String key = queries.next();
            jedis.select(2);
            try {
                if(jedis.exists(key)){
                    // smembers方法存疑
                    res.addAll(jedis.smembers(key));
                    // log.info("DB 2: "+key+"; result: "+jedis.smembers(key));
                }
                
            } catch (Exception e){
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
    public List<String> getIndustryCodes(String query)
    {
        startTime = System.currentTimeMillis();
        Jedis jedis= jedisUtil.getClient();
        List<String> res = new ArrayList<>();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                jedis.select(2);
                if(jedis.exists(key)){
                    res.add(jedis.get(key));
                    // log.info("DB 2: "+key+"; result: "+jedis.get(key));
                }
                res.addAll(fz.FuzzySearchList(key, 2));
                jedis.select(11);
                if(jedis.exists(key)){
                    res.addAll(jedis.smembers(key));
                    // log.info("DB 11: "+key+"; result: "+jedis.smembers(key));
                }
                
            } catch (Exception e){
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
     * 根据传入的query，返回模糊匹配的industryCode列表
     * @param words
     * @return industryCodeList
     */
    public List<String> industryFuzzySearch(String query)
    {
        startTime = System.currentTimeMillis();
        Jedis jedis= jedisUtil.getClient();
        List<String> res = new ArrayList<>();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                jedis.select(2);
                if(jedis.exists(key)){
                    res.add(jedis.get(key));
                    // log.info("DB 2: "+key+"; result: "+jedis.get(key));
                }
                res.addAll(fz.FuzzySearchList(key, 2));
                
            } catch (Exception e){
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

}
