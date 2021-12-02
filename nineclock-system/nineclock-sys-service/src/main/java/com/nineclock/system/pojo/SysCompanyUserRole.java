package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *   员工角色关系表
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_company_user_role")
public class SysCompanyUserRole implements Serializable {

    private Long companyUserId; //员工ID
    private Long roleId; //角色ID
    private Long companyId; //企业ID

}
