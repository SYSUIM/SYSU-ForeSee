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
 * @ClassName NewsQuery
 * @Description 关于新闻的相关检索
 * @create 2021-03-02
 */


@Slf4j
@Component
public class NewsQuery {

    @Autowired
    JedisUtil jedisUtil;

    // @Autowired
    JedisUtil_113 jedisUtil_113 = new JedisUtil_113();
    JedisUtil_112 jedisUtil_112 = new JedisUtil_112();
    JedisUtil_106 jedisUtil_106 = new JedisUtil_106();

    //记录时间，用于测试
    long startTime;
    long finishTime;

    /**
     * 根据传入的query，返回newsId列表
     * @param words
     * @return newsIdList
     */
    public List<String> getNewsIds(String query)
    {
        startTime = System.currentTimeMillis();
        List<String> res = new ArrayList<>();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                Jedis jedis= jedisUtil.getClient();
                jedis.select(15);
                // if(jedis.exists(key)){
                //     res.addAll(jedis.smembers(key));
                //     log.info("DB 15: "+key+"; result: "+jedis.smembers(key));
                // }else{
                    jedis.close();
                    jedis = null;
                    jedis = jedisUtil_113.getClient_113();
                    jedis.select(15);
                    if(jedis.exists(key)){
                        res.addAll(jedis.smembers(key));
                        log.info("DB_113 15: "+key+"; result: "+jedis.smembers(key));
                    }else{
                        jedis.close();
                        jedis = null;
                        jedis = jedisUtil_112.getClient_112();
                        jedis.select(15);
                        if(jedis.exists(key)){
                            res.addAll(jedis.smembers(key));
                            log.info("DB_112 15: "+key+"; result: "+jedis.smembers(key));
                        }
                        else{
                            jedis.close();
                            jedis = null;
                            jedis = jedisUtil_106.getClient_106();
                            jedis.select(15);
                            if(jedis.exists(key)){
                                res.addAll(jedis.smembers(key));
                                log.info("DB_106 15: "+key+"; result: "+jedis.smembers(key));
                            }
                        }
                    }
                // }
                jedis.close();
                jedis = null;
            } catch (Exception e){
                System.out.println("Error in RedisDao getNewsIds");
                e.printStackTrace();
            }
        }
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); //去重（顺序不变）
        finishTime = System.currentTimeMillis();
        log.info("RedisDao getNewsIds process time:" + (finishTime - startTime));
//        return result.subList(0,Math.min(10,res.size())); //返回十条
        return result;  //使用分页查询时使用
    }
}