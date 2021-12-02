package com.nineclock.attendance.service;

import com.nineclock.attendance.dto.AttendPunchDTO;
import com.nineclock.attendance.dto.AttendPunchUserWholeDayDTO;

import java.io.IOException;
import java.util.List;

public interface AttendPunchService {
    /**
     * 考勤: 移动端打卡
     */
    void punch(AttendPunchDTO attendPunchDTO);
    /**
     * 考勤: 查询打卡数据
     */
    AttendPunchUserWholeDayDTO queryPunchRecord();
    /**
     * 考勤: 查询考勤列表数据
     */
    List<AttendPunchDTO> queryMembers(String startTime, String endTime);
    /**
     * 考勤: 导出考勤列表数据创
     */
    void exportAttendData(String startTime, String endTime)throws IOException;

    // 2.1 生成企业的考勤报表
    String generatePunchReport(Long companyId);
}
