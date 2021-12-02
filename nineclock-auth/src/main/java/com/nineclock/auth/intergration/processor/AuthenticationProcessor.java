package com.nineclock.auth.intergration.processor;

import com.nineclock.auth.intergration.entity.IntergrationAuthenticationEntity;
import com.nineclock.system.dto.SysUserDTO;

//自定义认证器接口，并定义两个方法 authenticate 与 support
//其中 support 方法用于判定当前处理器是否可以处理该请求; 而authenticate方法, 则是具体的认证逻辑方法。

public interface AuthenticationProcessor {
    
    /**
     * 处理集成认证方法
     */
    public SysUserDTO authenticate(IntergrationAuthenticationEntity entity);
    
    /**
     * 判断是否支持当前集成认证类型
     * 从 IntegrationAuthenticationEntity参数中认证类型 authType 判断当前提交认证方式是哪个
     */
    public boolean support(IntergrationAuthenticationEntity entity);
}