package com.nineclock.approve.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 审批编号控制
 */
@Data
@Accessors(chain = true)
@TableName("approve_inst_no_ctrl")
public class ApproveInstNoCtrl implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // id

    private String companyId; // 企业ID

    private String definitionId; // 审批定义ID

    private String definitionName; // 审批定义名称

    private String definitionNameShort; // 审批定义名称拼音

    private String genDay; // 产生序号日期

    private Integer currSeq; // 当前序号

    private String isValid; // 有效标记

    private Date createTime; // 创建时间

    private Date updateTime; // 最后更新时间

}
