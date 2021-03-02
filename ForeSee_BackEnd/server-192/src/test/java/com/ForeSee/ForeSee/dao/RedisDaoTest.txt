package com.ForeSee.ForeSee.dao;

import com.ForeSee.ForeSee.dao.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Zenglr
 * @ClassName
 * @Description
 * @create 2020-10-16-7:16 下午
 */
@SpringBootTest
@Slf4j
public class RedisDaoTest {
    @Autowired
    RedisDao redisDao;

    @Test
    public void getStockCode(){
        String query = "大东海A";
        log.info("使用"+query+"查询");
        String result = redisDao.getStockcode(query);
        String testAns = "000613";
        log.info(result);
        Assert.assertEquals("Wrong output！",testAns,result);
    }

    @Test
    public void getStockCodeListTest(){
        String query = "600485 BK0427 汽车 中环";
        log.info("使用"+query+"进行模糊查询");
        List<String> result = redisDao.getStockCodeList(query);
        log.info(result.toString());
    }

    @Test
    public void getAnsOnPageTest()
    {
        String query = "汽车 上海 中天 东方";
        log.info("测试getAnsOnPageTest");
        List<String> res = redisDao.getAnsOnPage(2,query);
        log.info(res.size()+res.toString());
    }

    @Test
    public void getPageNum()
    {
        String query = "汽车 上海 中天 东方";
        log.info("测试getPageNum");
        Long num = redisDao.getPageNum(query);
        log.info("一共有"+num+"页");
    }

}
