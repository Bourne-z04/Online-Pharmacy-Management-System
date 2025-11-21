package com.pharmacy.online;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 百惠康大药房在线商城后端启动类
 */
@SpringBootApplication
@MapperScan("com.pharmacy.online.mapper")
@EnableScheduling
public class OnlineMarketApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OnlineMarketApplication.class, args);
        System.out.println("========================================");
        System.out.println("百惠康大药房在线商城后端服务启动成功！");
        System.out.println("========================================");
    }
}

