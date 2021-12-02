package com.nineclock.auth.intergration.entity;

import lombok.Data;

import java.util.Map;

@Data
public class IntergrationAuthenticationEntity {

    private String authType; //认证类型, 如果是短信类型, 前端传递sms ; 否则就是用户名密码类型
    private Map<String, String[]> authParameters;//请求登录认证参数集合

    /***
     * 通过参数名称获取登录参数的值
     */
    public String getAuthParameter(String paramter) {
        String[] values = this.authParameters.get(paramter);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }
}