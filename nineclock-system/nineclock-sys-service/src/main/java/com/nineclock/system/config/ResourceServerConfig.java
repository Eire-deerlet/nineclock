package com.nineclock.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
   
    @Autowired
    private TokenStore tokenStore;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
            .antMatchers(
                    "/user/query", // 认证微服务调用该接口时还没有接口，因此放行该接口，无需认证也可以访问
                    "/companyUser/query", // 放行接口，因为此时正在认证中，是没有token
                    "/actuator/**",
                    "/user/hello","/user/say","/sms/code","/user/register",
                    "/v2/api-docs",
                    "/swagger-resources/configuration/ui",
                    "/swagger-resources",
                    "/swagger-resources/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**","list",
                    "/user/register", // 增加
                    "/company/list", // 增加
                    "/industry/list",// 增加，该接口用于查询企业行业, 但是当前并未使用到, 但是APP端调用了, 所以也需要加入
                    "/company/applyJoinCompany", // 增加
                    "/company/uploadOSS", // 增加
                    "/companyUser/queryAllUserByCompanyId",
                    "/companyUser/queryAdminByCompanyId"
                             ).permitAll().anyRequest().authenticated();
    }
}