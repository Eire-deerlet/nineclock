package com.nineclock.approve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 保存审批定义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ApproveDefinitionDto implements Serializable {

    private ApproveDefinitionBaseDataDto baseData; //基础信息

    private List<ApproveDefinitionTableDataDto> tableData; //表单信息

    private String flowData; //流程信息

}
