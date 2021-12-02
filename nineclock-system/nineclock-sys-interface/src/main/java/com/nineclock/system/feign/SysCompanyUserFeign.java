package com.nineclock.system.feign;

import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.SysCompanyUserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("sys-service")
public interface SysCompanyUserFeign {

    //根据用户id，企业id查询企业员工的基本信息，企业信息，角色信息
    @GetMapping("/companyUser/query")
    public Result<List<SysCompanyUserDTO>> queryCompanyUserDTO(@RequestParam Long userId,


                                                               @RequestParam(required = false) Long companyId);

    /**
     * 文档第一个接口
     * 根据员工ID查询企业员工信息
     */
    @GetMapping("/companyUser/query/{companyUserId}")
    public Result<SysCompanyUserDTO> queryCompanyUserById(@PathVariable Long companyUserId);


    //查询全部的企业员工
    @GetMapping("/companyUser/queryAllUser")
    public Result<List<SysCompanyUserDTO>> queryAllCompanyUser();

    /**
     * 查询当前企业所有部门ID集合
     */
    @GetMapping("organization/department/top")
    public Result<List<Long>> queryDepartmentIds();

    /**
     * 根据部门ID集合查询部门下的员工数量
     */
    @GetMapping("organization/department/membernum")
    public Result<Integer> queryUserCountByDepartmentIds(@RequestParam List<Long> departmentIds);

    /**
     * 根据员工id获得部门从低到高级别的部门ID集合
     */
    @GetMapping("/organization/allDepId/{companyUserId}")
    Result<List<Long>> queryDepartmentsByUserId(@PathVariable("companyUserId") Long companyUserId);

    /**
     * 根据企业ID查询企业的主管理员
     */
    @GetMapping("/companyUser/queryAdminByCompanyId")
    public Result<SysCompanyUserDTO> queryAdminByCompanyId(@RequestParam Long companyId);

    /**
     * 查询某企业的员工列表
     */
    @GetMapping("/companyUser/queryAllUserByCompanyId")
     Result<List<SysCompanyUserDTO>> queryAllUserByCompanyId(@RequestParam Long companyId);

}
