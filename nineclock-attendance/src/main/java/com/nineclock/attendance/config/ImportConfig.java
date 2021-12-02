package com.nineclock.attendance.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * com.nineclock.common.exception : 全局异常处理
 * com.nineclock.common.swagger : Swagger配置类
 * com.nineclock.common.sms : 短信工具配置类
 * com.nineclock.common.oss : 阿里云OSS存储
 */
@Configuration
@ComponentScan(basePackages = {"com.nineclock.common.exception",
                               "com.nineclock.common.swagger",
                               "com.nineclock.common.sms",
                               "com.nineclock.common.oss"
})
public class ImportConfig {
}
