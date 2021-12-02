package com.nineclock.auth;

import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.feign.SysUserFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserFeignTest {



    @Autowired
    private SysUserFeign sysUserFeign;

    /**
     * 测试根据用户名密码查询用户feign接口
     */
    @Test
    public void testQueryUser(){
        Result<SysUserDTO> result = sysUserFeign.queryUser("小明");
        System.out.println(result.getData());
    }
}
