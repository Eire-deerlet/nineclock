package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *   部门信息
 * </p>
 */
@Data
@TableName("sys_department")
public class SysDepartment implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //ID
    private Long parentId; //上级部门ID
    private String name; //部门名称
    private Long managerId; //主管id
    private Long userId; //创建用户ID
    private Date createTime; //创建时间
    private Long companyId; //企业ID

}
