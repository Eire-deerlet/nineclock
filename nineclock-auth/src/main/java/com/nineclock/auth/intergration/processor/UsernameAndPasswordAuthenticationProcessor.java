package com.nineclock.auth.intergration.processor;

import com.nineclock.auth.intergration.entity.IntergrationAuthenticationEntity;

import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.feign.SysUserFeign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户名密码认证
 */
@Component
public class UsernameAndPasswordAuthenticationProcessor implements AuthenticationProcessor {

    @Autowired
    private SysUserFeign sysUserFeign;

    @Override
    public SysUserDTO authenticate(IntergrationAuthenticationEntity entity) {
        // 获取用户名/密码，进行判断
        String username = entity.getAuthParameter("username");
        String password = entity.getAuthParameter("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }

        // 调用系统微服务获取用户信息
        SysUserDTO sysUserDTO = sysUserFeign.queryUser(username).getData();

        if (sysUserDTO == null) {
            throw new NcException(ResponseEnum.INVALID_USERNAME_PASSWORD);
        }

        return sysUserDTO;
    }

    @Override
    public boolean support(IntergrationAuthenticationEntity entity) {
        // authType为空，由该处理器进行处理
        if (entity != null && StringUtils.isEmpty(entity.getAuthType())) {
            return true;
        }
        return false;
    }
}
