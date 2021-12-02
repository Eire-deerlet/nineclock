package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nineclock.common.entity.NcAuthority;
import org.springframework.security.core.GrantedAuthority;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *     系统功能权限DTO
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysFunctionDTO   extends NcAuthority implements Serializable {

    private Long id; //权限ID

    private String name; //权限名称

    private String title; //菜单名称

    private String icon; //icon路径

    private Integer orderNum; //排序，最小为0

    private String path; //匹配路径

    private Short status; //使用状态，1为可用0为不可用

    private String color; //颜色

    private Long companyId; //企业ID

    @Override
    public String getAuthority() {
        return name;
    }
}
