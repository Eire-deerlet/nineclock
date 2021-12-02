package com.nineclock.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装审核加入企业的参数数据
 */
@Data
public class SysAllowedJoinCompanyUserDTO implements Serializable {

    private Long applyUserId; //申请人ID
    private Boolean approved; //是否审批通过
    private String remark; //批注
    private String notifyMsgId; //审批提醒消息ID
	
}