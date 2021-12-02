package com.nineclock.attendance.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nineclock.attendance.dto.AttendGroupDTO;
import com.nineclock.attendance.dto.AttendGroupPCExtraConfigDTO;
import com.nineclock.attendance.dto.AttendGroupPartDTO;
import com.nineclock.attendance.euums.AttendEnums;
import com.nineclock.attendance.mapper.AttendGroupMapper;
import com.nineclock.attendance.mapper.AttendGroupPartMapper;
import com.nineclock.attendance.mapper.AttendPunchMapper;
import com.nineclock.attendance.pojo.AttendGroup;
import com.nineclock.attendance.pojo.AttendGroupPart;
import com.nineclock.attendance.service.AttendGroupService;
import com.nineclock.common.entity.PageResult;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.system.feign.SysCompanyUserFeign;
import com.nineclock.system.feign.SysUserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class AttendGroupServiceImpl implements AttendGroupService {
    @Autowired
    private AttendGroupMapper attendGroupMapper;
    @Autowired
    private SysUserFeign sysUserFeign;
    @Autowired
    private AttendPunchMapper attendPunchMapper;
    @Autowired
    private AttendGroupPartMapper attendGroupPartMapper;
    @Autowired
    private SysCompanyUserFeign companyUserFeign;

    /**
     * 考勤组: 分页查询
     * <p>
     * /**
     * 分页查询考勤分页数据，业务实现基本步骤如下：
     * <p>
     * 1. 查询当前企业是否初次使用
     * <p>
     * 2. 如果初次使用需要新增默认考勤组, 设置企业所有部门员工都采用默认考勤组 (调用系统微服务获取企业所有部门)
     * <p>
     * 3. 分页查询企业考勤组列表数据
     * <p>
     * 4. 遍历列表，获取企业考勤组中参与者数量
     * <p>
     * 在第2步骤中, 需要根据查询当前企业下部门ID集合，用于将默认考勤组关联到部门。故需要在系统微服务中提供Feign接口逻辑为根据企业ID查询企业部门ID集合。
     * <p>
     * 在第4步骤中, 需要计算考勤组下关联的用户数量，故还需要提供根据部门ID查询部门员工数量的接口。
     */


    @Override
    public PageResult<AttendGroupDTO> queryAttendGroupPage(Integer page, Integer pageSize) {
        // 1.查询当前企业是否初次使用

        LambdaQueryWrapper<AttendGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendGroup::getCompanyId, CurrentUserHolder.get().getCompanyId());
        Integer count = attendGroupMapper.selectCount(wrapper);
        //如果初次使用需要新增默认考勤组

        if(count == 0){
            AttendGroup attendGroup = this.buildDefaultAttendGroup();
            attendGroupMapper.insert(attendGroup);

            //保存了默认的考勤组之后, 还需要设置所有的部门都采用该默认考勤组
            List<Long> ids = companyUserFeign.queryDepartmentIds().getData();

            for (Long departmentId : ids) {
                AttendGroupPart attendGroupPart = new AttendGroupPart();
                attendGroupPart.setAttendGroupId(attendGroup.getId());
                attendGroupPart.setObjectId(departmentId);
                attendGroupPart.setAttendType(AttendEnums.ATTEND_TYPE_YES.value());
                attendGroupPart.setObjectType(AttendEnums.ATTEND_OBJECT_TYPE_DEP.value());
                attendGroupPartMapper.insert(attendGroupPart);
            }
        }

        // 3.分页查询企业考勤组列表数据
        IPage<AttendGroup> ipage = new Page<>(page, pageSize);
        ipage = attendGroupMapper.selectPage(ipage, wrapper);


        //4. 遍历列表，获取企业考勤组中参与者数量
        // 4.1 获取当前页的数据，转为DTO集合
        List<AttendGroup> attendGroupList = ipage.getRecords();
        List<AttendGroupDTO> attendGroupDTOList = BeanHelper.copyWithCollection(attendGroupList, AttendGroupDTO.class);
        // 查询考勤组成员表，统计参数者数量
        for (AttendGroupDTO attendGroupDTO : attendGroupDTOList) {

            LambdaQueryWrapper<AttendGroupPart> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(AttendGroupPart::getAttendGroupId, attendGroupDTO.getId());  //考勤组ID= //考勤组id
            wrapper1.eq(AttendGroupPart::getAttendType, AttendEnums.ATTEND_TYPE_YES.value()); //参加的成员
            List<AttendGroupPart> attendGroupPartList = attendGroupPartMapper.selectList(wrapper1);

            // 统计参与者直接是员工的数量 + 参与者是部门下的员工数量

            Integer sum = 0; // 参与者总数

            List<Long> deptIdList = new ArrayList<>();  // 部门id集合
            for (AttendGroupPart attendGroupPart : attendGroupPartList) {
                // 如果参与者是员工，总数 + 1        //1部门 2用户  =         //参与考勤用户类型
                if (attendGroupPart.getObjectType().equals(AttendEnums.ATTEND_OBJECT_TYPE_USER.value())) {
                    sum++;
                } else {
                    // 如果参与者是部门，统计部门id //部门ID 或者是用户ID
                    deptIdList.add(attendGroupPart.getObjectId());
                }
            }
            // 调用系统微服务接口，获取指定部门下的员工数量
            Integer deptUserCount = companyUserFeign.queryUserCountByDepartmentIds(deptIdList).getData();
            sum = sum + deptUserCount;  // 汇总所有参与者数量
            attendGroupDTO.setMemberNum(sum);
        }
        return new PageResult<>(ipage.getTotal(), ipage.getPages(), attendGroupDTOList);
    }

    //构建默认的考勤组
    private AttendGroup buildDefaultAttendGroup() {
        AttendGroup attendGroup = new AttendGroup();
        attendGroup.setName("默认考勤组");
        attendGroup.setAddressRange(500); // 500米有效距离打卡
        attendGroup.setAllowLateMinutes(10); // 允许迟到分数
        attendGroup.setCompanyId(CurrentUserHolder.get().getCompanyId());
        attendGroup.setLng(new BigDecimal("23.13514700000000000000"));// 广州黑马程序员
        attendGroup.setLat(new BigDecimal("113.43467700000000000000"));// 广州黑马程序员
        attendGroup.setAddressName("黑马程序培训中心(广州校区)");
        attendGroup.setAddress("广东省广州市天河区珠吉街道珠吉路58号黑马培训机构");
        attendGroup.setStartWorkTime("09:00:00"); // 上班时间
        attendGroup.setOffWorkTime("18:00:00"); // 下班时间
        attendGroup.setStartNoonRestTime("12:00:00"); // 中午休息开始时间
        attendGroup.setEndNoonRestTime("14:00:00"); // 中午休息结束时间
        attendGroup.setLateMinutes(30); // 允许旷工分数
        attendGroup.setWorkdays("1,1,1,1,1,0,0");  // 工作日
        attendGroup.setCreateUserId(CurrentUserHolder.get().getCompanyUserId());
        attendGroup.setCreateTime(new Date());
        attendGroup.setUpdateUserId(CurrentUserHolder.get().getCompanyUserId());
        attendGroup.setUpdateTime(new Date());
        return attendGroup;
    }

    /**
     * 考勤组: 添加
     */
    @Override                               //考勤组
    public void addAttendGroup(AttendGroupDTO attendGroupDTO) {
        //1.健壮性判断
        if (attendGroupDTO == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //2.组装考勤组对象, 保存考勤组 ===> at_attend_group
        AttendGroup attendGroup = BeanHelper.copyProperties(attendGroupDTO, AttendGroup.class);
        //2.1 设置考勤组关联的企业ID - 创建人ID - 更新人ID 等基本信息
        attendGroup.setCompanyId(CurrentUserHolder.get().getCompanyId());
        attendGroup.setCreateUserId(CurrentUserHolder.get().getCompanyUserId());//创建人ID
        attendGroup.setUpdateUserId(CurrentUserHolder.get().getCompanyUserId());  //更新人ID
        //2.2 设置工作日，使用逗号 "," 分割
        if (attendGroupDTO.getWorkdays() != null) {
            attendGroup.setWorkdays(StrUtil.join(",", attendGroupDTO.getWorkdays().toArray()));
        }

        //2.3 设置必须/不用打卡的日期，获取出来之后使用逗号","分割
        List<String> necessaryTimeList = new ArrayList<>();
        List<String> unnecessaryTimeList = new ArrayList<>();
        //特殊日期: 必须打卡日期数组 ex: [2019-09-09, 2019-09-10]
        List<AttendGroupPCExtraConfigDTO> extraConfigDTOList = attendGroupDTO.getExtraConfig();
        //必须考勤日期 或 无需考勤日期
        for (AttendGroupPCExtraConfigDTO extraConfigDTO : extraConfigDTOList) {
            //设置日期
            String time = extraConfigDTO.getSetDate();
            String date = DateUtil.format(new Date(Long.valueOf(time)), "yyyy-MM-dd");
            //是否需要打卡：1为必须     0为无需
            if (extraConfigDTO.getRequiredAttend() == 1) {
                necessaryTimeList.add(date);
            } else if (extraConfigDTO.getRequiredAttend() == 0) {
                unnecessaryTimeList.add(date);
            }

        }

        //2.4 保存考勤组
        //保存考勤组
        attendGroupMapper.insert(attendGroup);

        this.saveAttendGroupPart(attendGroupDTO, attendGroup.getId());
    }



    /**
     * //3.保存关联用户、部门到该考勤组 ===> at_attend_group_part
     *
     * @param attendGroupDTO
     * @param attendGroupId
     */
    private void saveAttendGroupPart(AttendGroupDTO attendGroupDTO, Long attendGroupId) {
        //3.1 需要参与考勤组的对象处理
        List<AttendGroupPartDTO> partDTOS = attendGroupDTO.getParticipates();
        partDTOS.forEach(attendGroupPartDTO -> {
            //3.1.1 判断当前对象是否加入到某个考勤组（要求: 用户/部门只能加入一个考勤组
            LambdaQueryWrapper<AttendGroupPart> wrapper = new LambdaQueryWrapper<>();
            //考勤组ID =//用户或部门id
            wrapper.eq(AttendGroupPart::getObjectId, attendGroupPartDTO.getObjectId());
            //1部门 2用户=  //类型 1为部门 2为用户
            wrapper.eq(AttendGroupPart::getObjectType, attendGroupPartDTO.getObjectType());
            //是否参加 1：参加  2：不参加                  //是否参与考勤
            wrapper.eq(AttendGroupPart::getAttendType, AttendEnums.ATTEND_TYPE_YES.value());


            Integer count = attendGroupPartMapper.selectCount(wrapper);
            //3.1.2 如果存在旧的考勤组, 需要删除旧考勤组
            if (count > 0) {
                attendGroupPartMapper.delete(wrapper);
            }
            //3.1.3 给考勤对象关联新的考勤组
            AttendGroupPart attendGroupPart = new AttendGroupPart();

            attendGroupPart.setAttendGroupId(attendGroupId);
            attendGroupPart.setObjectType(attendGroupPartDTO.getObjectType());  //类型 1为部门 2为用户
            attendGroupPart.setObjectId(attendGroupPartDTO.getObjectId());   //用户或部门id
            attendGroupPartMapper.insert(attendGroupPart);

        });
        //3.2 不需要参与考勤组的对象处理
        List<AttendGroupPartDTO> notParticipates = attendGroupDTO.getParticipates();
        //3.2.1 判断用户/部门是否已经在无需参与考勤组中
        notParticipates.forEach(attendGroupPartDTO -> {
            LambdaQueryWrapper<AttendGroupPart> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(AttendGroupPart::getObjectId, attendGroupPartDTO.getObjectId());
            wrapper2.eq(AttendGroupPart::getObjectType, attendGroupPartDTO.getObjectType());
            wrapper2.eq(AttendGroupPart::getAttendType, AttendEnums.ATTEND_TYPE_NO.value());
            wrapper2.eq(AttendGroupPart::getAttendGroupId, attendGroupId);
            //3.2.2 判定是否已经存在，如果不存在，则新增无需考勤组记录，如果存在，不需要处理
            Integer count = attendGroupPartMapper.selectCount(wrapper2);
            if (count == 0) {
                AttendGroupPart attendGroupPart = new AttendGroupPart();
                //是否参加 1：参加  2：不参加
                attendGroupPart.setAttendType(AttendEnums.ATTEND_TYPE_NO.value());
                //部门ID 或者是用户ID
                attendGroupPart.setObjectId(attendGroupPartDTO.getObjectId());
                //考勤组ID
                attendGroupPart.setAttendGroupId(attendGroupId);
                //1部门 2用户
                attendGroupPart.setObjectType(attendGroupPartDTO.getObjectType());

                //2.2 新增无需考勤组用户记录
                attendGroupPartMapper.insert(attendGroupPart);
            }

        });

    }
    /**
     * 考勤组: 获取当前登录用户考勤组
     */
    @Override
    public AttendGroupDTO getAttendGroupByUserId() {
        //获取当前登录用户的员工id
        Long companyUserId = CurrentUserHolder.get().getCompanyUserId();

        // 根据员工id查询考勤组参与者
        LambdaQueryWrapper<AttendGroupPart> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(AttendGroupPart::getObjectId, companyUserId);
        //1部门 2用户
        wrapper.eq(AttendGroupPart::getObjectType,AttendEnums.ATTEND_OBJECT_TYPE_USER.value());
        wrapper.eq(AttendGroupPart::getAttendType,AttendEnums.ATTEND_TYPE_YES.value());
        List<AttendGroupPart> attendGroupParts = attendGroupPartMapper.selectList(wrapper);
        // 如果不存在，则查询员工所在部门及上级部门的id集合，根据部门id查询考勤组参与者
        if (CollectionUtils.isEmpty(attendGroupParts)){
            //根据员工id获得部门从低到高级别的部门ID集合
            List<Long> longList = companyUserFeign.queryDepartmentsByUserId(companyUserId).getData();
            for (Long deptId : longList) {
                LambdaQueryWrapper<AttendGroupPart> wrapper1 =new LambdaQueryWrapper<>();
                //部门ID 或者是用户ID  =  部门ID集合
                wrapper1.eq(AttendGroupPart::getObjectId, deptId);
                wrapper1.eq(AttendGroupPart::getObjectType, AttendEnums.ATTEND_OBJECT_TYPE_DEP.value());
                wrapper1.eq(AttendGroupPart::getAttendType, AttendEnums.ATTEND_TYPE_YES.value());
                attendGroupParts =   attendGroupPartMapper.selectList(wrapper1);
                // 如果存在参与者信息，直接跳出循环
                if(!CollectionUtils.isEmpty(attendGroupParts)){
                    break;
                }
            }
        }
        // 如果仍然不存在，抛出异常
        if (CollectionUtils.isEmpty(attendGroupParts)){
            //未查询到用户考勤组,请先配置
            throw  new NcException(ResponseEnum.USER_NOT_MATCH_ATTENDGROUP);
        }else {
            //封装结构返回
            AttendGroupPart attendGroupPart = attendGroupParts.get(0);
            //根据考勤组id查询考勤组
            AttendGroup attendGroup = attendGroupMapper.selectById(attendGroupPart.getAttendGroupId());
            //数据转换
            AttendGroupDTO attendGroupDTO = BeanHelper.copyProperties(attendGroup, AttendGroupDTO.class);

            //额外 设置工作日属性
            String workdays = attendGroup.getWorkdays();
            attendGroupDTO.setWorkdays(Arrays.asList(workdays.split(",")));

            return attendGroupDTO;
        }
    }


}
