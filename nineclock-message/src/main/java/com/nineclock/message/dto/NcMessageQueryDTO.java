package com.nineclock.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NcMessageQueryDTO implements Serializable {

    private String id;//ID, 主键
    private String type;//消息类型
    private Long userId;//申请用户ID
    private Long targetId;//消息处理目标用户ID  - 审批人用户ID

}