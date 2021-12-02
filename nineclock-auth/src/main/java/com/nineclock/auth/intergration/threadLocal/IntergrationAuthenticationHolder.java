package com.nineclock.auth.intergration.threadLocal;

import com.nineclock.auth.intergration.entity.IntergrationAuthenticationEntity;

/**
 * 存储集成认证实体对象
 */
public class IntergrationAuthenticationHolder {

    private static ThreadLocal<IntergrationAuthenticationEntity> ENTITY_LOCAL
        				= new ThreadLocal<IntergrationAuthenticationEntity>();

    /**
     * 获取认证实体信息
     * @return
     */
    public static IntergrationAuthenticationEntity get(){
        return ENTITY_LOCAL.get();
    }

    /**
     * 存储认证实体信息
     * @param entity
     */
    public static void set(IntergrationAuthenticationEntity entity){
        ENTITY_LOCAL.set(entity);
    }

    /**
     * 移除认证实体信息
     */
    public static void remove(){
        ENTITY_LOCAL.remove();
    }
}