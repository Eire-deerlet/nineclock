package com.nineclock.system;

import com.nineclock.common.oss.OssClientUtils;
import com.nineclock.common.sms.AliyunSmsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OssTest {

    @Autowired
    private OssClientUtils ossClientUtils;


    @Test
    public void testUpload() throws Exception {
        String url = ossClientUtils.uploadFile("呆英.jpg", new FileInputStream("D:\\abc\\DDDDY.jpg"));
        System.out.println(url);
    }
}