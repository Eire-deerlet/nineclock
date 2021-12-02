package com.nineclock.system.controller;

import com.nineclock.common.entity.Result;
import com.nineclock.system.service.SysCompanyContactConfigService;
import com.nineclock.system.dto.SysCompanyContactConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companyContact")
@Api(value = "企业通讯录字段配置管理", tags = "企业通讯录", description = "企业通讯录字段配置管理")
public class SysCompanyContactConfigController {

    @Autowired
    private SysCompanyContactConfigService configService;
    /** 添加数据
     * 请求方式POST
     * URL/companyContact/config
     * 请求参数json ---> SysCompanyContactConfigDTO
     * 响应数据Result
     */
    @PostMapping("/config")
    @ApiOperation("添加数据")
    public Result addCompanyContactConfig(@RequestBody SysCompanyContactConfigDTO configDTO){
        configService.addCompanyContact(configDTO);
        return Result.success();
    }

    /**
     * 企业通讯录: 查询并初始化
     * @return
     * GET/sys/companyContact/config
     */
    @GetMapping("/config")
    @ApiOperation("企业通讯录: 查询并初始化")
    public Result<List<SysCompanyContactConfigDTO>> queryCompanyContactConfig(){
        List<SysCompanyContactConfigDTO>  configDTOS = configService.queryCompanyContactConfig();
        return Result.success(configDTOS);
    }
    /**
     * 企业通讯录: 修改状态
     * PUT/sys/companyContact/config/{id}/{status}
     */
    @PutMapping("/config/{id}/{status}")
    @ApiOperation("企业通讯录: 修改状态")
    public Result updateCompanyContactConfigStatus(@PathVariable("id") Long id ,@PathVariable("status") Short status){
        configService.updateCompanyContactConfigStatus(id,status);
        //没有返回值
        return Result.success();
    }

    /**
     * 企业通讯录: 删除
     * DELETE/sys/companyContact/config/{id}
     */
    @DeleteMapping("/config/{id}")
    @ApiOperation("企业通讯录: 删除")
    public Result deleteCompanyContactConfig(@PathVariable("id") Long id){
        configService.deleteCompanyContactConfig(id);
        //没有返回值
        return Result.success();
    }

}
