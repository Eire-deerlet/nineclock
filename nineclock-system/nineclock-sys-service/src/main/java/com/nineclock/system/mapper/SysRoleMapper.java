package com.nineclock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nineclock.system.pojo.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据员工id查询对应的角色
     * @param companyUserId
     * @return
     */
    @Select("select  r.* from sys_company_user cu INNER JOIN sys_company_user_role cur INNER JOIN sys_role r " +
            "on cu.id = cur.company_user_id  and cur.role_id = r.id " +
            "where cu.id = #{companyUserId}")
    public List<SysRole> queryRoleByCompanyUserId(@Param("companyUserId") Long companyUserId);
}
