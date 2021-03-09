package com.ForeSee.ForeSee.dao.Neo4jDao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongshsh
 * @ClassName RuleMatchDao
 * @Description 正则匹配
 * @create 2021-03-03
 */

@Slf4j
@Component
public class RuleMatchDao {

    public static List<String> getRelation(String query){
        List<String> target = new ArrayList<>();
        String name = "", rl = "";
        
        String pattern;
        boolean isMatch;
        
        // 判断是否有关键词：的
        pattern = ".*的.*";
        isMatch = Pattern.matches(pattern, query);
        // 如果包含“的”
        if (isMatch){
            pattern = ".*是.*";
            isMatch = Pattern.matches(pattern, query);
            if (isMatch) {
                pattern = "(\\D*)的(\\D*)是.*";
            } else {
                pattern = "(\\D*)的(\\D*)";
            } 
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(query);
            if (m.find( )) {
                name =  m.group(1);
                rl = m.group(2);
            } 
        } else {
            pattern = ".* .*";
            isMatch = Pattern.matches(pattern, query);
            // 如果包含“的”
            if (isMatch){
                pattern = "(\\D*) (\\D*)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(query);
                if (m.find( )) {
                    name =  m.group(1);
                    rl = m.group(2);
                } 
            }
        }

        target.add(name);
        target.add(rl);
        
        return target;
    }
}