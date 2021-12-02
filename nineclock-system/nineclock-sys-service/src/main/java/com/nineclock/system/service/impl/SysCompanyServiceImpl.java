package com.nineclock.system.service.impl;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.common.constant.NcConstant;
import com.nineclock.common.constant.SMSConstant;
import com.nineclock.common.enums.MessageTypeEnum;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.common.oss.OssClientUtils;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.system.dto.*;
import com.nineclock.system.im.HxImManager;
import com.nineclock.system.mapper.*;
import com.nineclock.system.message.MessageDTO;
import com.nineclock.system.pojo.*;
import com.nineclock.system.service.SysCompanyService;
import com.nineclock.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
@Transactional
@Slf4j
public class SysCompanyServiceImpl implements SysCompanyService {

    @Autowired
    private SysCompanayMapper sysCompanayMapper;

    @Autowired
    private OssClientUtils ossClientUtils;


    @Autowired
    private SysCompanayUserMapper companayUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysCompanyUserRoleMapper companyUserRoleMapper;

    @Autowired
    private SysUserService userService;

    @Autowired

    private SysUserMapper userMapper;
    //RocketMQ
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Autowired
    private HxImManager hxImManager;



    @Override
    public List<SysCompanyDTO> findCompanyByUserId(Long userId) {
        // 参数校验
        if (userId == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }

        // 根据用户id查询关联的企业列表
        List<SysCompany> sysCompanyList = sysCompanayMapper.queryUserJoinCompany(userId);
        if (CollectionUtils.isEmpty(sysCompanyList)) {
            // 如果为空，代表未加入企业，抛出异常
            throw new NcException(ResponseEnum.USER_NOT_JOIN_COMPANY);

        }

        // 数据转换
        List<SysCompanyDTO> companyDTOList = BeanHelper.copyWithCollection(sysCompanyList, SysCompanyDTO.class);

        return companyDTOList;
    }


    @Override
    public SysCompanyDTO queryCompanyInfo()  {
        // 获取当前登录用户id
        Long companyId = CurrentUserHolder.get().getCompanyId();

        // 根据id查询企业信息
        SysCompany sysCompany = sysCompanayMapper.selectById(companyId);

        if(sysCompany == null){
            throw new NcException(ResponseEnum.COMPANY_NOT_FOUND);
        }

        return BeanHelper.copyProperties(sysCompany, SysCompanyDTO.class);
    }



