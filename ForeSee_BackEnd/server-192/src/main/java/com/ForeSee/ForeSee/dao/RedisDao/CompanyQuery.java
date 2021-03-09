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
 * @ClassName CompanyQuery
 * @Description 关于行业信息的相关检索
 */

@Slf4j
@Component
public class CompanyQuery {

    @Autowired
    JedisUtil jedisUtil;

    //模糊查询
    @Autowired
    FuzzySearch fz;

    //记录时间，用于测试
    long startTime;
    long finishTime;

    /**
     * 根据传入的query进行切词，并标题模糊匹配，返回所有检索字段对应检索结果的stockCode列表
     * @param query
     * @return stockList
     */
    public List<String> companyFuzzySearch(String query)
    {
        startTime = System.currentTimeMillis();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        List<String> res = new ArrayList<>();
        Jedis jedis = jedisUtil.getClient();
        //不适用多线程，因为每个词次序代表了重要性
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                jedis.select(1);
                //模糊匹配
                res.addAll(fz.FuzzySearchList(key, 1));
            } catch (Exception e){
                System.out.println("Error in RedisDao getStockCodes");
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
     * 根据传入的query进行切词，返回所有检索字段对应检索结果的stockCode列表
     * @param query
     * @return stockList
     */
    public List<String> getStockCodes(String query)
    {
        startTime = System.currentTimeMillis();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        List<String> res = new ArrayList<>();
        Jedis jedis = jedisUtil.getClient();
        //不适用多线程，因为每个词次序代表了重要性
        //进行词匹配
        jedis.select(13);
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                if(jedis.exists(key)){
                    res.addAll(jedis.smembers(key));
                    // log.info("DB 13: "+key+"; result: "+jedis.smembers(key));
                }
            } catch (Exception e){
                System.out.println("Error in RedisDao getStockCodes");
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
     * 根据传入的industryCode/industryName，返回stockCode列表，用于InfoService的getNews
     * @param industryCode
     * @return stockList
     */
    public List<String> getStockCodeBasedIndustry(String industryCode)
    {
        startTime = System.currentTimeMillis();
        Jedis jedis= jedisUtil.getClient();
        List<String> res = new ArrayList<>();
        try {
            jedis.select(1);
            if(jedis.exists(industryCode)){
                res.addAll(jedis.smembers(industryCode));
                // log.info("DB 1:  "+industryCode+" ; result: "+jedis.smembers(industryCode));
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        jedis.close();
        jedis = null;
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); //去重（顺序不变）
        finishTime = System.currentTimeMillis();
        log.info("RedisDao getStockCodeBasedIndustry process time:" + (finishTime - startTime));
        return result;  
    }


}
