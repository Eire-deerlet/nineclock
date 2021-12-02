package com.nineclock.system.service;

import com.nineclock.system.dto.SysAllowedJoinCompanyUserDTO;
import com.nineclock.system.dto.SysApplyJoinCompanyUserDTO;
import com.nineclock.system.dto.SysCompanyDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysCompanyService {


    /**
     * 查询当前登录用户的企业信息
     *
     * @return
     */
    SysCompanyDTO queryCompanyInfo();

    /**
     * 根据用户ID获取关联的企业列表
     *
     * @param userId
     * @return
     */
    List<SysCompanyDTO> findCompanyByUserId(Long userId);

    String uploadOSS(MultipartFile file);

    /**
     * 企业管理: 更新当前企业的基本信息
     * 接口路径：PUT/sys/company
     */
    void updateCompanyInfo(SysCompanyDTO sysCompanyDTO);

    /**
     * 企业管理: 更改系统管理员
     */
    void changeSysAdmin(String code, Long userId);

    /**
     * 根据关键字名称查询企业
     */
    List<SysCompanyDTO> queryCompanyByName(String keyword);

    /**
     * 加入企业申请
     */
    void applyJoinCompany(SysApplyJoinCompanyUserDTO applyJoinCompanyUserDTO);
    /**
     * 加入企业审核
     */
    void allowedJoinCompany(SysAllowedJoinCompanyUserDTO sysAllowedJoinCompanyUserDTO);
}
