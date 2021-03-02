package com.ForeSee.ForeSee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class eurekaServerMain {
    public static void main(String[] args) {
        SpringApplication.run(eurekaServerMain.class,args);
    }
}
