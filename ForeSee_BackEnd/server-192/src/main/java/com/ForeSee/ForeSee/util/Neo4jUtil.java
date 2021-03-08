package com.ForeSee.ForeSee.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.v1.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.neo4j.driver.v1.Values.parameters;


/**
 * @author zhongshsh
 * @ClassName Neo4jUtil
 * @Description 图数据库连接
 */

@Slf4j
@Component
public class Neo4jUtil {

    private static Driver driver;
    private static Session session;
    

    public static Driver getClient() {
        driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "nopassword" ) );
        return driver ;
    }

}