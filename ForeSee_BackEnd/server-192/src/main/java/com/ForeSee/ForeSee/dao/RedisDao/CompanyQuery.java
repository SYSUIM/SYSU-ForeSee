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
 * @create 2021-03-02
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
        //指定线程池大小为检索词数目
        ExecutorService executor = Executors.newFixedThreadPool(runSize);
        //countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行
        final CountDownLatch latch = new CountDownLatch(runSize);
        List<String> res = new ArrayList<>();

        //对每一个检索词用一个线程执行查询
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //jedis连接在方法外面获取时，好像不能成功，所以放在了里面，存疑
                        Jedis jedis = jedisUtil.getClient();
                        jedis.select(1);
                        if(jedis.exists(key)){
                            res.addAll(jedis.smembers(key));
                            log.info("DB 1: "+key+"; result: "+jedis.smembers(key));
                        } else {
                            //模糊匹配
                            res.addAll(fz.FuzzySearchList(key, 1));
                            //进行词匹配
                            jedis.select(13);
                            if(jedis.exists(key)){
                                res.addAll(jedis.smembers(key));
                                log.info("DB 13: "+key+"; result: "+jedis.smembers(key));
                            }
                        }
                        jedis.close();
                        jedis = null;
                    } catch (Exception e){
                        System.out.println("Error in RedisDao getStockCodes");
                        e.printStackTrace();
                    }
                    //计数器-1操作
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
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
                log.info("DB 1:  "+industryCode+" ; result: "+jedis.smembers(industryCode));
            }
            
        } catch (Exception e){
            System.out.println("Error in RedisDao getStockCodeBasedIndustry");
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
