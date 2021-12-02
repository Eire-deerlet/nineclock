package com.nineclock.common.enums;

import lombok.Getter;

/**
 * 响应状态码以及提示消息的枚举类型
 *
 */
@Getter
public enum ResponseEnum {

    PASSWORD_NOT_NULL(4001,  "密码不能为空！"),
    OK(200,  "操作成功！"),
    SUCCESS(200, "操作成功！"),
    ERROR(500, "操作失败！"),

    INVALID_FILE_TYPE(400, "无效的文件类型！"),
    INVALID_PARAM_ERROR(400, "无效的请求参数！"),
    INVALID_PHONE_NUMBER(400, "无效的手机号码"),
    INVALID_VERIFY_CODE(400, "验证码错误！"),
    INVALID_USERNAME_PASSWORD(400, "无效的用户名和密码！"),
    INVALID_SERVER_ID_SECRET(400, "无效的服务id和密钥！"),
    INVALID_NOTIFY_PARAM(400, "回调参数有误！"),
    INVALID_NOTIFY_SIGN(400, "回调签名有误！"),

    DATA_TRANSFER_ERROR(500, "数据转换异常！"),
    INSERT_OPERATION_FAIL(500, "新增操作失败！"),
    UPDATE_OPERATION_FAIL(500, "更新操作失败！"),
    DELETE_OPERATION_FAIL(500, "删除操作失败！"),
    FILE_UPLOAD_ERROR(500, "文件上传失败！"),
    DIRECTORY_WRITER_ERROR(500, "目录写入失败！"),
    FILE_WRITER_ERROR(500, "文件写入失败！"),
    FILE_NOT_FOUND(500, "文件未找到！"),
    FILE_WRITE_NOT_PERMIT(500, "您没有权限操作此文件"),
    UPLOAD_FILE_NOT_FOUND(500, "上传文件未找到！"),
    FILE_SIZE_EXCEED_MAX_LIMIT(500, "上传文件大小超出限制！"),
    SEND_MESSAGE_ERROR(500, "短信发送失败！"),
    CODE_IMAGE_ERROR(500, "验证码错误！"),
    DATA_NOT_EXISTS(500,"操作的数据不存在"),

    USER_MOBILE_EXISTS(500, "该手机号已经被注册！"),
    USER_MOBILE_EMAIL_EXISTS(500, "该手机号或邮箱已经被注册！"),
    USER_NOT_REGISTER(500, "当前用户还未进行注册！"),
    USER_NOT_JOIN_COMPANY(500, "请加入企业后再使用该功能！"),
    USER_NOT_COMPANY_ADMIN(500, "您不是企业管理员！"),
    USER_NOT_MATCH_ATTENDGROUP(500, "未查询到用户考勤组,请先配置！"),
    USER_NOT_FOUND(500, "没有查询到用户！"),

    COMPANY_ADMIN_NOT_EXISTS(500, "没有找到对应企业管理员！"),
    COMPANY_NOT_FOUND(500, "企业不存在！"),
    WROK_NUM_EXISTS(500, "当前工号已经存在！"),

    PUNCH_INVALID_AREA(500, "打卡地点不在考勤点范围内！"),
    PUNCH_INVALID_DAY(500, "非工作日无需打卡！"),
    PUNCH_ALREADY(500, "已经打卡，无需重复打卡！"),
    SIGN_DATA_NULL(404, "未检索到签到数据！"),

    MESSAGE_PARAMS_LOST(500, "消息参数缺失！"),
    SERVER_ERROR(500,"对不起,系统繁忙,请稍后重试"),
    NO_PERMISSION_WITH_APPROVE(500,"您无权发起此审批流程"),

    UNAUTHORIZED(401, "登录失效或未登录！"),
    FOBIDDEN(403, "禁止访问！"),
    SIGNDATA_NOT_FOUND(500, "当前检索条件没有签到数据！"),

    ROLE_NOT_FOUND(403, "角色列表不存在！"),
    SYS_PERMISSSION_NOT_FOUND(403, "当前企业无权限");

    //最上方提供各种情况

    //提供私有构造
    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //提供属性  get方法
    private Integer code;
    private String message;

}
