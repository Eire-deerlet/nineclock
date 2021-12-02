package com.nineclock.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NcGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcGatewayApplication.class, args);
    }
}
