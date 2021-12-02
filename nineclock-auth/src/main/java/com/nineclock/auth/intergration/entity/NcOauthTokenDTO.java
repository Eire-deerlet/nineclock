package com.nineclock.auth.intergration.entity;

import com.nineclock.common.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NcOauthTokenDTO implements Serializable {
    private OAuth2AccessToken oauth2AccessToken; //封装申请的令牌信息
    private UserInfo user; //封装返回的用户信息
}