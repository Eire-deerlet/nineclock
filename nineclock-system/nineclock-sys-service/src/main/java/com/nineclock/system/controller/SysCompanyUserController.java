package com.nineclock.system.controller;

import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.service.SysCompanyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业员工管理接口控制器
 */
@RestController
@Api(value = "企业员工管理接口", tags = "系统服务", description = "企业员工管理接口")
@RequestMapping("companyUser")
public class SysCompanyUserController {

    @Autowired
    private SysCompanyUserService companyUserService;

    /**
     * 员工管理: 根据企业/用户ID查询员工列表
     * 接口路径：GET/sys/companyUser/query
     */
    @ApiOperation(value = "根据系统用户ID、企业id查询员工列表")
    @GetMapping("query")
    public Result<List<SysCompanyUserDTO>> queryCompanyUserDTO(@RequestParam(required = false) Long userId,
                                                               //企业id可以不传，用户id必须传
                                                               @RequestParam(required = false) Long companyId) {
        List<SysCompanyUserDTO> companyUserDTOList = companyUserService.queryCompanyUserDTO(userId, companyId);
        return Result.success(companyUserDTOList);
    }

    /**
     * 文档第一个接口
     * 根据员工ID查询企业员工信息
     */
    @ApiOperation(value = "根据员工ID查询企业员工信息")
    @GetMapping("/query/{companyUserId}")
    public Result<SysCompanyUserDTO> queryCompanyUserById(@PathVariable Long companyUserId) {
        SysCompanyUserDTO companyUserDTO = companyUserService.queryCompanyUserById(companyUserId);
        return Result.success(companyUserDTO);
    }

    /**
     * 查询当前企业员工列表
     * 接口路径：GET/sys/companyUser/queryAllUser
     *
     * @return
     */
    @ApiOperation(value = "查询当前企业所有的员工")
    @GetMapping("/queryAllUser")
    public Result<List<SysCompanyUserDTO>> queryAllCompanyUser() {
        List<SysCompanyUserDTO> sysCompanyUserDTOList = companyUserService.queryAllCompanyUser();
        return Result.success(sysCompanyUserDTOList);
    }

    /**
     * 组织架构:APP-根据手机号获取员工信息
     * 接口路径：GET/sys/companyUser/queryone
     */
    @ApiOperation("组织架构:APP-根据手机号获取员工信息")
    @GetMapping("/queryone")
    public Result<SysCompanyUserDTO> findCompanyUserByMobile(String mobile) {
        SysCompanyUserDTO companyUserDTO = companyUserService.findCompanyUserByMobile(mobile);
        return Result.success(companyUserDTO);
    }

    /**
     * 根据企业ID查询企业的主管理员
     */
    @ApiOperation(value = "根据企业ID查询企业的主管理员")
    @GetMapping("/queryAdminByCompanyId")
    public Result<SysCompanyUserDTO> queryAdminByCompanyId(@RequestParam Long companyId) {
        SysCompanyUserDTO companyUserDTO = companyUserService.queryAdminByCompanyId(companyId);
        return Result.success(companyUserDTO);
    }
    /**
     * 查询某企业的员工列表
     */
    @ApiOperation(value="查询某企业的员工列表")
    @GetMapping("/queryAllUserByCompanyId")
    public Result<List<SysCompanyUserDTO>> queryAllUserByCompanyId(@RequestParam Long companyId){
        List<SysCompanyUserDTO> companyUserDTOList = companyUserService.queryAllUserByCompanyId(companyId);
        return Result.success(companyUserDTOList);
    }
}
