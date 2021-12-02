package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysDepartmentDTO implements Serializable {
    
    private Long id;//部门id
    private String name;//部门名称
    private String label; //ElementUI tree展示的名称
    List<SysDepartmentDTO> children;//子部门列表
    
    //private SysDepartmentDTO parent;//父部门
    //private SysUserDTO manager;//主管

    //tree组件 节点名称取值 从 部门名称获取
    public String getLabel() {
        return name;
    }
}