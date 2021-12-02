package com.nineclock.message.jpush;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "nineclock.jpush")
public class JpushProperties {
    private String appKey; //极光平台应用的唯一标识
    private String masterSecret; //用于服务器端 API 调用时与 AppKey 配合使用达到鉴权的目的
}