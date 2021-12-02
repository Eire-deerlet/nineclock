package com.nineclock.system.controller;

import com.nineclock.common.entity.Result;
import com.nineclock.system.service.SmsService;
import com.nineclock.system.service.SysCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发送短信控制器
 */
@RestController
@RequestMapping("/sms")
@Api(value = "发送短信接口", tags = "系统服务")
public class SmsController {
    @Autowired
    private SmsService smsService;

    /**
     * 短信: 发送短信验证码
     * GET/sys/sms/code
     */
    @ApiOperation("短信: 发送短信验证码")
    @GetMapping("/code")
    public Result sendSms(String mobile, String type){
        smsService.sendSms(mobile, type);
        return Result.success();
    }
    /**
     * 短信: 验证码校验
     * 接口路径：GET/sys/sms/verify
     */
    @ApiOperation("短信: 验证码校验")
    @GetMapping("verify")
    public Result<Boolean> verify(String checkcode ,String mobile , String type){
        Boolean flag=smsService.verify(checkcode,mobile,type);
        return Result.success(flag);
    }




}
