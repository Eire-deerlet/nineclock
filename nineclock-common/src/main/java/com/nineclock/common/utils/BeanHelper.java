package com.nineclock.common.utils;

import com.nineclock.common.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 对象拷贝工具类
 */
@Slf4j
public class BeanHelper {

    /**
     * 对象拷贝
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> target){
        try {
            if(source == null) return null;
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException(ResponseEnum.DATA_TRANSFER_ERROR.getMessage());
        }
    }


    public static <T> T copyProperties(Object source, Class<T> target, String... ignoreProperties){
        try {
            if(source == null) return null;
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t, ignoreProperties);
            return t;
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException(ResponseEnum.DATA_TRANSFER_ERROR.getMessage());
        }
    }

    /**
     * 对象集合拷贝
     * @param sourceList
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target){
        try {
            if(sourceList == null) return null;
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException(ResponseEnum.DATA_TRANSFER_ERROR.getMessage());
        }
    }

    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target){
        try {
            if(sourceList == null) return null;
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new RuntimeException(ResponseEnum.DATA_TRANSFER_ERROR.getMessage());
        }
    }
}
