package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nineclock.common.entity.NcAuthority;
import org.springframework.security.core.GrantedAuthority;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     角色DTO
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleDTO  extends NcAuthority implements Serializable {

    private Long id; //角色ID

    private String roleName; //角色名称

    private String roleDesc; //角色描述

    private Long companyId; //企业ID

    //当前角色包含的权限
    private List<SysFunctionDTO> sysFunctionAbbrList;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
