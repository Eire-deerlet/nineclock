package com.nineclock.system.service;

import com.nineclock.system.dto.SysRegisterDTO;
import com.nineclock.system.dto.SysUserDTO;

public interface SysUserService {
    /**
     * 根据用户名或手机号查询用户
     * @param username
     * @return
     */
    SysUserDTO queryUser(String username);

    /**
     * 用户注册: 注册用户
     * 接口路径：POST/sys/user/register
     */
    void register(SysRegisterDTO registerDTO);
}
