package com.nineclock.system.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.nineclock.common.constant.SMSConstant;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.sms.AliyunSmsUtils;
import com.nineclock.common.utils.PhoneNumCheckUtils;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.service.SmsService;
import com.nineclock.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.RandomUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

@Service
@Transactional
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private AliyunSmsUtils smsUtils;
    @Autowired
    private SysUserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送短信验证码
     * @param mobile 手机号
     * @param type 业务类型：1: 登录, 2: 注册 , 3: 更换管理员 , 4: 重置密码
     */
    @Override
    public void sendSms(String mobile, String type) {
    //1.参数校验
        //1.1 参数不为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(type)){
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //1.2手机号格式验证
        if (!PhoneNumCheckUtils.isPhoneLegal(mobile)){
            throw new NcException(ResponseEnum.INVALID_PHONE_NUMBER);
        }
        //查到一个用户
        SysUserDTO sysUserDTO = userService.queryUser(mobile);

        //1.3 ，如果是注册，判断手机号有没有被注册过 SMS_REGISTER_KEY_PREFIX =
        if (type.equals(SMSConstant.SMS_TYPE_REGISTER)){ //如果是2
            if (sysUserDTO!=null){
                throw new NcException(ResponseEnum.USER_MOBILE_EXISTS);
            }
        }else {
            //1.4如果其他业务
            if (sysUserDTO == null){    //当前用户还未进行注册
                throw  new NcException(ResponseEnum.USER_NOT_REGISTER);
            }
        }
        //2.生成6位随机的验证码      hutool的类
        //String code = RandomUtil.randomNumbers(6);
        String code = "456789";
        //3.调用ariyun短信 ，发送验证码  //传入手机号 验证码
       // Boolean smsBoolean = smsUtils.sendSMS(mobile, code);
      Boolean smsBoolean = true;
        if (!smsBoolean){
            //发送失败
            throw  new NcException(ResponseEnum.SEND_MESSAGE_ERROR);
        }
        //4.存储验证码到redis 设置过期时间，key必须唯一
        // 4.1 根据业务类型，获取前缀 getPrefix 建个方法来判断 //登录  //注册  //更改管理员 //重置密码
        String prefix = getPrefix(type);

                                            //业务类型 + 手机号 +验证码 +保存时间
        redisTemplate.opsForValue().set(prefix+mobile,code, Duration.ofMinutes(2));

        log.info("短信发送成功，手机号 = " + mobile + "，业务类型 = " + type + "，验证码 = " + code);



    }
    /**
     * 短信: 验证码校验
     */
    @Override
    public Boolean verify(String checkcode, String mobile, String type) {
       if (StringUtils.isEmpty(checkcode)||StringUtils.isEmpty(mobile) ||
               StringUtils.isEmpty(type)){
           throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
       }
        //根据业务类型，获取前缀
        String prefix = getPrefix(type);
        //从redis中获取验证码进行判断                                   //验证码类型

        String redisCode = redisTemplate.opsForValue().get(prefix + mobile);
        //redis的验证码不能为空， 且与传进来的一致
        if (!StringUtils.isEmpty(redisCode) && redisCode.equals(checkcode)){
        //判断通过 删除验证码
            redisTemplate.delete(prefix+mobile);
            return true;
        }
        return false;
    }

    /**
     * 根据操作类型，获取验证码前缀
     * @param type
     * @return
     */
    private String getPrefix(String type){
        String prefix = "";
        switch (type){
            case "1":
                prefix = SMSConstant.SMS_LOGIN_KEY_PREFIX;//登录
                break;
            case "2":
                prefix = SMSConstant.SMS_REGISTER_KEY_PREFIX;//注册
                break;
            case "3":
                prefix = SMSConstant.SMS_CHANGE_MANAGER_KEY_PREFIX;//更改管理员
                break;
            case "4":
                prefix = SMSConstant.SMS_RESET_KEY_PREFIX;//重置密码
                break;
            default:
                break;
        }
        return prefix;
    }
}
