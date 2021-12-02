package com.nineclock.common.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.nineclock.common.utils.JsonUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Slf4j
@Data
public class AliyunSmsUtils {

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String templateCode;

    //使用common工程中配置的默认的签名和模板发送短信 ;
    public Boolean sendSMS(String phone, String code) {

        log.info("调用阿里云服务,发送短信: " + phone + ", " + code);
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");


        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("调用阿里云服务发送短信完成, 返回结果 " + response.getData());
            Map map = JsonUtils.toBean(response.getData(), Map.class);
            return map.get("Code").equals("OK");

        } catch (Exception e) {
            log.error("发送短信出错：", e);
            return false;
        }
    }

}