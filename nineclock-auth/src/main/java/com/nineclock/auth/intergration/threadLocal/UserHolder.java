package com.nineclock.auth.intergration.threadLocal;

import com.nineclock.common.entity.UserInfo;

/**
 * 存储认证用户详细信息
 */
public class UserHolder {
    
    private static ThreadLocal<UserInfo> USERINFO_LOCAL = new ThreadLocal<UserInfo>();
    
    /**
     * 存储
     * @param userInfo
     */
    public static void set(UserInfo userInfo){
        USERINFO_LOCAL.set(userInfo);
    }
    /**
     * 获取
     * @return
     */
    public static UserInfo get(){
        return USERINFO_LOCAL.get();
    }
    /**
     * 移除
     */
    public static void remove(){
        USERINFO_LOCAL.remove();
    }
}