package com.ForeSee.ForeSee.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public class InfoServiceTest {
    @Autowired
    InfoService infoService;
    @Test
    public void getAllInfo(){
        String result = infoService.getIndustryReports("600485","1");
        log.info(result);
    }
}
