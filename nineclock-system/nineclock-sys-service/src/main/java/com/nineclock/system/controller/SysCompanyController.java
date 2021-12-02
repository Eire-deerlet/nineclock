package com.nineclock.system.controller;

import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.SysAllowedJoinCompanyUserDTO;
import com.nineclock.system.dto.SysApplyJoinCompanyUserDTO;
import com.nineclock.system.dto.SysCompanyDTO;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.service.SysCompanyService;
import com.nineclock.system.service.SysCompanyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 企业管理接口控制器
 */
@RestController
@Api(value = "企业管理接口", tags = "系统服务", description = "企业管理接口")
@RequestMapping("company")
public class SysCompanyController {

    @Autowired
    private SysCompanyService companyService;

    @Autowired
    private SysCompanyUserService companyUserService;

    /**
     * 企业管理: 根据用户ID查询关联的企业列表
     * 接口路径：GET/sys/company/list/{userId}
     */
    @ApiOperation(value = "根据用户ID查询关联的企业列表")
    @GetMapping("list/{userId}")
    public Result<List<SysCompanyDTO>> queryUserJoinCompany(@PathVariable Long userId) {
        List<SysCompanyDTO> companyDTOList = companyService.findCompanyByUserId(userId);
        return Result.success(companyDTOList);
    }
    /**
     * 企业管理: 查询当前企业信息
     * 接口路径：GET/sys/company
     */
    @ApiOperation("查询当前企业信息")
    @GetMapping
    public Result<SysCompanyDTO> queryCompany(){
        SysCompanyDTO companyDTO = companyService.queryCompanyInfo();
                return Result.success(companyDTO);
    }

    /**
     * 企业管理: 上传图片
     * 接口路径：POST/sys/company/uploadOSS
     */
    @ApiOperation("企业管理: 上传图片")
    @PostMapping("uploadOSS")
    public Result<String> uploadOSS(MultipartFile file){
        String flag=  companyService.uploadOSS(file);
        return Result.success(flag);
    }

    /**
     * 企业管理: 更新当前企业的基本信息
     * 接口路径：PUT/sys/company
     */
    @ApiOperation("企业管理: 更新当前企业的基本信息")
    @PutMapping                       //SysCompanyDTO 包含logo和企业全称
    public Result updateCompanyInfo(@RequestBody SysCompanyDTO sysCompanyDTO) {
        companyService.updateCompanyInfo(sysCompanyDTO);
        return Result.success();
    }
    /**
     * 企业管理: 获取企业当前主管理员
     * 接口路径：GET/sys/company/getCurrentAdmin
     */
    @ApiOperation("企业管理: 获取企业当前主管理员")
    @GetMapping("/getCurrentAdmin")
    public Result<SysCompanyUserDTO> getCurrentAdmin(){
        SysCompanyUserDTO companyUserDTO = companyUserService.getCurrentAdmin();

        return Result.success(companyUserDTO);
    }
    /**
     * 企业管理: 更改系统管理员
     * 接口路径：POST/sys/company/config/admin
     * 操控 SysCompanyUserRoleMapper 表
     */
    @ApiOperation("企业管理: 更改系统管理员")
    @PostMapping("/config/admin")
    public Result updateAdmin(@RequestBody Map<String, Object> map){
        String code = (String) map.get("code"); // 验证码
        Long userId = Long.valueOf(map.get("userId").toString());// 员工id
        companyService.changeSysAdmin(code, userId);
        return Result.success();
    }

    /**
     * 根据关键字名称查询企业
     * 接口路径：GET/sys/company/list
     */
    @ApiOperation("根据关键字名称查询企业")
    @GetMapping("/list")
    public  Result<List<SysCompanyDTO>> queryCompanyByName(String keyword) {
        List<SysCompanyDTO> companyDTOList = companyService.queryCompanyByName(keyword);
        return Result.success(companyDTOList);
    }
    /**
     * 加入企业申请
     * 接口路径：POST/sys/company/applyJoinCompany
     */
    @PostMapping("/applyJoinCompany")
    @ApiOperation("加入企业申请")
    public Result applyJoinCompany(@RequestBody SysApplyJoinCompanyUserDTO applyJoinCompanyUserDTO){
        companyService.applyJoinCompany(applyJoinCompanyUserDTO);
        return Result.success();
    }
    /**
     * 加入企业审核
     * 接口路径：POST/sys/company/allowedJoinCompany
     */
    @ApiOperation("加入企业审核")
    @PostMapping("allowedJoinCompany")
    public Result allowedJoinCompany(SysAllowedJoinCompanyUserDTO sysAllowedJoinCompanyUserDTO) {
        companyService.allowedJoinCompany(sysAllowedJoinCompanyUserDTO);
        return Result.success();
    }

}