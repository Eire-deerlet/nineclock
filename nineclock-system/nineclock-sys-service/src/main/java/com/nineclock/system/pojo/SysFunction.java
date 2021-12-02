package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统功能表
 * </p>
 */
@Data
@TableName("sys_function")
public class SysFunction implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //主键
    private String name; //权限名称
    private String title; //菜单名称
    private String icon; //icon路径
    private Integer orderNum; //排序，最小为0
    private String path; //匹配路径
    private Short status; //使用状态，1为可用 ; 0为不可用
    private String color; //主体颜色
    private Long companyId; //企业ID

}
