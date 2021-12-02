package com.nineclock.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算工具类
 */
public class CountUtil {

    /**
     * 通过总数量计算出总页数，返回页数list
     *
     * @param totalCount
     * @return
     */
    public static List getPageListByTotalCount(Long totalCount, int pageNumber) {
        int page = (int) Math.ceil((double) totalCount / pageNumber);
        List<Integer> pageList = new ArrayList<>();
        for (int i = 1; i <= page; i++) {
            pageList.add(i);
        }
        return pageList;
    }

    public static String getSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static Long getIntSize(long size) {
        return size / 1024;

    }
}