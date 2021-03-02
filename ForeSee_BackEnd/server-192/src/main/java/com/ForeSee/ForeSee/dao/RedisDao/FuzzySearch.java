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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhongshsh
 * @ClassName FuzzySearch
 * @Description 模糊匹配
 * @create 2021-03-02
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
        log.info("模糊匹配到keys："+keys.toString());
        List<String> list = new ArrayList<>();
        if(keys.size()>0){
            for(String key : keys){
            list.addAll(jedis.smembers(key));
            }
        }else {
            log.info("redis没有查到，返回"+list.toString());
            return list;
        }
        log.info("redis模糊查找:"+query+",返回"+list.toString());
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
        log.info("{} 模糊匹配,size:{}",pattern, res.size());
//        return res.subList(0,Math.min(10,res.size()));
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
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> keys = new ArrayList<>();
        ScanParams scanParams = new ScanParams();
        scanParams.match(pattern);
        scanParams.count(10000);
        while (true){
            //使用scan命令获取数据，使用cursor游标记录位置，下次循环使用
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            cursor = scanResult.getCursor();// 返回0 说明遍历完成
            keys = scanResult.getResult();
            if ("0".equals(cursor)){
                break;
            }
        }
        jedis.close();
        jedis = null;
        return keys;
    }

    
}