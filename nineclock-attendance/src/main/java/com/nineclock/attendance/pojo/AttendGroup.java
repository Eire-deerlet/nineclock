package com.nineclock.attendance.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 考勤组
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("at_attend_group")
public class AttendGroup implements Serializable {

    //考勤组id
    @TableId(type = IdType.AUTO)
    private Long id;

    //考勤组名称
    private String name;

    //公司ID
    private Long companyId;

    //考勤组负责人ID
    private Long managerId;

    //上班时间
    private String startWorkTime;

    //允许迟到分数数
    private Integer allowLateMinutes;

    //旷工迟到分钟数
    private Integer lateMinutes;

    //午休开始时间
    private String startNoonRestTime;

    //午休结束时间
    private String endNoonRestTime;

    //下班时间
    private String offWorkTime;

    //工作日设置 工作日通用设置每周双休：样例 [1,1,1,1,1,0,0]  0:非工作日 1：工作日
    private String workdays;

    //考勤地址详情
    private String address;

    //考勤地点名称 考勤地点名称
    private String addressName;

    //考勤地点GPS 考勤地点纬度
    private BigDecimal lat;

    //考勤地点GPS 考勤地点经度
    private BigDecimal lng;

    //考勤地点范围 单位：米
    private Integer addressRange;

    //加班规则id
    private Long overtimeRuleId;

    //创建人
    private Long createUserId;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //更新用户ID
    private Long updateUserId;

    //必须打卡日期，多个打卡日期使用逗号分隔
    private String necessaryTimeList;

    //不需要打卡日期，多个打卡日期使用逗号分隔
    private String unnecessaryTimeList;
}
