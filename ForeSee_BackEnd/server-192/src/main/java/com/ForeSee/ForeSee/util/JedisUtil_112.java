package com.ForeSee.ForeSee.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Zhongshsh
 * @ClassName JedisUtil
 * @Description JedisPool配置以及连接
 * @create 2021-02-23-8:17 下午
 */


public class JedisUtil_112 {

    public JedisUtil_112(){
    }

    public JedisPool initPoll_112(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnCreate(false);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMaxWaitMillis(10*1000);
        return new JedisPool(jedisPoolConfig,"192.168.1.112",6479,3000,"nopassword");
    }

    JedisPool jedisPool_112;
    public Jedis getClient_112(){
        jedisPool_112 = initPoll_112();
        return jedisPool_112.getResource();
    }
}

