package com.nineclock.approve.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 审批定义表单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveDefinitionTableDataDto implements Serializable {

    //字段主键
    private String fieldKey;

    //组件名称
    private String lab;

    //组件类型
    private String type;

    //组件图标
    private String icon;

    //标题
    private String title;

    //组件数据设置
    private String data;

    //是否源于模板
    private String fromTemplate = "0";
}
