package com.nineclock.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

// 二个依赖 三个配置类 ，一个用户详情类service类  一个数据源
@Configuration
@EnableAuthorizationServer // 开启OAuth2.0授权服务机制
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private DataSource dataSource;

    //配置客户端的详情信息
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
     /*   clients.inMemory()
               .withClient("nc_client_app").secret(passwordEncoder.encode("123456"))
               .scopes("all").redirectUris("http://localhost").authorizedGrantTypes("authorization_code","password")
               .and()
               .withClient("nc_client_pc").secret(passwordEncoder.encode("123456"))
               .scopes("all").redirectUris("http://localhost").authorizedGrantTypes("authorization_code","password");*/
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenServices(tokenServices())  // 获取token使用令牌存储以及自定义客户端信息
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); // 获取token的请求只支持post请求
    }

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenStore(tokenStore);

        // 【添加】
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter);

        // 【---添加---】
        tokenServices.setSupportRefreshToken(true); //支持刷新token

        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll") //tokenkey，当使用JwtToken且使用非对称加密时，用于获取公钥而开放的，这里指这个 endpoint完全公开
                .checkTokenAccess("permitAll") //checkToken这个endpoint完全公开
                .allowFormAuthenticationForClients(); //允许表单认证(密码/授权码模式认证)
    }
	

}