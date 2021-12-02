package com.nineclock.attendance.controller;

import com.nineclock.attendance.dto.AttendPunchDTO;
import com.nineclock.attendance.dto.AttendPunchUserWholeDayDTO;
import com.nineclock.attendance.service.AttendPunchService;
import com.nineclock.common.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api(value = "考勤打卡接口管理", tags = "考勤服务")
@RestController

public class AttendPunchController {

    @Autowired
    private AttendPunchService attendPunchService;

    /**
     * 考勤: 移动端打卡
     * 接口路径：POST/atte/punch
     */
    @ApiOperation(value = "打卡: 移动端打卡")
    @PostMapping("/punch")
    public Result punch(@RequestBody AttendPunchDTO attendPunchDTO) {
        attendPunchService.punch(attendPunchDTO);
        return Result.success();
    }

    /**
     * 考勤: 查询打卡数据
     * 接口路径：GET/atte/get
     */
    @ApiOperation("考勤: 查询打卡数据")
    @GetMapping("/get")
    public Result<AttendPunchUserWholeDayDTO> queryPunchRecord() {
        AttendPunchUserWholeDayDTO attendPunchUserWholeDayDTO = attendPunchService.queryPunchRecord();
        return Result.success(attendPunchUserWholeDayDTO);

    }

    /**
     * 考勤: 查询考勤列表数据
     * 接口路径：GET/atte/members
     */
    @GetMapping("members")
    @ApiOperation("考勤: 查询考勤列表数据")
    public Result<List<AttendPunchDTO>> queryMembers(@RequestParam String startTime,
                                                     @RequestParam String endTime) {
        List<AttendPunchDTO> attendPunchDTOList = attendPunchService.queryMembers(startTime, endTime);
        return Result.success(attendPunchDTOList);
    }

    /**
     * 考勤: 导出考勤列表数据创
     * 接口路径：GET/atte/export
     */
    @ApiOperation("考勤: 导出考勤列表数据创")
    @GetMapping("export")
    public void exportAttendData(String startTime, String endTime) throws IOException {
        attendPunchService.exportAttendData(startTime, endTime);


    }
}
