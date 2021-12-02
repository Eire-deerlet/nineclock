package com.nineclock.common.utils;

import java.util.UUID;

/**
 * 生成uuid
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
