package com.nineclock.approve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.nineclock.approve.mapper")
@ServletComponentScan(basePackages ="com.nineclock.common.filter")
public class NcApproveApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcApproveApplication.class, args);
    }
}
