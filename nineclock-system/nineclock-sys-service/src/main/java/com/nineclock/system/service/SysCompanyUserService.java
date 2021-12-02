package com.nineclock.system.service;

import com.nineclock.common.entity.PageResult;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.excel.ExcelMember;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SysCompanyUserService {

    List<SysCompanyUserDTO> queryCompanyUserDTO(Long userId, Long companyId);
    /**
     * 企业管理: 获取企业当前主管理员
     * 接口路径：GET/sys/company/getCurrentAdmin
     */
    SysCompanyUserDTO getCurrentAdmin();
    /**
     * 组织架构: 查询组织所有员工
     */
    List<SysCompanyUserDTO> queryCompanyMemberList();
    /** 文档第一个接口
     * 根据员工ID查询企业员工信息
     */
    SysCompanyUserDTO queryCompanyUserById(Long companyUserId);

    /**
     * 查询当前企业员工列表
     * 接口路径：GET/sys/companyUser/queryAllUser
     * @return
     */
    List<SysCompanyUserDTO> queryAllCompanyUser();
    /**
     * 组织架构:PC-获取部门成员列表
     */
    PageResult<SysCompanyUserDTO> queryMembers(Integer page, Integer pageSize, Long departmentId, String keyword);
    /**
     * 组织架构: PC-直接导入员工数据
     */
    void uploadExcel(MultipartFile excelFile) throws IOException;
    /**
     * 处理解析完毕之后的员工数据, 进行数据组装及持久化操作
     * @param memberList
     */
    void handleParsedData(List<ExcelMember> memberList);
    /**
     * 组织架构:APP-根据手机号获取员工信息
     */
    SysCompanyUserDTO findCompanyUserByMobile(String mobile);
    /**
     * 根据部门ID集合查询部门下的员工数量
     */
    Integer queryUserCountByDepartmentIds(List<Long> departmentIds);
    /**
     * 根据企业ID查询企业的主管理员
     */
    SysCompanyUserDTO queryAdminByCompanyId(Long companyId);
    /**
     * 查询某企业的员工列表
     */
    List<SysCompanyUserDTO> queryAllUserByCompanyId(Long companyId);
}
