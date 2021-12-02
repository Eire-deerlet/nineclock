package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *      企业与用户关联表 - 企业员工表
 * </p>
 */
@Data
@TableName("sys_company_user")
public class SysCompanyUser implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //主键
    private Long userId; //用户id
    private Long companyId; //企业id
    private String companyName; //公司名称
    private Long departmentId; //部门id
    private String departmentName; //部门名称
    private String post; //岗位
    private String workNumber; //工号
    private String email; //邮箱
    private String tel; //固定电话
    private String officeAddress; //办公地点
    private Date timeEntry; //入职时间
    private String remark; //备注
    private Short enable; //有效标记
    private Date createTime; //创建时间
    private Date updateTime; //更新时间
    private String mobile; //手机号
    private String userName; //用户名
    private String imageUrl; //注册上传图片地址

}
