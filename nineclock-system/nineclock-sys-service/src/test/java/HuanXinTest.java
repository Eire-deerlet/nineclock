import com.nineclock.system.NCSystemApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes= NCSystemApplication.class)
@RunWith(SpringRunner.class)
public class HuanXinTest {
    
    @Autowired
    private RestTemplate restTemplate;
	
    //编写单元测试，调用环信提供的接口完成获取令牌的操作;
    @Test
    public void getToken(){
        String url = "http://a1.easemob.com/1142211126116972/nineclock/token";

        Map<String,Object> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("client_id", "YXA6AzxxKq68SYiGFyonJHjI8Q");
        body.put("client_secret", "YXA6r0HS1HIxe7Yr6owYxN80BIkpkZI");

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(body, headers);

        ResponseEntity<Map> entity = restTemplate.postForEntity(url, request, Map.class);

        Map result = entity.getBody();

        System.out.println(result);
    }
    //在注册用户时，需要将刚才获取到的令牌携带在请求头中 ;
    @Test
    public void registerUser(){
        String url = "http://a1.easemob.com/1142211126116972/nineclock/users";

        Map<String, Object> body = new HashMap<>();
        body.put("username", "13335440011");
        body.put("password", "123456");

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer YWMtQfKv4E6aEey1iLHdbgPSf5CSsdXbvzaShfrNeL5XSB0DPHEqrrxJiIYXKickeMjxAgMAAAF9W4zczwWP1ADASC1ItcWcaZCyI5w4Usc13WgO52JRw-BWcp297HC-lg");
        //application=033c712a-aebc-4988-8617-2a272478c8f1, access_token=YWMtQfKv4E6aEey1iLHdbgPSf5CSsdXbvzaShfrNeL5XSB0DPHEqrrxJiIYXKickeMjxAgMAAAF9W4zczwWP1ADASC1ItcWcaZCyI5w4Usc13WgO52JRw-BWcp297HC-lg, expires_in=93312000

        HttpEntity<Map<String,Object>> request = new HttpEntity<Map<String,Object>>(body, headers);

        ResponseEntity<Map> entity = restTemplate.postForEntity(url, request, Map.class);
        Map result = entity.getBody();
        System.out.println(result);
    }
}   