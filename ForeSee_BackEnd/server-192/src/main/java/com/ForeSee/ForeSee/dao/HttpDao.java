package com.ForeSee.ForeSee.dao;

import com.ForeSee.ForeSee.util.*;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhongshsh
 * @ClassName HttpDao
 * @Description 访问flask
 */

@Slf4j
@Component
public class HttpDao {

    @Autowired
    HttpUtil httpUtil;

    /**
     * 将value和type传给模型，返回实体提取后的数据
     * @param 
     * @return
     */
    public String getEntities(String values, String type){
        String url = "http://192.168.1.107:7788/qu?query=";
        try {
            url = url + URLEncoder.encode(values,"utf-8") + "&type=" + type;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpUtil.sendGet(url);
    }

    /**
     * 将id和词向量传给模型，返回精排之后的id
     * @param 
     * @return
     */
    public String sortIds(String values){
        String url = "http://192.168.1.107:7789/rank";
        values = values.replace("\"[", "[").replace("]\"", "]").replace("\\", "");
        return httpUtil.sendPost(url, values);
    }
}