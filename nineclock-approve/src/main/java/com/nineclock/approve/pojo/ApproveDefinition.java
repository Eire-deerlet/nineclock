package com.nineclock.approve.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;


/**
 * 审批定义
 */
@Data
@Accessors(chain = true)
@TableName("approve_definition")
public class ApproveDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // id

    private String companyId; // 企业ID

    private String groupType; // 审批类型 -(1:出勤休假, 2:财务, 3:人事, 4:行政, 5:其他)

    private String name; // 审批名称

    private Integer seq; // 序号

    private String icon; // 图标

    private String description; // 审批说明

    private String opinionPrompt; // 审批意见填写提示

    private String opinionRequired; // 审批意见是否必填

    private String allowUserJson; // 谁可以发起这个审批json

    private String formJson; // 表单json

    private String flowJson; // 流程json

    private String tableName; // 提交表单表名

    private String columns; // 提交表单字段名

    private String isValid; // 有效标记

    private Date createTime; // 创建时间

    private Date updateTime; // 最后更新时间

    private String dataFrom; // 数据来源

    private String templateId; // 模板ID

}
