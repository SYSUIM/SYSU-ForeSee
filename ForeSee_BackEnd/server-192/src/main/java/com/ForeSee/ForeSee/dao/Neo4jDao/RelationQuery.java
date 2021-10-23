package com.ForeSee.ForeSee.dao.Neo4jDao;

import com.ForeSee.ForeSee.util.*;
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

import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.bson.Document;

/**
 * @author zhongshsh
 * @ClassName RelationQuery
 * @Description 关于图数据库的关系检索
 * @create 2021-03-03
 */

@Slf4j
@Component
public class RelationQuery {

    private static Driver driver;
    private static Session session;
    private static List<String> res;

    @Autowired
    Neo4jUtil nu;

    public RelationQuery() {
        res = new ArrayList<>();
        res.add("企业名称");
        res.add("邮箱");
        res.add("联系方式");
        res.add("股票代码");
        res.add("登记机关");
        res.add("地址");
        res.add("企业类型");
        res.add("行业类型");
        res.add("法定代表人");
        res.add("成立时间");
        res.add("注册资本");
        res.add("工商注册号");
        res.add("组织机构代码");
        res.add("纳税人识别号");
        driver = nu.getClient();
    }
    

    public static String getRelation(List<String> target){
        session = driver.session();
        String name = target.get(0);
        String rl = target.get(1);
        Document doc = new Document();
        Iterator<String> it = res.iterator();
        StringBuilder sb = new StringBuilder();
        
        try {
            StatementResult result = session.run( "MATCH (a:企业名称) WHERE a.name =~ '.*"+name+".*' RETURN a AS result");
            
            if ( result.hasNext() && name.length() > 1) {
                Record record = result.next();
                String rs = record.get( "result" ).get(rl).asString();
                // log.info("name: "+name+";rl:"+rl+";result:"+rs);
                // if ( result.hasNext() ){
                //     sb = new StringBuilder("{");
                // } else {
                    // null
                    if ( !rs.equals("null") ) {
                        sb.append("{\"name\":\""+name+"\",\"relation\":\""+rl+"\",\"result\":\""+rs+"\",\"tableData\":[{");
                        while(it.hasNext()) {
                            int count = 0 ;
                            while(it.hasNext() && count < 4) {
                                String tmp = it.next();
                                count ++;
                                sb.append("\"col"+count+"\":\""+tmp+"\",");
                                count ++;
                                sb.append("\"col"+count+"\":\""+record.get( "result" ).get(tmp).asString()+"\",");
                                
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append("},{");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 1);
                        sb.append("]");
                    } else {
                        sb = new StringBuilder("{");
                    }
                // }
            }
        } catch (Exception e){
            sb = new StringBuilder("{");
            e.printStackTrace();
        }
        session.close();
        sb.append("}");
        return sb.toString();

    }

}