package com.nineclock.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendPunchUserWholeDayDTO implements Serializable {

    //上午打卡结果
    AttendPunchDTO attendPunchMorningResponse;

    // 下午打卡结果
    AttendPunchDTO attendPunchAfterNoonResponse;

} 