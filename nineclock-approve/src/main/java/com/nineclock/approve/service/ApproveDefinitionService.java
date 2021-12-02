package com.nineclock.approve.service;

import com.nineclock.approve.dto.ApproveDefinitionDto;
import com.nineclock.approve.dto.ApproveGroupDefinitionDto;

import java.util.List;

public interface ApproveDefinitionService {

    /**
     * 流程定义: 查询列表
     */
    List<ApproveGroupDefinitionDto> queryApproveGroupDefinitio();
    /**
     * 流程定义: 新增/修改
     */
    void saveApproveDefinition(ApproveDefinitionDto approveDefinitionDto);
}
