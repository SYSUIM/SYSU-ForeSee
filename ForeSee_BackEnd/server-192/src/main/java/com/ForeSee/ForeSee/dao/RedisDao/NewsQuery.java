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
import java.util.HashSet;
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
 * @ClassName NewsQuery
 * @Description 关于新闻的相关检索
 */


@Slf4j
@Component
public class NewsQuery {

    @Autowired
    JedisUtil jedisUtil;

    //模糊查询
    @Autowired
    FuzzySearch fz;

    // @Autowired
    JedisUtil_113 jedisUtil_113 = new JedisUtil_113();
    JedisUtil_112 jedisUtil_112 = new JedisUtil_112();
    JedisUtil_106 jedisUtil_106 = new JedisUtil_106();

    //记录时间，用于测试
    long startTime;
    long finishTime;

    //http访问flask
    @Autowired
    HttpDao httpDao;

    private int sortNum = 50;

    /**
     * 根据传入的query进行标题模糊匹配，返回newsId列表，用于新闻检索
     * @param words
     * @return newsIdList
     */
    public List<String> getNewsIds(String query, MongoClient mongoClient, String page) {

        JSONObject jsonObject = new JSONObject();
        List<String> res = new ArrayList<>();
        // 实体提取
        try {
            String jsonResult = httpDao.getEntities(query, "news");
            jsonObject =  new JSONObject(jsonResult);
            if (jsonObject.getString("core_ent").length() > 0) {
                query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent");
            } else if (jsonObject.getString("norm_ent").length() > 0){
                query = jsonObject.getString("norm_ent");
            } else {
                query = jsonObject.getString("non_ent");
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
            res.addAll(fz.FuzzySearchList(key, 9));
        }
        // int sz = res.size();
        // //内容匹配
        // try {
        //     query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent") + " " + jsonObject.getString("non_ent");
        //     query = query.trim().replace("  ", " ");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // List<String> fcIds = getNewsByContent(query);
        // if ((Integer.parseInt(page)-1)*10 <= (sz+200) && (Integer.parseInt(page)-1)*10 > sz ){
        //     // 精排
        //     try {
        //         int idSize = fcIds.size();
        //         List<String> subIds;
        //         if ( idSize >=  sortNum) {
        //             subIds = fcIds.subList(0, sortNum);
        //         } else {
        //             subIds = fcIds.subList(0, idSize);
        //         }
        //         Document dm = new Document();
        //         String id = subIds.toString();
        //         id = id.replace("[","[\"").replace("]","\"]").replace(", ","\", \"");
        //         dm.put("id", id);
        //         dm.put("queryVec", jsonObject.getJSONArray("query_vec").toString());
        //         String vec = VectorInfo.getNewsVector(subIds, mongoClient);
        //         dm.put("vectors", vec);
        //         res.addAll(Arrays.asList(httpDao.sortIds(dm.toJson()).split(" ")));
        //     }catch (Exception e){
        //         e.printStackTrace();
        //     }
        // }
        // res.addAll(fcIds);
        res.remove("");
        //去重（顺序不变）
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); 
        return result; 

    }


    /**
     * 根据传入的query进行标题模糊匹配，返回newsId列表，用于倒推
     * @param words
     * @return newsIdList
     */
    public List<String> getNewsIds(String query, MongoClient mongoClient) {
        JSONObject jsonObject = new JSONObject();
        List<String> res = new ArrayList<>();
        // 实体提取
        try {
            String jsonResult = httpDao.getEntities(query, "news");
            jsonObject =  new JSONObject(jsonResult);
            if (jsonObject.getString("core_ent").length() > 0) {
                query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent");
            } else if (jsonObject.getString("norm_ent").length() > 0){
                query = jsonObject.getString("norm_ent");
            } else {
                query = jsonObject.getString("non_ent");
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
            res.addAll(fz.FuzzySearchList(key, 9));
        }
        //内容匹配
        try {
            query = jsonObject.getString("core_ent") + " " + jsonObject.getString("norm_ent") + " " + jsonObject.getString("non_ent");
            query = query.trim().replace("  ", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> fcIds = getNewsByContent(query);
        res.addAll(fcIds);
        res.remove("");
        //去重（顺序不变）
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); 
        return result; 
    }

    /**
     * 根据传入的query，返回newsId列表
     * @param words
     * @return newsIdList
     */
    public List<String> getNewsByContent(String query)
    {
        List<String> res = new ArrayList<>();
        if (query.length() == 0) return res;
        //对检索词串进行切词
        String queries[] = query.split(" ");
        int runSize = queries.length;
        for(int i = 0; i < runSize; i++)
        {
            String key = queries[i];
            try {
                // 内容匹配
                Jedis jedis = jedisUtil_113.getClient_113();
                jedis.select(15);
                if(jedis.exists(key)){
                    res.addAll(jedis.smembers(key));
                    // log.info("DB_113 15: "+key);
                }else{
                    jedis.close();
                    jedis = null;
                    jedis = jedisUtil_112.getClient_112();
                    jedis.select(15);
                    if(jedis.exists(key)){
                        res.addAll(jedis.smembers(key));
                        // log.info("DB_112 15: "+key);
                    }
                    else{
                        jedis.close();
                        jedis = null;
                        jedis = jedisUtil_106.getClient_106();
                        jedis.select(15);
                        if(jedis.exists(key)){
                            res.addAll(jedis.smembers(key));
                            // log.info("DB_106 15: "+key);
                        }
                    }
                }
                // 保底检索策略
                if (res.size() == 0) {
                    jedis= jedisUtil.getClient();
                    jedis.select(15);
                    if(jedis.exists(key)){
                        res.addAll(jedis.smembers(key));
                    }
                    jedis.close();
                    jedis = null;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        List<String> result = new ArrayList<String>(new LinkedHashSet<String>(res)); //去重（顺序不变）
        
        return result;  
    }
}