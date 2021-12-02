package com.nineclock.common.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {

    AUDIT("audit", "alias", "有一项工作待您处理"),
    DOCUMENT("attendance", "alias", "有一个文档需要您协同"),
    ATTEND("attend", null, "打卡完成"),
    ATTEND_NOTIFY("attend_notify", "alias", "考勤提醒"),
    COMPANY_APPLY("company_apply", "alias", "团队申请"),
    COMPANY_APPLY_PASS("company_apply_pass", "alias", "团队申请通过");
    
    private String type; //消息类型
    private String audience; //消息推送目标类型
    private String title; //消息提醒标题

    MessageTypeEnum(String type, String audience, String title) {
        this.type = type;
        this.audience = audience;
        this.title = title;
    }
    public static MessageTypeEnum getEnumByType(String type) {
        if (null == type) {
            return null;
        }
        for (MessageTypeEnum temp : MessageTypeEnum.values()) {
            if (temp.getType().equals(type)) {
                return temp;
            }
        }
        return null;
    }
}