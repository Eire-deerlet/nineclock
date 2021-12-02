package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 角色权限/菜单关系表 
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_function")
public class SysRoleFunction implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //主键
    private Long roleId; //角色id
    private Long functionId; //权限ID

}
