
package com.nineclock.attendance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.nineclock.system.feign")
@MapperScan(basePackages = "com.nineclock.attendance.mapper")
@ServletComponentScan(basePackages = "com.nineclock.common.filter")
public class NcAttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcAttendanceApplication.class, args);
    }
}