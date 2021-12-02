package com.nineclock.document;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@MapperScan(basePackages = "com.nineclock.document.mapper")
@ServletComponentScan(basePackages = "com.nineclock.common.filter")
@EnableFeignClients(basePackages = "com.nineclock.system.feign")
@EnableDiscoveryClient
public class NcDocumentApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcDocumentApplication.class, args);
    }
}
