package com.nineclock.system.feign;

import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.dto.SysUserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("sys-service")
public interface SysUserFeign {
    //微服务之间的调用
    @GetMapping("/user/query")
    public Result<SysUserDTO> queryUser(@RequestParam String username);

    /**
     * 根据员工ID查询员工信息
     * @param companyUserId
     * @return
     */
    @GetMapping("/companyUser/query/{companyUserId}")
    public Result<SysCompanyUserDTO> queryCompanyUserById(@PathVariable Long companyUserId);


    //查询全部的企业员工
    @GetMapping("/companyUser/queryAllUser")
    public Result<List<SysCompanyUserDTO>> queryAllCompanyUser();
}
