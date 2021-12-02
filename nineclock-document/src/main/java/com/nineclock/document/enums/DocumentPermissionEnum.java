package com.nineclock.document.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 文档权限枚举类
 */
@Getter
public enum DocumentPermissionEnum {

    ALL_READ((short)0, "公共读"),
    ALL_READ_WRITE((short)1, "公共读写"),
    PRIVATE_READ_WRITE((short)2, "私有");

    DocumentPermissionEnum(Short permission, String desc) {
        this.permission = permission;
        this.desc = desc;
    }

    private Short permission;
    private String desc;

}