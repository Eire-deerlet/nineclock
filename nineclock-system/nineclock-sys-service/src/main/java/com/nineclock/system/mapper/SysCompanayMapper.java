package com.nineclock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nineclock.system.pojo.SysCompany;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysCompanayMapper extends BaseMapper<SysCompany> {

    /**
     * 根据用户id查询关联的企业列表
     * @param userId
     * @return
     */
    @Select("select c.* from sys_company_user cu " +
            " INNER JOIN sys_company c on cu.company_id = c.id where cu.user_id = #{userId}")
    public List<SysCompany> queryUserJoinCompany(@Param("userId") Long userId);
}