package com.nineclock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nineclock.system.pojo.SysCompanyUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysCompanayUserMapper  extends BaseMapper<SysCompanyUser> {
    //根据企业id查询主管理员信息
    @Select("SELECT cu.* FROM sys_company_user cu \n" +
            "INNER JOIN  sys_company_user_role cur ON cu.id = cur.company_user_id \n" +
            "INNER JOIN sys_role r ON cur.role_id = r.id\n" +
            "\n" +
            "WHERE  cu.company_id =#{companyId} \n" +
            "            and r.role_name = 'ROLE_ADMIN_SYS'")
    //返回是sys_company_user
 SysCompanyUser queryAdminCompanyUser(@Param("companyId") Long companyId);

}
