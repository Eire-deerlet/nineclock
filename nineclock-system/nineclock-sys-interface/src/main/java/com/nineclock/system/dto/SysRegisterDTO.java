package com.nineclock.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装用户注册信息
 */
@Data
public class SysRegisterDTO implements Serializable {
    private String mobile;//手机号
    private String password;//密码
    private String checkcode;//验证码
}