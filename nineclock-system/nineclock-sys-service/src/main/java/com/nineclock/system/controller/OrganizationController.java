package com.nineclock.system.controller;

import com.nineclock.common.entity.PageResult;
import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.DepartmentOrUserSimpleDTO;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.dto.SysDepartmentDTO;
import com.nineclock.system.service.SysCompanyUserService;
import com.nineclock.system.service.SysDepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/organization")
@RestController
public class OrganizationController {

    @Autowired
    private SysCompanyUserService companyUserService;

    @Autowired
    private SysDepartmentService departmentService;

    /**
     * 组织架构: 查询组织所有员工
     * 接口路径：GET/sys/organization/members/simple
     */
    @ApiOperation("组织架构: 查询组织所有员工")
    @GetMapping("/members/simple")
    public Result<List<SysCompanyUserDTO>> queryCompanyMemberList() {
        List<SysCompanyUserDTO> companyUserDTOList = companyUserService.queryCompanyMemberList();
        return Result.success(companyUserDTOList);
    }

    /**
     * 组织架构: PC-获取组织架构列表(左侧菜单)
     * 接口路径：GET/sys/organization/department
     */
    @ApiOperation("组织架构: PC-获取组织架构列表(左侧菜单)")
    @GetMapping("/department")
    public Result<List<SysDepartmentDTO>> querydepartment() {
        List<SysDepartmentDTO> departmentList = departmentService.querydepartment();
        return Result.success(departmentList);
    }

    /**
     * 组织架构:PC-获取部门成员列表
     * 接口路径：GET/sys/organization/members
     */
    @ApiOperation("组织架构:PC-获取部门成员列表")
    @GetMapping("members")
    public Result<PageResult<SysCompanyUserDTO>> queryMembers(@RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "9") Integer pageSize,
                                                              @RequestParam(required = false) Long departmentId,//部门id
                                                              @RequestParam(required = false) String keyword) {  //关键字
        PageResult<SysCompanyUserDTO> pageResult = companyUserService.queryMembers(page, pageSize, departmentId, keyword);
        return Result.success(pageResult);
    }

    /**
     * 组织架构: PC-直接导入员工数据
     * 接口路径：POST/sys/organization/uploadExcel
     */
    @ApiOperation("组织架构: PC-直接导入员工数据")
    @PostMapping("uploadExcel")
    public Result uploadExcel(MultipartFile excelFile) throws IOException {
        companyUserService.uploadExcel(excelFile);
        return Result.success();
    }

    /**
     * 查询当前企业所有部门ID集合
     *
     * @return
     */
    @ApiOperation(value = "获取当前企业的所有部门ID")
    @GetMapping("/department/top")
    public Result<List<Long>> queryDepartmentIds() {
        List<Long> depIds = departmentService.queryDepartmentIds();
        return Result.success(depIds);
    }

    /**
     * 根据部门ID集合查询部门下的员工数量
     *
     * @param departmentIds
     * @return
     */
    @ApiOperation(value = "根据部门ID集合查询部门下的员工数量")
    @GetMapping("/department/membernum")
    public Result<Integer> queryUserCountByDepartmentIds(@RequestParam List<Long> departmentIds) {
        Integer count = companyUserService.queryUserCountByDepartmentIds(departmentIds);
        return Result.success(count);
    }

    /**
     * 组织架构:获取部门简单列表
     * 接口路径：GET/sys/organization/simple
     * 查询条件没有分页参数，返回结果也不是分页对象
     */
    @ApiOperation("组织架构:获取部门简单列表")
    @GetMapping("simple")
    public Result<List<DepartmentOrUserSimpleDTO>> querySimleDepartment(Long id, Integer includeMember) {
        List<DepartmentOrUserSimpleDTO> departmentOrUserSimpleDTOList = departmentService.querySimleDepartment(id, includeMember);
        return Result.success(departmentOrUserSimpleDTOList);
    }

    /**
     * 根据员工id获得从低到高级别的部门ID集合
     * @param
     * @return
     */
    @ApiOperation(value="根据员工id获得从低到高级别的部门ID集合")
    @GetMapping("/allDepId/{companyUserId}")
    public Result<List<Long>> queryDepartmentsByUserId(@PathVariable("companyUserId") Long companyUserId) {
        List<Long> idList = departmentService.queryDepartmentsByUserId(companyUserId);
        return Result.success(idList);
    }

}
