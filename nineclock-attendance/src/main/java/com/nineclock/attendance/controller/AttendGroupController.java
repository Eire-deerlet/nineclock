package com.nineclock.attendance.controller;

import com.nineclock.attendance.dto.AttendGroupDTO;
import com.nineclock.attendance.service.AttendGroupService;
import com.nineclock.common.entity.PageResult;
import com.nineclock.common.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Api(value = "考勤组管理接口", tags = "考勤服务")
public class AttendGroupController {

    @Autowired
    private AttendGroupService attendGroupService;

    /**
     * 考勤组: 分页查询
     * 接口路径：GET/atte/attendGroup
     */
    @ApiOperation("考勤组: 分页查询")
    @GetMapping("attendGroup")
    public Result<PageResult<AttendGroupDTO>> queryAttendGroupPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageResult<AttendGroupDTO> pageResult = attendGroupService.queryAttendGroupPage(page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 考勤组: 添加
     * 接口路径：POST/atte/attendGroup
     */
    @ApiOperation("考勤组: 添加")
    @PostMapping("/attendGroup")
    public Result addAttendGroup(@RequestBody AttendGroupDTO attendGroupDTO) {
        attendGroupService.addAttendGroup(attendGroupDTO);
        return Result.success();
    }
    /**
     * 考勤组: 获取当前登录用户考勤组
     * 接口路径：GET/atte/getAttendGroupByUserId
     */
    @ApiOperation("考勤组: 获取当前登录用户考勤组")
    @GetMapping("getAttendGroupByUserId")
    public Result<AttendGroupDTO> getAttendGroupByUserId(){
        AttendGroupDTO attendGroupDTO= attendGroupService.getAttendGroupByUserId();
        return Result.success(attendGroupDTO);
    }
}