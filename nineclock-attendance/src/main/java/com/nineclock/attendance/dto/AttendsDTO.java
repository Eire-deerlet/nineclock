package com.nineclock.attendance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 每日打卡信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendsDTO implements Serializable {

    //考勤日期 格式 YYYY/MM/DD
    private String attendDate;

    //考勤状态   通过/符号 区分上下午  正常/早退
    private String attendStatus;

}
