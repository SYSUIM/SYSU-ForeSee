package com.ForeSee.ForeSee.dao.RedisDao;

import com.ForeSee.ForeSee.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhongshsh
 * @ClassName RunPython
 * @Description 引用外部python代码
 * @create 2021-03-02
 */

@Slf4j
@Component
public class RunPython {
    //运行forVec.py获取向量
    private String runPython(String arg){
        String vec = null;
        try {
            Process pyprocess = Runtime.getRuntime().exec("python /home/user02/suoyin/vec/forVec.py "+arg);  //在单独的进程中执行
            pyprocess.waitFor();  //等待直到pyprocess进程执行完毕
            BufferedReader br = new BufferedReader(new InputStreamReader(pyprocess.getInputStream()));
            br.readLine();  //跳过第一行
            vec = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vec.replaceAll(" ","");
    }

    //运行forVec.py获取相似度
    private double runPython(String arg1, String arg2){
        double sim = 0;
        try {
            Process pyprocess = Runtime.getRuntime().exec("python /home/user02/suoyin/vec/forVec.py "+arg1+" "+arg2);  //在单独的进程中执行
            pyprocess.waitFor();  //等待直到pyprocess进程执行完毕
            BufferedReader br = new BufferedReader(new InputStreamReader(pyprocess.getInputStream()));
            sim = Double.parseDouble(br.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sim;
    }
}