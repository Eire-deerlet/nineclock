package com.nineclock.system.service;

import com.nineclock.system.dto.SysCompanyContactConfigDTO;

import java.util.List;

public interface SysCompanyContactConfigService {

    /**
     * 添加字段
     * @param sysCompanyContactConfigDTO
     */
    void addCompanyContact(SysCompanyContactConfigDTO sysCompanyContactConfigDTO);

    /**
     * 企业通讯录: 查询并初始化
     * @return
     */
    List<SysCompanyContactConfigDTO> queryCompanyContactConfig();
    /**
     * 企业通讯录: 修改状态
     */
    void updateCompanyContactConfigStatus(Long id, Short status);

    /**
     * 企业通讯录: 删除
     * @param id
     */
    void deleteCompanyContactConfig(Long id);
}
