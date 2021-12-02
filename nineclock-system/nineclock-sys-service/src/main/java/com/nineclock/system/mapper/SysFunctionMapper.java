package com.nineclock.system.mapper;

import com.nineclock.system.pojo.SysFunction;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysFunctionMapper {
    //根据角色id 找到权限
    @Select("SELECT sr.* FROM sys_role sr INNER JOIN sys_role_function  srf INNER JOIN sys_function  sf ON  sr.id =srf.function_id AND\n" +
            "srf.function_id = sf.id where  sr.id = #{RoleId}\n")
    public List<SysFunction>  queryRoleIDFunction(@Param("RoleId") Long  RoleId);
}
