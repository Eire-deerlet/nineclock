package com.nineclock.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 添加考勤组请求中的参与或不参与模块
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendGroupPartDTO implements Serializable {

    //用户或部门id
    private Long objectId;

    //类型 1为部门 2为用户
    private Integer objectType;

    //所选对象名称 部门名称 或者 用户名称
    private String objectName;

}