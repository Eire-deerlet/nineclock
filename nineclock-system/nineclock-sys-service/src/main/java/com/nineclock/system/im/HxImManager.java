package com.nineclock.system.im;

import com.nineclock.system.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class HxImManager {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${huanxin.orgName}")
    private String orgName;

    @Value("${huanxin.appName}")
    private String appName;

    @Value("${huanxin.clientId}")
    private String clientId;

    @Value("${huanxin.clientSecret}")
    private String clientSecret;

    /**
     * 注册单个用户到环信
     * @param sysUser
     */
    public void registerUser2HuanXing(SysUser sysUser){

        log.info("调用接口, 开始注册用户信息到环信云 : " + sysUser);

        //获取Token
        String accessTokenRequestUrl = "http://a1.easemob.com/"  + orgName + "/"+ appName+"/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("grant_type", "client_credentials");
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);

        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(map, headers);
        ResponseEntity<Map> stringResponseEntity = restTemplate.postForEntity(accessTokenRequestUrl, request, Map.class);
        String access_token = stringResponseEntity.getBody().get("access_token").toString();
        log.info("调用接口, 获取环信云接口访问token");


        // 注册用户
        String registerUserRequestUrl = "http://a1.easemob.com/"+orgName+"/"+appName+"/users";

        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.set("Authorization", "Bearer " + access_token);

        Map<String,Object> body = new HashMap<String,Object>();
        body.put("username", sysUser.getUsername());
        body.put("password", sysUser.getPassword());

        HttpEntity<Map<String, Object>> requestBody = new HttpEntity<Map<String, Object>>(body, headers1);

        ResponseEntity<String> stringResponseEntity1 = restTemplate.postForEntity(registerUserRequestUrl, requestBody, String.class);
        log.info("调用接口, 注册用户, 返回结果: " + stringResponseEntity1.getBody());
    }
}