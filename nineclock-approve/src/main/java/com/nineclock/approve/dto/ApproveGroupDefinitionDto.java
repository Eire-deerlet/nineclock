package com.nineclock.approve.dto;

import com.nineclock.approve.pojo.ApproveDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * 审批流程返回数据对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApproveGroupDefinitionDto implements Serializable {

    private String groupType; //分类 -- 考勤相关的所有流程

    private List<ApproveDefinition> definitionList; ////审批定义  -- 所有考勤相关的审批流程

}
