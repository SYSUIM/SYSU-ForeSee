package com.ForeSee.ForeSee.dao.RedisDao;

import com.ForeSee.ForeSee.util.*;
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
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhongshsh
 * @ClassName FuzzySearch
 * @Description 模糊匹配
 */

@Slf4j
@Component
class FuzzySearch { 
    
    @Autowired
    JedisUtil jedisUtil;

    /**
     * 使用Jedis模糊查询query
     * @param query
     * @return result
     */
    public List<String> FuzzySearchList(String query, int db)
    {
        Jedis jedis = jedisUtil.getClient();
        jedis.select(db);
        List<String> keys = FuzzySearchQuery(query, db);
        List<String> list = new ArrayList<>();
        if(keys.size()>0){
            for(String key : keys){
                if (db == 2) list.add(jedis.get(key));
                else list.addAll(jedis.smembers(key));
            }
        }
        jedis.close();
        jedis = null;
        return list;
    }

    /**
     * 进行模糊匹配
     * @param query
     * @return
     */
    public List<String> FuzzySearchQuery(String query, int db){
        String pattern=query.trim().replaceAll("\\s+","*");
        pattern="*" + pattern + "*";
        List<String>res=jedisScan(pattern, db);
        // log.info("{} 模糊匹配,size:{}, db: {}",pattern, res.size(), db);
        return res;
    }

    /**
     * 使用jedis进行key的扫描匹配
     * @param pattern
     * @return keys
     */
    private List<String> jedisScan(String pattern, int db){
        Jedis jedis= jedisUtil.getClient();
        jedis.select(db);
        List<String> keys = new ArrayList<>();
        Set<String> set = jedis.keys(pattern);  
        keys.addAll(set);
        jedis.close();
        jedis = null;
        return keys;
    }

    
}