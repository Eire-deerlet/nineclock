package com.nineclock.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//企业用户部门表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentOrUserSimpleDTO {
    //部门或用户id
    private Long id;
    //部门或用户名称
    private String name;
    //对象类型：0为公司 1为部门，2为用户
    private Integer type;
}