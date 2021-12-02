package com.nineclock.common.constant;

import java.util.Arrays;
import java.util.List;
//默认需要的企业通讯录
public class NcConstant {

    public static final String ADMIN_ROLE_PREFIX = "ROLE_ADMIN_"; //管理员角色名称前缀

    public static final String ADMIN_ROLE_SYS = "ROLE_ADMIN_SYS"; //系统管理员角色名称

    public static final List<String> ALLOWED_IMG_TYPES = Arrays.asList("image/jpeg", "image/bmp", "image/png"); //允许的图片类型

    public static final Long maxFileSize = 1024 * 1024L; //阿里云OSS文件大小限制

    //企业通讯录配置信息
    public static final String[] COMPANY_CONFIG_CONTACT_LIST = new String[]{"部门", "职位", "姓名", "工号", "手机号", "邮箱", "座机", "办公地点", "备注", "入职时间"};

}
