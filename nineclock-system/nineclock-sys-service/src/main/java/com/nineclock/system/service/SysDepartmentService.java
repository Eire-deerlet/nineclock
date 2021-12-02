package com.nineclock.system.service;

import com.nineclock.system.dto.DepartmentOrUserSimpleDTO;
import com.nineclock.system.dto.SysDepartmentDTO;


import java.util.List;

public interface SysDepartmentService {
    /**
     * 组织架构: PC-获取组织架构列表(左侧菜单)
     */
    List<SysDepartmentDTO> querydepartment();
    /**
     * 查询当前企业所有部门ID集合
     * @return
     */
    List<Long> queryDepartmentIds();
    /**
     * 组织架构:获取部门简单列表
     */
    List<DepartmentOrUserSimpleDTO> querySimleDepartment(Long id, Integer includeMember);
    /**
     * 根据员工id获得从低到高级别的部门ID集合
     */
    List<Long> queryDepartmentsByUserId(Long companyUserId);
}
