package com.nineclock.common.utils;

import java.nio.ByteBuffer;


/**
 * 数据类型转换工具类
 */
public class ConvertUtil {

    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    /**
     * String 数组与 long 数组的相互转换
     */
    public static Long[] string2Long(String[] strArr) {
        Long[] longAry = new Long[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            longAry[i] = new Long(strArr[i]);
        }
        return longAry;
    }

    /**
     * String 数组与 int 数组的相互转换
     */
    public static Integer[] string2Integer(String[] strArr) {
        Integer[] intAry = new Integer[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            intAry[i] = new Integer(strArr[i]);
        }
        return intAry;
    }

    //byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}