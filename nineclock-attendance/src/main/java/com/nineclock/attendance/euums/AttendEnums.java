package com.nineclock.attendance.euums;

/**
 * 考勤模块枚举类型
 */
public enum  AttendEnums {

    //参与考勤用户类型
    ATTEND_OBJECT_TYPE_DEP(1,"部门"),
    ATTEND_OBJECT_TYPE_USER(2,"用户"),

    //是否参与考勤
    ATTEND_TYPE_YES(1,"参加"),
    ATTEND_TYPE_NO(2,"不参加"),

    SETTING_MAKEUP(1, "补卡规则"),
    SETTING_ATTENDANCE(2, "考勤配置"),

    //打卡类型
    PUNCH_TYPE_OK(1, "正常"),
    PUNCH_TYPE_LATE(2, "迟到"),
    PUNCH_TYPE_EARLY(3, "早退"),
    PUNCH_TYPE_STAYALAY(4, "旷工"),
    PUNCH_TYPE_NOT_WORK_DAY(5, "非工作日打卡"),

    //打卡上午或下午
    NOON_TYPE_MORNING(1, "上午"),
    NOON_TYPE_AFTERNOON(2, "下午"),

    //当天打卡类型
    DAY_TYPE_WORKDAY(1, "工作日"),
    DAY_TYPE_REST(2, "休息日"),
    DAY_TYPE_OVERTIME(3, "加班"),
    DAY_TYPE_HOLIDAY(4, "假期'"),

    //打卡数据来源
    PUNCH_SOURCE_PUNCH(1, "打卡"),
    PUNCH_SOURCE_MAKEUP(1, "补卡"),

    //最小考勤单位
    OVERTIME_UNIT_MINUTES(1, "分钟"),
    OVERTIME_UNIT_HALF_HOUR(2, "半小时"),
    OVERTIME_UNIT_HOUR(3, "小时"),
    OVERTIME_UNIT_HALF_DAY(4, "半天"),
    OVERTIME_UNIT_DAY(1, "天"),
    
     //打卡 补卡
    PUNCH_NORMAL(1, "正常打卡"),
    PUNCH_MAKEUP(2, "补卡"),

    ;

    private final int value;
    private final String desc;

    AttendEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return desc;
    }

}