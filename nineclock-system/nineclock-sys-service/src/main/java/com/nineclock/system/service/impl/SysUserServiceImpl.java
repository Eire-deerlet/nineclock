package com.nineclock.system.service.impl;
import java.util.Date;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.common.constant.SMSConstant;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.system.dto.SysRegisterDTO;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.mapper.SysUserMapper;
import com.nineclock.system.pojo.SysUser;
import com.nineclock.system.service.SmsService;
import com.nineclock.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SmsService smsService;

    /**
     * 根据用户名或手机号查询用户
     * @param username
     * @return
     */
    @Override
    public SysUserDTO queryUser(String username) {
       LambdaQueryWrapper<SysUser> wrapper =new LambdaQueryWrapper<>();
       wrapper.eq(SysUser::getUsername,username).
               or()
               .eq(SysUser::getMobile, username);
        wrapper.eq(SysUser::getStatus, 1);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        //如果查询到了用户
        if (sysUser != null) {
            //转成sysUser
            return BeanHelper.copyProperties(sysUser, SysUserDTO.class);
        }
        return null;
    }

    /**
     * 用户注册: 注册用户
     * 接口路径：POST/sys/user/register
     */
    @Override
    public void register(SysRegisterDTO registerDTO) {
        // 参数校验
        if(registerDTO == null || StringUtils.isEmpty(registerDTO.getMobile())
                || StringUtils.isEmpty(registerDTO.getPassword())
                || StringUtils.isEmpty(registerDTO.getCheckcode())){

            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //短信没有mapper ，就一个service
        // 校验验证码
        Boolean verify = smsService.verify(registerDTO.getCheckcode(), registerDTO.getMobile(), SMSConstant.SMS_TYPE_REGISTER);
        //如果不是true
        if(!verify){
            throw new NcException(ResponseEnum.INVALID_VERIFY_CODE);
        }
        // 新增用户信息
        SysUser sysUser = new SysUser();
        //生成密文存入数据库 防止泄密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        sysUser.setPassword(encoder.encode(registerDTO.getPassword())); //设置密码

        sysUser.setMobile(registerDTO.getMobile()); //用户手机号

        sysUser.setCreateTime(new Date());  //创建日期
        sysUser.setUpdateTime(new Date()); //更新日期
        sysUser.setStatus((short)1);    //用户状态
        sysUser.setLastLoginCompanyId(0L); //未次登录选择企业ID
        sysUserMapper.insert(sysUser);
    }
}
