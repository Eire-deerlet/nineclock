package com.nineclock.auth.intergration.processor;

import com.nineclock.auth.intergration.entity.IntergrationAuthenticationEntity;
import com.nineclock.common.constant.SMSConstant;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.feign.SysUserFeign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 手机验证码认证处理器
 */
@Component
public class SmsAuthenticationProcessor implements AuthenticationProcessor {

    @Autowired
    private SysUserFeign sysUserFeign;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //导入redis
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUserDTO authenticate(IntergrationAuthenticationEntity entity) {
        // 获取手机号及验证码，进行判断
        String mobile = entity.getAuthParameter("mobile"); // 手机号
        String password = entity.getAuthParameter("password"); // 验证码

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }

        // TODO 校验验证码，后续从redis取出验证码进行对比，现在假设redis中的验证码是1234
                                        //登录 +手机号
        String redisCode = redisTemplate.opsForValue().get(SMSConstant.SMS_LOGIN_KEY_PREFIX + mobile);
        //redis == password   redisCode!= null  password是验证码
        if (redisCode.equals(password)&&!StringUtils.isEmpty(redisCode)) {
            // 调用系统微服务，根据手机号获取用户信息
            SysUserDTO sysUserDTO = sysUserFeign.queryUser(mobile).getData();
            if (sysUserDTO == null) {
                throw new NcException(ResponseEnum.INVALID_USERNAME_PASSWORD);
            }
            // 由于传进来的password是验证码，而sysUserDTO的password是数据库查询得到的密码
            // 因此此处把传进来的密码进行加密后设置进去，后续security框架在进行密码匹配就能够匹配成功
            sysUserDTO.setPassword(passwordEncoder.encode(password));
            return sysUserDTO;

        } else {
            // 抛出异常，验证码错误
            throw new NcException(ResponseEnum.INVALID_VERIFY_CODE);
        }
    }

    @Override
    public boolean support(IntergrationAuthenticationEntity entity) {
        // 如果authType=sms，由该处理器进行处理
        if (entity != null && entity.getAuthType() != null && entity.getAuthType().equals("sms")) {
            return true;
        }
        return false;
    }
}