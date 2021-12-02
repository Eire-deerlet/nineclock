package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

/**
 * <p>
 *     系统-用户DTO
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserDTO implements Serializable, UserDetails {

    private Long id; //主键ID

    private String username; //姓名

    private String password; //密码

    private String mobile; //手机号

    private String wxAccount; //微信号

    private String email; //邮箱

    private String qqAccount; //QQ

    private String color; //主体颜色

    private Short status; //状态 1激活、2未激活、3注销

    private Date createTime; //创建时间

    private Date updateTime; //更新时间

    private Long lastLoginCompanyId; //最后登录公司id



    private List<SysRoleDTO> roles;  //用户角色
    private List<SysFunctionDTO> functions;  //用户权限

	
	
	
    private List<GrantedAuthority> grantedAuthorities;

    /**
     * 封装当前用户所有的角色 以及 权限
     * @return
     */
    @Override
    public List<GrantedAuthority> getAuthorities() {
        grantedAuthorities = new ArrayList<GrantedAuthority>();
        if(!CollectionUtils.isEmpty(roles)){
            grantedAuthorities.addAll(roles);
        }
        if(!CollectionUtils.isEmpty(functions)){
            grantedAuthorities.addAll(functions);
        }
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
