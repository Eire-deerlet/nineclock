package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;  //用户ID
    private String username; //用户名称
    private String password; //用户密码
    private String mobile;  //用户手机号
    private String wxAccount; //用户微信账号
    private String email;     //用户邮箱
    private String qqAccount; //用户QQ账户
    private String color;       //用户偏好UI颜色
    private Date createTime;   //创建日期
    private Date updateTime;  //更新日期
    private Short status;   //用户状态

    private Long lastLoginCompanyId;//未次登录选择企业ID
}
