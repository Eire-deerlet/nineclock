package com.nineclock.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {
    private Long id;
    private String username;

    // 员工信息
    private String mobile;
    private String wxAccount;
    private String email;
    private String qqAccount;
    private String color;
    private String icon;
    private Date createTime;
    private Date updateTime;
    private String post;
    private String workNumber;
    private Date timeEntry;
    private boolean enable;
    private Long departmentId;
    private String departmentName;
    private Long companyId;
    private String companyName;
    private Long companyUserId;

    //用户登录采用的客户端ID
    private String clientId;

    @JsonIgnore  //忽略转为json
    private List<NcAuthority> grantedAuthorities = Arrays.asList();
}