    @Override
    public String uploadOSS(MultipartFile file) {
        //参数校验
        if (file == null){
            throw new NcException(ResponseEnum.INVALID_FILE_TYPE);
        }

        //文件类型检验
        String contentType = file.getContentType();
        //"image/jpeg", "image/bmp", "image/png"; //允许的图片类型
        if (!NcConstant.ALLOWED_IMG_TYPES.contains(contentType)){
            throw new NcException(ResponseEnum.INVALID_FILE_TYPE);
        }
        //文件大小校验
        if (file.getSize()>NcConstant.maxFileSize){
            throw new NcException(ResponseEnum.FILE_SIZE_EXCEED_MAX_LIMIT);
        }

        //4.调用oss工具类上传文件
        String flag = null;
        try {
            flag = ossClientUtils.uploadFile(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void updateCompanyInfo(SysCompanyDTO sysCompanyDTO) {
        // 参数校验
        if (sysCompanyDTO == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //拿到当前登录的企业id
        Long companyId = CurrentUserHolder.get().getCompanyId();

        //更新企业的信息
        SysCompany sysCompany = new SysCompany();
        //将传入的企业名字 和企业logo更新
        sysCompany.setName(sysCompanyDTO.getName());
        sysCompany.setLogo(sysCompanyDTO.getLogo());
        //将登录的企业id 也写入
        sysCompany.setId(companyId);
        sysCompanayMapper.updateById(sysCompany);


        //还需要更新 员工表中的企业名称
        SysCompanyUser sysCompanyUser = new SysCompanyUser();
        //将新的企业名字 更新到企业员工表
        sysCompanyUser.setCompanyName(sysCompanyDTO.getName());

        LambdaQueryWrapper<SysCompanyUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysCompanyUser::getCompanyId,companyId);

        companayUserMapper.update(sysCompanyUser,wrapper);

    }

    /**
     * 企业管理: 更改系统管理员
     */
    @Override
    public void changeSysAdmin(String code, Long userId) {
        // 参数校验
        if(StringUtils.isEmpty(code) || userId == null){
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //根据传入的userId 拿到 手机号
        SysCompanyUser companyUser = companayUserMapper.selectById(userId);
        //从redis拿到验证码进行判断                   ////重置密码
        String redisCode = redisTemplate.opsForValue().get(SMSConstant.SMS_CHANGE_MANAGER_KEY_PREFIX + companyUser.getMobile());

        if (StringUtils.isEmpty(redisCode)||!redisCode.equals(code)){
            throw new NcException(ResponseEnum.INVALID_VERIFY_CODE);
        }

        //先查询当前主管理员的信息
        SysCompanyUserDTO currentAdmin = this.getCurrentAdmin();


        // 查询系统管理员的角色id
        LambdaQueryWrapper<SysRole> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getCompanyId,currentAdmin.getCompanyId()); //管理员的企业id

        wrapper.eq(SysRole::getRoleName, NcConstant.ADMIN_ROLE_SYS); //系统管理员角色名称
        SysRole sysRole = roleMapper.selectOne(wrapper);

        // 更换管理员
        LambdaQueryWrapper<SysCompanyUserRole> wrapper1= new LambdaQueryWrapper<>();

        //三表连接进行修改
        wrapper1.eq(SysCompanyUserRole::getCompanyUserId,currentAdmin.getId()); //管理员的id
        wrapper1.eq(SysCompanyUserRole::getRoleId,sysRole.getId()); //管理的角色id



        SysCompanyUserRole sysCompanyUserRole =new SysCompanyUserRole();
        sysCompanyUserRole.setCompanyUserId(userId);

        companyUserRoleMapper.update(sysCompanyUserRole,wrapper1);

    }

    private SysCompanyUserDTO getCurrentAdmin() {
        //拿到当前登录的企业id
        Long companyId = CurrentUserHolder.get().getCompanyId();
        //企业id拿到主管理员
        SysCompanyUser sysCompanyUser = companayUserMapper.queryAdminCompanyUser(companyId);


    return BeanHelper.copyProperties(sysCompanyUser,SysCompanyUserDTO.class);

    }
    /**
     *  根据关键字名称查询企业  like
     */
    @Override
    public List<SysCompanyDTO> queryCompanyByName(String keyword) {
        LambdaQueryWrapper<SysCompany> wrapper =new LambdaQueryWrapper<>();
        //关键字查询
        wrapper.like(SysCompany::getName,keyword);
        List<SysCompany> companyList = sysCompanayMapper.selectList(wrapper);
        return  BeanHelper.copyWithCollection(companyList, SysCompanyDTO.class);

    }

    /**
     * 加入企业申请
     */
    @Override
    public void applyJoinCompany(SysApplyJoinCompanyUserDTO applyJoinCompanyUserDTO) {
        // 参数校验
        if(applyJoinCompanyUserDTO == null || applyJoinCompanyUserDTO.getMobile() == null){
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }

        //调用userService 传入输入的输入的手机号 查到一个 sysUser
        SysUserDTO userDTO = userService.queryUser(applyJoinCompanyUserDTO.getMobile());

        SysUser sysUser = BeanHelper.copyProperties(userDTO, SysUser.class);

        sysUser.setUsername(applyJoinCompanyUserDTO.getUserName()); //传入的名字
        sysUser.setEmail(applyJoinCompanyUserDTO.getEmail());  //邮箱
        sysUser.setUpdateTime(new Date()); //时间
        sysUser.setLastLoginCompanyId(applyJoinCompanyUserDTO.getCompanyId()); //公司id
        //修改用户信息
        userMapper.updateById(sysUser);


        // 新增员工信息
        SysCompanyUser sysCompanyUser = new SysCompanyUser();
        sysCompanyUser.setUserId(userDTO.getId());
        sysCompanyUser.setCompanyId(applyJoinCompanyUserDTO.getCompanyId());
        sysCompanyUser.setCompanyName(applyJoinCompanyUserDTO.getCompanyName());
        sysCompanyUser.setPost(applyJoinCompanyUserDTO.getPost());
        sysCompanyUser.setEmail(applyJoinCompanyUserDTO.getEmail());
        sysCompanyUser.setTimeEntry(new Date());
        sysCompanyUser.setRemark(applyJoinCompanyUserDTO.getApplyReason());
        sysCompanyUser.setEnable((short)0); // 未审核
        sysCompanyUser.setCreateTime(new Date());
        sysCompanyUser.setUpdateTime(new Date());
        sysCompanyUser.setMobile(applyJoinCompanyUserDTO.getMobile());
        sysCompanyUser.setUserName(applyJoinCompanyUserDTO.getUserName());
        sysCompanyUser.setImageUrl(applyJoinCompanyUserDTO.getImageUrl());

        //企业用户类添加
        companayUserMapper.insert(sysCompanyUser);


        //发送消息：
        //参数1：消息的主题：标签 ，参数2：消息内容（一般用JSON字符串）
        MessageDTO messageDTO =new MessageDTO();
        messageDTO.setMessageType(MessageTypeEnum.COMPANY_APPLY.getType()); // 消息类型
        messageDTO.setCompanyId(sysCompanyUser.getCompanyId().toString()); // 公司id
        messageDTO.setTitle(MessageTypeEnum.COMPANY_APPLY.getTitle()); // 标题
        messageDTO.setContent(sysCompanyUser.getUserName() + " 申请加入企业，请及时审批"); // 内容
        messageDTO.setUseStatus(0); // 未读
        messageDTO.setAudience(MessageTypeEnum.COMPANY_APPLY.getAudience()); // 消息作用域

        // 获取当前企业主管理员
        SysCompanyUser mainAdmin = companayUserMapper.queryAdminCompanyUser(sysCompanyUser.getCompanyId());
        messageDTO.setTargets(Arrays.asList(mainAdmin.getMobile())); // 目标用户，即审批人

        messageDTO.setApproveStatue(0); // 未审核
        messageDTO.setApplyUserId(sysCompanyUser.getUserId()); // 申请用户ID
        messageDTO.setApplyUsername(sysCompanyUser.getUserName()); // 申请用户名称

        messageDTO.setApproveUserId(mainAdmin.getUserId()); // 审批用户id
        messageDTO.setApproveUsername(mainAdmin.getUserName()); // 审批用户名称

        rocketMQTemplate.convertAndSend("messagePushTopic", JSON.toJSONString(messageDTO));


        // 注册用户到环信云, 用于APP端用户之间的及时通讯
        sysUser.setPassword("123456");
        sysUser.setUsername(sysUser.getMobile());//手机号
        hxImManager.registerUser2HuanXing(sysUser);
        log.info("注册用户到环信云");
    }

    /**
     * 加入企业审核
     */
    @Override
    public void allowedJoinCompany(SysAllowedJoinCompanyUserDTO sysAllowedJoinCompanyUserDTO) {

        //根据企业ID、用户ID查询员工信息
        LambdaQueryWrapper<SysCompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId()); // 企业id
        wrapper.eq(SysCompanyUser::getUserId, sysAllowedJoinCompanyUserDTO.getApplyUserId());//申请人id

        SysCompanyUser sysCompanyUser = companayUserMapper.selectOne(wrapper);


        //修改状态 就是审核 把 0该成1
        if (sysCompanyUser !=null){
            sysCompanyUser.setRemark(sysAllowedJoinCompanyUserDTO.getRemark()); //批注
                                                                //设为传入 如果是true 就是设为1
            sysCompanyUser.setEnable(sysAllowedJoinCompanyUserDTO.getApproved()?(short)1:(short)0);

            companayUserMapper.updateById(sysCompanyUser);
            if (sysAllowedJoinCompanyUserDTO.getApproved()){ //如果为true 审批通过
                //发送消息到MQ, 更新加入团队申请的消息状态 为已审核通过;
                rocketMQTemplate.convertAndSend("allowedJoinCompanyTopic",
                        sysAllowedJoinCompanyUserDTO.getNotifyMsgId());//审批提醒消息ID
                //发送消息到MQ，推送消息给用户，通知审批通过
                MessageDTO messageDTO = new MessageDTO();


                messageDTO.setContent(sysCompanyUser.getUserName()+"加入企业申请通过");

                messageDTO.setTargets(Collections.singletonList(sysCompanyUser.getMobile()));

                messageDTO.setApplyUserId(sysAllowedJoinCompanyUserDTO.getApplyUserId());
                messageDTO.setApplyUsername(sysCompanyUser.getUserName());

                log.info("发送消息到MQ，推送审核通过的消息给系统管理员");
                rocketMQTemplate.convertAndSend("messagePushTopic",messageDTO);

            }
        }else {
            //审核拒绝
            //删除员工信息
            LambdaQueryWrapper<SysCompanyUser> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId()); //企业id
            wrapper1.eq(SysCompanyUser::getUserId, sysAllowedJoinCompanyUserDTO.getApplyUserId()); //申请人ID
            companayUserMapper.delete(wrapper1);
        }


    }

}
