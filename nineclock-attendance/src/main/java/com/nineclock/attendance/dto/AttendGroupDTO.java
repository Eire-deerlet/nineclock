package com.nineclock.attendance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 考勤组
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendGroupDTO implements Serializable {

    //考勤组id
    private Long id;

    //考勤组名称
    private String name;

    //考勤组人数
    private Integer memberNum;

    //上班时间
    private String startWorkTime;

    //下班时间
    private String offWorkTime;

    //工作日设置值：ex 1,1,1,1,1,0,0  0:否定 1:肯定
    private List<String> workdays;

    //公司ID
    private Long companyId;

    //负责人ID
    private Long managerId;

    //允许迟到分数数
    private Integer allowLateMinutes;

    //旷工迟到分钟数
    private Integer lateMinutes;

    //午休开始时间
    private String startNoonRestTime;

    //午休结束时间
    private String endNoonRestTime;

    //考勤地点
    private String address;

    //考勤地名称
    private String addressName;

    //考勤地纬度
    private BigDecimal lat;

    //考勤地经度
    private BigDecimal lng;

    //有效范围 单位：米
    private Integer addressRange;

    //加班规则ID
    private Long overtimeRuleId;

    //创建用户ID
    private Long createUserId;

    //最后更细腻用户ID
    private Long updateUserId;

    //参与考勤人员
    private List<AttendGroupPartDTO> participates;

    //无需考勤人员
    private List<AttendGroupPartDTO> notParticipates;

    //特殊日期: 必须打卡日期数组 ex: [2019-09-09, 2019-09-10]
    private List<AttendGroupPCExtraConfigDTO> extraConfig;


}