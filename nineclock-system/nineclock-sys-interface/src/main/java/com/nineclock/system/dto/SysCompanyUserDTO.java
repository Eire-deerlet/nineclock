package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nineclock.common.entity.NcAuthority;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     企业与用户关联 -- 员工DTO
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysCompanyUserDTO implements Serializable {


    private Long id; //员工ID

    private Long userId; //用户id

    private Long companyId; //企业id

    private String companyName; //公司名称

    private Long departmentId; //部门id

    private String departmentName; //部门名称

    private String post;  //岗位

    private String workNumber; //工号

    private String email; //邮箱

    private String tel; //固定电话

    private String officeAddress; //办公地点

    private Date timeEntry; //入职时间

    private String remark; //备注

    private String mobile; //手机号

    private String userName; //用户名

    private String imageUrl; //注册上传图片地址
	
	
    private List<NcAuthority> grantedAuthorities; //当前员工包含的权限（角色，权限）

    private List<SysRoleDTO> roles;  //用户角色
    private List<SysFunctionDTO> functions; //用户权限

    //员工所属的部门
    //private SysDepartmentDTO department;


    /**
     * 封装当前用户所有的角色 以及 权限
     */
    public List<NcAuthority> getGrantedAuthorities() {
        grantedAuthorities = new ArrayList<>();
        if(!CollectionUtils.isEmpty(roles)){
            grantedAuthorities.addAll(roles);
        }
        if(!CollectionUtils.isEmpty(functions)){
            grantedAuthorities.addAll(functions);
        }
        return grantedAuthorities;
    }
}
