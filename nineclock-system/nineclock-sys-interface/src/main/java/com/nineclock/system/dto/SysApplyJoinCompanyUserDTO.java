package com.nineclock.system.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysApplyJoinCompanyUserDTO implements Serializable {

    private Long userId; //用户ID
    private String userName; //用户名
    private Long companyId; //企业ID
    private String companyName; //企业名称
    private String email; //邮箱
    private String mobile; //手机号
    private String post; //职位
    private String applyReason; //申请原因
    private String imageUrl; //图像URL

}