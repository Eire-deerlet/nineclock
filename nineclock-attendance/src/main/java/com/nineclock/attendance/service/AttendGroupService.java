package com.nineclock.attendance.service;

import com.nineclock.attendance.dto.AttendGroupDTO;
import com.nineclock.common.entity.PageResult;

public interface AttendGroupService {

    /**
     * 考勤组: 分页查询
     */
    PageResult<AttendGroupDTO> queryAttendGroupPage(Integer page, Integer pageSize);
    /**
     * 考勤组: 添加
     */
    void addAttendGroup(AttendGroupDTO attendGroupDTO);
    /**
     * 考勤组: 获取当前登录用户考勤组
     */
    AttendGroupDTO getAttendGroupByUserId();
}
