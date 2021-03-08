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
 */

@Component
public class JedisUtil {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int MaxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int MinIdle;


    public JedisUtil(){
    }
    
    @Bean
    public JedisPool initPoll(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10000);
        jedisPoolConfig.setMaxIdle(MaxIdle);
        jedisPoolConfig.setMinIdle(MinIdle);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnCreate(false);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);

        jedisPoolConfig.setMaxWaitMillis(10*1000);
        return new JedisPool(jedisPoolConfig,host,port,3000,"nopassword");
    }

    @Autowired
    JedisPool jedisPool;
    public Jedis getClient(){
        return jedisPool.getResource();
    }
}

