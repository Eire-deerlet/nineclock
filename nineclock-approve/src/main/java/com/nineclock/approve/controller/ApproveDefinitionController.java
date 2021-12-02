package com.nineclock.approve.controller;

import com.nineclock.approve.dto.ApproveDefinitionDto;
import com.nineclock.approve.dto.ApproveGroupDefinitionDto;
import com.nineclock.approve.service.ApproveDefinitionService;
import com.nineclock.common.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approve")
@Api(value = "审批流程管理", tags = "审批中心")
public class ApproveDefinitionController {
    @Autowired
    private ApproveDefinitionService approveDefinitionService;

    /**
     * 流程定义: 查询列表
     * 接口路径：GET/approve/approveGroupDefinition
     */
    @ApiOperation("流程定义: 查询列表")
    @GetMapping("approveGroupDefinition")
    public Result<List<ApproveGroupDefinitionDto>> queryApproveGroupDefinition() {
        List<ApproveGroupDefinitionDto> approveGroupDefinitionDtos = approveDefinitionService.queryApproveGroupDefinitio();
        return Result.success(approveGroupDefinitionDtos);
    }
    /**
     * 流程定义: 新增/修改
     * 接口路径：POST/approve/approveDefinition
     */
    @ApiOperation("流程定义: 新增/修改")
    @PostMapping("approveDefinition")
    public Result saveApproveDefinition(@RequestBody ApproveDefinitionDto approveDefinitionDto){
        approveDefinitionService.saveApproveDefinition(approveDefinitionDto);
        return Result.success();
    }
}
