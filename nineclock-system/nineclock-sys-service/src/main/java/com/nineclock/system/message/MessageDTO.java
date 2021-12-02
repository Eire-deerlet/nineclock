package com.nineclock.system.message;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
//消息dto
@Data
public class MessageDTO implements Serializable {

    private String messageType; //消息类型
    private String companyId; //企业id
    private String title; //消息标题
    private String content; //消息摘要
    private Integer useStatus; //消息使用状态 已读/未读
    private String audience; //消息作用域 广播 or 单点
    private List<String> targets; //消息发送目标人ids
    private Integer approveStatue = 0; //审批状态 0未审核 1已审核
    private String approveResult; //审核结果 0 拒绝请求 1：审核通过
    private Long applyUserId; //申请人ID
    private String applyUsername; //申请人用户名称
    private Long approveUserId; //审批人用户ID
    private String approveUsername; //审批人用户名称
}