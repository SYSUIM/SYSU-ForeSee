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
 * @ClassName NoticeQuery
 * @Description 关于公告的相关检索
 * @create 2021-03-02
 */


@Slf4j
@Component
public class NoticeQuery {

    @Autowired
    JedisUtil jedisUtil;

    // @Autowired
    JedisUtil_113 jedisUtil_113 = new JedisUtil_113();
    JedisUtil_112 jedisUtil_112 = new JedisUtil_112();
    JedisUtil_106 jedisUtil_106 = new JedisUtil_106();
    JedisUtil_105 jedisUtil_105 = new JedisUtil_105();


    //记录时间，用于测试
    long startTime;
    long finishTime;

    /**
     * 根据传入的query，返回noticeId列表
     * @param words
     * @return noticeIdList
     */
    public List<String> getNoticeIds(String query)
    {
        startTime = System.currentTimeMillis();
        Jedis jedis= jedisUtil_105.getClient_105();
        jedis.select(14);
        List<String> res = new ArrayList<>();
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                if(jedis.exists(key)){
                    res.addAll(jedis.smembers(key));
                    log.info("DB_105 14: "+key+"; result: "+jedis.smembers(key));
                }
            } catch (Exception e){
                System.out.println("Error in RedisDao getNoticeIds");
                e.printStackTrace();
            }
        }
        jedis.close();
        jedis = null;
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); //去重（顺序不变）
        finishTime = System.currentTimeMillis();
        log.info("RedisDao getNoticeIds process time:" + (finishTime - startTime));
        return result;  
    }
}