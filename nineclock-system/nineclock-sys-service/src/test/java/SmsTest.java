package com.nineclock.system;

import com.nineclock.common.sms.AliyunSmsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsTest {

    @Autowired
    private AliyunSmsUtils smsUtils;

    @Test
    public void testSendSms(){
        Boolean result = smsUtils.sendSMS("13660605731", "123456");
        System.out.println("发送短信结果：" + result);
    }
}