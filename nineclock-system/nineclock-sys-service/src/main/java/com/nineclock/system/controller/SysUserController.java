package com.nineclock.system.controller;


import com.nineclock.common.entity.Result;

import com.nineclock.system.dto.SysRegisterDTO;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(value = "用户管理接口",tags = "系统微服务")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     *根据用户名 、 手机号查询用户
     */
    @GetMapping("/query")
    @ApiOperation("根据用户名 、 手机号查询用户")        //是否必须 是
    public Result<SysUserDTO> queryUser(@RequestParam String username){
        SysUserDTO userDTO = sysUserService.queryUser(username);
        return Result.success(userDTO);
    }
    //判断权限
    @GetMapping("hello")
    @PreAuthorize("hasRole('ADMIN')")
    public Result hello(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);

        String nameStr = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(nameStr);
        return Result.success("访问hello");
    }
    //判断权限
    @GetMapping("say")
    @PreAuthorize("hasRole('SELLER')")
    public Result say(){
        return Result.success("访问say");
    }

    /**
     * 用户注册: 注册用户
     * 接口路径：POST/sys/user/register
     */
    @ApiOperation("用户注册: 注册用户")
    @PostMapping("register")
    public Result register(SysRegisterDTO registerDTO){
         sysUserService.register(registerDTO);
        return Result.success();
    }



}
