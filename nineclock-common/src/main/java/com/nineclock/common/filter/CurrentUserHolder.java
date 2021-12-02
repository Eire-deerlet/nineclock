package com.nineclock.common.filter;

import com.nineclock.common.entity.UserInfo;

/**
 * 存储当前登录用户的详细信息
 */
public class CurrentUserHolder {

    private static ThreadLocal<UserInfo> CURRENT_USER_LOCAL = new ThreadLocal<UserInfo>();
    /**
     * 存储
     * @param userInfo
     */
    public static void set(UserInfo userInfo){
        CURRENT_USER_LOCAL.set(userInfo);
    }
    /**
     * 获取
     * @return
     */
    public static UserInfo get(){
        return CURRENT_USER_LOCAL.get();
    }
    /**
     * 移除
     */
    public static void remove(){
        CURRENT_USER_LOCAL.remove();
    }

}