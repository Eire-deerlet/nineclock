package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
//实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 如果对象中有属性为空，该属性不会序列化
@ApiModel
public class SysCompanyContactConfigDTO implements Serializable {
    @ApiModelProperty(value = "主键id")
    private Long id; //主键

    @ApiModelProperty(value = "字段名称")
    private String name; //字段名称

    @ApiModelProperty(value = "fixed-固定字段(不可删除)，dynamic-动态字段(可删除)")
    private String type; //类型：fixed-固定字段(不可删除)，dynamic-动态字段(可删除)

    @ApiModelProperty(value = "字段类型：Integer，String")
    private String fieldType; //字段类型：Integer，String

    @ApiModelProperty(value = "状态：1-可用 0-不可用")
    private Short status; //状态：1-可用 0-不可用

    @ApiModelProperty(value = "//企业ID")
    private Long companyId; //企业ID


}