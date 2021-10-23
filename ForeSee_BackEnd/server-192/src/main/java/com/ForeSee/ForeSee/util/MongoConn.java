package com.ForeSee.ForeSee.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import com.mongodb.MongoClientOptions;

/**
 * @author Zhongshsh
 * @ClassName MongoConn
 * @Description Mongo连接
 */

@Slf4j
// @Configuration
public class MongoConn {
    /**
     * @return Mongodb的连接
     */
    public static MongoClient getConn(){
        MongoClient mongoClient = null;
        MongoClientOptions.Builder build = new MongoClientOptions.Builder();        
        build.connectionsPerHost(50);   
        build.threadsAllowedToBlockForConnectionMultiplier(50); 
        build.maxWaitTime(1000*60*2);
        build.connectTimeout(1000*60*1);    
        MongoClientOptions myOptions = build.build();  

        try{
            mongoClient = new MongoClient("192.168.1.107:27017", myOptions);
        }catch (Exception e){
            log.error(e.getClass().getName()+": "+e.getMessage());
        }
        return mongoClient;
    }


}
