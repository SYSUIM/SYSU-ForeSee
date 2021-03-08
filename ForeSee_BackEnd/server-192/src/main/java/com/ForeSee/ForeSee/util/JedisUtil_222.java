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
 * @ClassName JedisUtil_222
 * @Description JedisPool配置以及连接
 * 通讯框架所处位置，不启用
 */


public class JedisUtil_222 {

    public JedisUtil_222(){
    }

    public JedisPool initPoll_222(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnCreate(false);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMaxWaitMillis(10*1000);
        return new JedisPool(jedisPoolConfig,"222.200.184.74",6379,3000,"nopassword");
    }

    JedisPool jedisPool_222;
    public Jedis getClient_222(){
        jedisPool_222 = initPoll_222();
        return jedisPool_222.getResource();
    }
}

