package com.nineclock.attendance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendPunchDTO implements Serializable {

    //id
    private Long id;

    //用户id
    private Long companyUserId;

    //用户名称
    private String userName;

    //工号
    private String workNumber;

    //职位
    private String post;

    //部门名称
    private String departmentName;

    //考勤信息列表: AttendsDTO中包括打卡日期 2020-05-30  上下午打卡状态 正常/缺卡
    private List<AttendsDTO> attendsList;

    //打卡时间
    private String dateStr;

    //公司id
    private Long companyId;

    //上午或者下午 1为上午 2为下午
    private Integer punchOnOffWork;

    private Integer noonType;

    //经度
    private BigDecimal lat;

    //纬度
    private BigDecimal lng;

    //地址
    private String address;

    //打卡时间
    @JsonFormat(pattern = "HH:mm:ss", timezone ="GMT+8")
    private Date punchTime;

    //打卡类型 1正常；2迟到；3早退；4旷工
    private Integer morningPunchType;

    //打卡类型 1正常；2迟到；3早退；4旷工
    private Integer afternoonPunchType;

    //数据来源：1打卡，2补卡
    private Integer punchSource;

    //1未审批 2审批通过 3审批不通过
    private Integer makeupStatus;

    //补卡原因
    private String makeupReason;

    //补卡审批人id
    private Long makeupApproveUserId;

}