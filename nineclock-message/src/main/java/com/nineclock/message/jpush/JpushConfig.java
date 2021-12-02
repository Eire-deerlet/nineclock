package com.nineclock.message.jpush;

import cn.jpush.api.JPushClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpushConfig {
    @Autowired
    private JpushProperties prop;
    
    @Bean
    public JPushClient jPushClient(){
        return new JPushClient(prop.getMasterSecret(), prop.getAppKey());
    }
}