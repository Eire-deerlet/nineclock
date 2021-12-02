package com.nineclock.system.service;


public interface SmsService {


    void sendSms(String mobile, String type);

    /**
     * 短信: 验证码校验
     */
    Boolean verify(String checkcode, String mobile, String type);
}
