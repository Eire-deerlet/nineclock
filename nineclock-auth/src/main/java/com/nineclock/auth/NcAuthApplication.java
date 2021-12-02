package com.nineclock.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.nineclock.system.feign")
@SpringBootApplication
public class NcAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcAuthApplication.class, args);
    }
}
