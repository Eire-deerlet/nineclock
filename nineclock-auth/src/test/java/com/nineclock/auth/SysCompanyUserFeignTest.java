package com.nineclock.auth;

import com.nineclock.common.entity.Result;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.feign.SysCompanyUserFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysCompanyUserFeignTest {
    @Autowired
    private SysCompanyUserFeign sysCompanyUserFeign;

    /**
     * 测试根据用户名密码查询用户feign接口
     */
    @Test
    public void testQueryCompanyUser(){
        Result<List<SysCompanyUserDTO>> result = sysCompanyUserFeign.queryCompanyUserDTO(30L, null);
        System.out.println(result.getData());
    }

}
