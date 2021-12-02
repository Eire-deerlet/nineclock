package com.nineclock.auth.config;

import com.nineclock.auth.intergration.entity.IntergrationAuthenticationEntity;
import com.nineclock.auth.intergration.processor.AuthenticationProcessor;
import com.nineclock.auth.intergration.threadLocal.IntergrationAuthenticationHolder;
import com.nineclock.auth.intergration.threadLocal.UserHolder;
import com.nineclock.common.constant.NcConstant;

import com.nineclock.common.entity.UserInfo;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.common.utils.JsonUtils;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.dto.SysRoleDTO;
import com.nineclock.system.dto.SysUserDTO;
import com.nineclock.system.feign.SysCompanyUserFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义用户详情类
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private List<AuthenticationProcessor> processorList;

    @Autowired
    private SysCompanyUserFeign sysCompanyUserFeign;

    // 明文密码为 123456
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1.从threadLocal中获取entity
        IntergrationAuthenticationEntity entity = IntergrationAuthenticationHolder.get();

        // 2.根据authType选择认证处理器
        AuthenticationProcessor authenticationProcessor = chooseAuthenticationProcessor(entity);
        if (authenticationProcessor == null) {
            throw new NcException(ResponseEnum.SERVER_ERROR);
        }

        log.info("选择的认证处理器为：" + authenticationProcessor.getClass().getSimpleName());

        // 3.认证处理器进行认证
        SysUserDTO sysUserDTO = authenticationProcessor.authenticate(entity);

        // 4.将SysUserDTO转换为UserInfo
        UserInfo userInfo = BeanHelper.copyProperties(sysUserDTO, UserInfo.class);

        // 【认证完善------------------------start】
        // 获取请求参数中的客户端id以及企业id
        String clientId = entity.getAuthParameter("client_id");
        String companyIdStr = entity.getAuthParameter("company_id");
        Long companyId = StringUtils.isEmpty(companyIdStr) ? null : Long.valueOf(companyIdStr);

        // PC端认证
        if (clientId.equals("pc_client")) {

            // 调用系统微服务接口，根据用户id、企业id得到员工列表
            List<SysCompanyUserDTO> companyUserDTOList = sysCompanyUserFeign.queryCompanyUserDTO(sysUserDTO.getId(), companyId).getData();

            if (CollectionUtils.isEmpty(companyUserDTOList)) {
                throw new NcException(ResponseEnum.USER_NOT_JOIN_COMPANY);
            }

            // 判断是否是企业管理员，非管理员不准登录pc端
            Boolean isAdmin = checkAdmin(companyUserDTOList);
            if (!isAdmin) {
                throw new NcException(ResponseEnum.USER_NOT_COMPANY_ADMIN);
            }

            // 如果员工信息只有一个，获取第一条数据，封装员工信息
            // 如果企业id不为空，代表是后续用户选择了企业，也是获取第一条数据，封装员工信息
            if (companyUserDTOList.size() == 1 || companyId != null) {
                SysCompanyUserDTO sysCompanyUserDTO = companyUserDTOList.get(0);
                // 封装员工信息
                wrapper(sysCompanyUserDTO, userInfo);
            }

        } else {  // APP端认证

            if(companyId == null){ // 如果没有传递企业id，获取用户最后一次登录的企业id
                companyId = sysUserDTO.getLastLoginCompanyId();
            }
            // 调用系统微服务接口，根据用户id、企业id得到员工列表
            List<SysCompanyUserDTO> companyUserDTOList = sysCompanyUserFeign.queryCompanyUserDTO(sysUserDTO.getId(), companyId).getData();

            if (CollectionUtils.isEmpty(companyUserDTOList)) {
                throw new NcException(ResponseEnum.USER_NOT_JOIN_COMPANY);
            }

            SysCompanyUserDTO sysCompanyUserDTO = companyUserDTOList.get(0);

            // 封装员工信息
            wrapper(sysCompanyUserDTO, userInfo);

        }
        // 【认证完善------------------------end】

        // 5.将用户信息放到threadLocal中
        UserHolder.set(userInfo);

        // 【把用户的角色、权限设置为数据库查出来的信息】
        return User.withUsername(JsonUtils.toString(userInfo)).password(sysUserDTO.getPassword())
                .authorities(userInfo.getGrantedAuthorities()).build();
    }

    /**
     * 组装用户的员工信息
     * @param sysCompanyUserDTO
     * @param userInfo
     */
    private void wrapper(SysCompanyUserDTO sysCompanyUserDTO, UserInfo userInfo) {
        userInfo.setCompanyId(sysCompanyUserDTO.getCompanyId());
        userInfo.setCompanyName(sysCompanyUserDTO.getCompanyName());
        userInfo.setDepartmentId(sysCompanyUserDTO.getDepartmentId());
        userInfo.setDepartmentName(sysCompanyUserDTO.getDepartmentName());
        userInfo.setCompanyUserId(sysCompanyUserDTO.getId());
        userInfo.setPost(sysCompanyUserDTO.getPost());
        userInfo.setWorkNumber(sysCompanyUserDTO.getWorkNumber());
        userInfo.setTimeEntry(sysCompanyUserDTO.getTimeEntry());
        userInfo.setEnable(true);

        // 用户的权限
        if (!CollectionUtils.isEmpty(sysCompanyUserDTO.getGrantedAuthorities())) {
            userInfo.setGrantedAuthorities(sysCompanyUserDTO.getGrantedAuthorities());
        }
    }

    /**
     * 判断员工是否是企业管理员
     * @param companyUserDTOList
     * @return
     */
    private Boolean checkAdmin(List<SysCompanyUserDTO> companyUserDTOList) {

        for (SysCompanyUserDTO sysCompanyUserDTO : companyUserDTOList) {
            // 获取员工对应的角色
            List<SysRoleDTO> roles = sysCompanyUserDTO.getRoles();

            if (!CollectionUtils.isEmpty(roles)) {

                for (SysRoleDTO role : roles) {
                    // 如果角色名称以ROLE_ADMIN_开头，代表是管理员角色
                    if (role.getRoleName().startsWith(NcConstant.ADMIN_ROLE_PREFIX)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 选择认证处理器
     * @param entity
     * @return
     */
    public AuthenticationProcessor chooseAuthenticationProcessor(IntergrationAuthenticationEntity entity) {
        for (AuthenticationProcessor authenticationProcessor : processorList) {
            if (authenticationProcessor.support(entity)) {
                return authenticationProcessor;
            }
        }
        return null;
    }
}