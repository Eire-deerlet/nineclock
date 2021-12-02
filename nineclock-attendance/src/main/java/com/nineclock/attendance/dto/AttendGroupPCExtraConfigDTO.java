package com.nineclock.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 必须考勤日期 或 无需考勤日期
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendGroupPCExtraConfigDTO implements Serializable {

    //设置日期
    private String setDate;

    //是否需要打卡：1为必须     0为无需
    private Integer requiredAttend;

}