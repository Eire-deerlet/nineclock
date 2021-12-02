package com.nineclock.common.constant;

/**
 * 常量信息
 */
public class SMSConstant {

    /**  短信类型相关常量 **/
    public static final String SMS_LOGIN_KEY_PREFIX = "user:login:code:"; //登录

    public static final String SMS_REGISTER_KEY_PREFIX = "user:register:code:"; //注册

    public static final String SMS_RESET_KEY_PREFIX = "user:reset:code:"; //更换管理员

    public static final String SMS_CHANGE_MANAGER_KEY_PREFIX = "manager:change:code:"; //重置密码



    public static final String SMS_TYPE_LOGIN = "1"; //登录

    public static final String SMS_TYPE_REGISTER = "2"; //注册

    public static final String SMS_TYPE_CHANGE_MANAGER = "3"; //更换管理员

    public static final String SMS_TYPE_RESET_PASSWORD = "4"; //重置密码

}