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

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("at_attend_punch")
public class AttendPunch implements Serializable {

    //打卡ID
    @TableId(type = IdType.AUTO)
    private Long id;

    //用户ID
    private Long companyUserId;

    //企业ID
    private Long companyId;

    //纬度
    private BigDecimal lat;

    //经度
    private BigDecimal lng;

    //打卡地址
    private String address;

    //打开时间
    private Date punchTime;

    //打卡日期
    private String punchDateStr;

    //打卡类型 1正常；2迟到；3早退；4旷工
    private Integer morningPunchType;

    //打卡类型 1正常；2迟到；3早退；4旷工 5非工作日打卡
    private Integer afternoonPunchType;

    //是否考勤点范围内有效打卡
    private Boolean areaValid;

    //上下午打卡是否为有效打卡
    private Boolean effectiveValid;

    //上午或者下午 1为上午 2为下午
    private Integer punchOnOffWork;

    //1工作日 2休息日 3加班 4假期
    private Integer dayType;

    //数据来源：1打卡，2补卡
    private Integer punchSource;

    //1未审批 2审批通过 3审批不通过
    private Integer makeupStatus;

    //补卡原因
    private String makeupReason;

    //补卡审批人id
    private Long makeupApproveUserId;

    //补卡审批时间
    private Date makeupApproveTime;

    //创建时间
    private Date createTime;
    
}