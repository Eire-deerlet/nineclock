package com.nineclock.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@MapperScan(basePackages = "com.nineclock.system.mapper")
@EnableDiscoveryClient
@SpringBootApplication
@ServletComponentScan(basePackages = "com.nineclock.common.filter")
public class NCSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(NCSystemApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
