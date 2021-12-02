package com.nineclock.approve.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 审批定义基础信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveDefinitionBaseDataDto implements Serializable {

    private String id;//ID

    private String groupType; // 审批类型

    private String name; // 审批名称

    private List<AllowUserObjDto> allowUserJson; // 谁可以发起这个审批json

    private String description; // 审批说明

    private String icon; // 图标

    private String opinionPrompt; // 审批意见填写提示

    private String opinionRequired; // 审批意见是否必填

    private Integer seq; // 序号

}
