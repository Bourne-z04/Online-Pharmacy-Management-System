package com.pharmacy.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 百惠康大药房业务运营后端启动类
 */
@SpringBootApplication
@MapperScan("com.pharmacy.business.mapper")
@EnableScheduling
public class BusinessOperationApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BusinessOperationApplication.class, args);
        System.out.println("========================================");
        System.out.println("百惠康大药房业务运营后端服务启动成功！");
        System.out.println("========================================");
    }
}

