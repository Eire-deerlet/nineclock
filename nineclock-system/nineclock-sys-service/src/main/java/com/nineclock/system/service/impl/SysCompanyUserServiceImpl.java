package com.nineclock.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.nineclock.common.entity.PageResult;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.dto.SysDepartmentDTO;
import com.nineclock.system.dto.SysFunctionDTO;
import com.nineclock.system.dto.SysRoleDTO;
import com.nineclock.system.excel.ExcelMember;
import com.nineclock.system.excel.ExcelMemberListener;
import com.nineclock.system.mapper.*;
import com.nineclock.system.pojo.*;
import com.nineclock.system.service.SysCompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysCompanyUserServiceImpl implements SysCompanyUserService {
    @Autowired
    private SysCompanayUserMapper companayUserMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysFunctionMapper sysFunctionMapper;

    @Autowired
    private SysDepartmentMapper departmentMapper;

    @Autowired
    private SysUserMapper userMapper;


    /**
     * 根据系统用户ID、企业id查询员工列表
     */
    @Override
    public List<SysCompanyUserDTO> queryCompanyUserDTO(Long userId, Long companyId) {

        List<SysCompanyUserDTO> sysCompanyUserDTOlist = Lists.newArrayList();


        // 根据用户id、企业id查询员工列表
        LambdaQueryWrapper<SysCompanyUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysCompanyUser::getUserId, userId);
        if (companyId != null) {
            wrapper.eq(SysCompanyUser::getCompanyId, companyId);
        }
        List<SysCompanyUser> sysCompanyUserList = companayUserMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(sysCompanyUserList)) {
            throw new NcException(ResponseEnum.USER_NOT_JOIN_COMPANY);
        }

        // 数据转换，List<SysCompanyUser> --> List<SysCompanyUserDTO>
        List<SysCompanyUserDTO> companyUserDTOList = BeanHelper.copyWithCollection(sysCompanyUserList, SysCompanyUserDTO.class);

        // 封装员工对应的角色、权限信息
        for (SysCompanyUserDTO sysCompanyUserDTO : companyUserDTOList) {
            // 根据员工id查询对应角色、权限信息
            List<SysRole> sysRoleList = roleMapper.queryRoleByCompanyUserId(sysCompanyUserDTO.getId());
            // 数据转换，List<SysRole> --> List<SysRoleDTO>
            List<SysRoleDTO> sysRoleDTOList = BeanHelper.copyWithCollection(sysRoleList, SysRoleDTO.class);

            // 设置员工的角色信息
            sysCompanyUserDTO.setRoles(sysRoleDTOList);

            // TODO 同理，需要根据角色id，查询对应的权限信息，并设置


            for (SysRole sysRole : sysRoleList) {
                //拿到角色id
                Long roleId = sysRole.getId();
                //权限集合
                List<SysFunction> functionList = sysFunctionMapper.queryRoleIDFunction(roleId);
                //转成dto
                List<SysFunctionDTO> functionDTOList = BeanHelper.copyWithCollection(functionList, SysFunctionDTO.class);

                //设置进去
                sysCompanyUserDTO.setFunctions(functionDTOList);
            }


        }

        return companyUserDTOList;
    }

    /**
     * 企业管理: 获取企业当前主管理员
     * 接口路径：GET/sys/company/getCurrentAdmin
     */
    @Override
    public SysCompanyUserDTO getCurrentAdmin() {
        //获取当前企业的id
        Long companyId = CurrentUserHolder.get().getCompanyId();
        //根据企业id查询主管理员信息

        // 查询主管理员 -  主管理员角色名 ROLE_ADMIN_SYS
        SysCompanyUser companyUserAdmin = companayUserMapper.queryAdminCompanyUser(companyId);

        if (companyUserAdmin == null) {
            throw new NcException(ResponseEnum.COMPANY_ADMIN_NOT_EXISTS);
        }

        return BeanHelper.copyProperties(companyUserAdmin, SysCompanyUserDTO.class);

    }

    /**
     * 组织架构: 查询组织所有员工
     */
    @Override
    public List<SysCompanyUserDTO> queryCompanyMemberList() {

        //根据企业id查询组织所有员工
        Long companyId = CurrentUserHolder.get().getCompanyId();

        LambdaQueryWrapper<SysCompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCompanyUser::getCompanyId, companyId);
        //Enable 表示在职的 0表示可能被辞退的
        wrapper.eq(SysCompanyUser::getEnable, 1);
        List<SysCompanyUser> companyUserList = companayUserMapper.selectList(wrapper);

        //转格式
        return BeanHelper.copyWithCollection(companyUserList, SysCompanyUserDTO.class);


    }

    /**
     * 文档第一个接口
     * 根据员工ID查询企业员工信息
     */
    @Override
    public SysCompanyUserDTO queryCompanyUserById(Long companyUserId) {
        SysCompanyUser companyUser = companayUserMapper.selectById(companyUserId);
        return BeanHelper.copyProperties(companyUser, SysCompanyUserDTO.class);
    }

    /**
     * 查询当前企业员工列表
     * 接口路径：GET/sys/companyUser/queryAllUser
     *
     * @return
     */
    @Override
    public List<SysCompanyUserDTO> queryAllCompanyUser() {
        LambdaQueryWrapper<SysCompanyUser> lambdaQueryWrapper = new LambdaQueryWrapper<SysCompanyUser>();
        lambdaQueryWrapper.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId());
        lambdaQueryWrapper.eq(SysCompanyUser::getEnable, (short) 1);
        List<SysCompanyUser> companyUserList = companayUserMapper.selectList(lambdaQueryWrapper);

        return BeanHelper.copyWithCollection(companyUserList, SysCompanyUserDTO.class);
    }

    /**
     * 组织架构:PC-获取部门成员列表
     */
    @Override                                                   //部门id  //关键字
    public PageResult<SysCompanyUserDTO> queryMembers(Integer page, Integer pageSize, Long departmentId, String keyword) {
        LambdaQueryWrapper<SysCompanyUser> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId());
        //关键词查询
        if (departmentId != null) {
            // 迭代查询该部门及所有的子部门id
            List<Long> deptIdList = new ArrayList<>();
            deptIdList.add(departmentId);
            //创建方法查询所有的id
            queryAllChildDeparmentIds(deptIdList, departmentId);
            //部门id 属于 这个list集合
            wrapper1.in(SysCompanyUser::getDepartmentId, deptIdList);
        }
        if (!StringUtils.isEmpty(keyword)) {
            //模糊查询
            // and (user_name like xxx or work_number like xxx or mobile like xxx)
            wrapper1.and(w -> {
                w.like(SysCompanyUser::getUserName, keyword) //姓名
                        .or()
                        .like(SysCompanyUser::getWorkNumber, keyword) //工号
                        .or()
                        .like(SysCompanyUser::getMobile, keyword); //电话
            });


        }
        // 创建分页对象
        IPage<SysCompanyUser> ipage = new Page<>(page, pageSize);

        // 分页查询
        ipage = companayUserMapper.selectPage(ipage, wrapper1);
        //获取当前页数据
        List<SysCompanyUser> companyUserList = ipage.getRecords();
        if (!CollectionUtils.isEmpty(companyUserList)) {
            //使用流转换
            List<SysCompanyUserDTO> companyUserDTOList = companyUserList.stream().map(sysCompanyUser -> {
                SysCompanyUserDTO companyUserDTO = BeanHelper.copyProperties(sysCompanyUser, SysCompanyUserDTO.class);

                //SysDepartmentDTO 对象，接口文档需要
                SysDepartmentDTO departmentDTO = new SysDepartmentDTO();
                departmentDTO.setId(companyUserDTO.getId());
                departmentDTO.setName(companyUserDTO.getDepartmentName());
                return companyUserDTO;
            }).collect(Collectors.toList());
            return new PageResult<>(ipage.getTotal(), ipage.getPages(), companyUserDTOList);
        }
        return new PageResult<>(ipage.getTotal(), ipage.getPages(), null);
    }


    /**
     * 递归查询某部门下所有子部门的id
     */
    private void queryAllChildDeparmentIds(List<Long> deptIdList, Long departmentId) {
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getParentId, departmentId);   //上级部门ID =//部门id
        wrapper.eq(SysDepartment::getCompanyId, CurrentUserHolder.get().getCompanyId());
        List<SysDepartment> departmentList = departmentMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(departmentList)) {
            //遍历集合
            for (SysDepartment department : departmentList) {
                //将这个id 摄入  deptIdList集合
                deptIdList.add(department.getId());  // 添加部门id

                // 递归查询子部门的子部门
                queryAllChildDeparmentIds(deptIdList, department.getId());
            }

        }

    }

    /**
     * 组织架构: PC-直接导入员工数据
     */
    @Override
    public void uploadExcel(MultipartFile excelFile) throws IOException {
        // 健壮性判断
        if (excelFile == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //解析excelFile
        EasyExcel.read(excelFile.getInputStream(), ExcelMember.class,
                new ExcelMemberListener(this)).sheet().doRead();
    }

    /**
     * 处理解析完毕之后的员工数据, 进行数据组装及持久化操作
     *
     * @param memberList
     */
    @Override
    public void handleParsedData(List<ExcelMember> memberList) {
        for (ExcelMember excelMember : memberList) {
            //1.保存存入的表的信息
            //1.1 判断手机号 ，邮箱是否被注册，工号是否存在
            pendingMobileAndEmailAndWorkNumer(excelMember);
            // 1.2 保存用户数据
            SysUser sysUser = new SysUser();

            sysUser.setUsername(excelMember.getName());
            sysUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
            sysUser.setMobile(excelMember.getMobile());

            sysUser.setEmail(excelMember.getEmail());


            sysUser.setCreateTime(new Date());
            sysUser.setUpdateTime(new Date());
            sysUser.setStatus(excelMember.getEnable().equals("可用") ? (short) 1 : (short) 0);//用户状态
            sysUser.setLastLoginCompanyId(CurrentUserHolder.get().getCompanyId());//未次登录选择企业ID
            //存入用户表
            userMapper.insert(sysUser);
            // 2.保存企业员工数据
            SysCompanyUser companyUser = saveCompanyUser(excelMember, sysUser);
            // 3.维护企业员工关联的角色(传入companyUser添加角色)
            saveCompanyUserRole(companyUser, excelMember);
        }
    }

    /**
     * 维护企业员工关联的角色(传入companyUser添加角色)
     */
    private void saveCompanyUserRole(SysCompanyUser companyUser, ExcelMember excelMember) {


        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getCompanyId, CurrentUserHolder.get().getCompanyId());
        wrapper.eq(SysRole::getRoleName, excelMember.getRoleName());
        SysRole sysRole = roleMapper.selectOne(wrapper);

        if (sysRole != null) {
            //添加进去
            SysCompanyUserRole companyUserRole = new SysCompanyUserRole();
            companyUserRole.setCompanyUserId(CurrentUserHolder.get().getCompanyUserId());
            companyUserRole.setRoleId(sysRole.getId());
            companyUserRole.setCompanyId(companyUser.getCompanyId());

        }
    }

    /**
     * 保存企业员工数据
     * @param excelMember 文件内容
     * @param sysUser     刚才的用户
     */
    private SysCompanyUser saveCompanyUser(ExcelMember excelMember, SysUser sysUser) {
        SysCompanyUser sysCompanyUser = new SysCompanyUser();

        sysCompanyUser.setUserId(sysUser.getId());
        sysCompanyUser.setCompanyId(CurrentUserHolder.get().getCompanyId());
        sysCompanyUser.setCompanyName(CurrentUserHolder.get().getCompanyName());

        sysCompanyUser.setDepartmentName(excelMember.getDepartmentName());
        sysCompanyUser.setPost(excelMember.getPosition());//职位
        sysCompanyUser.setWorkNumber(excelMember.getWorkNumber());
        sysCompanyUser.setEmail(excelMember.getEmail());

        sysCompanyUser.setOfficeAddress(excelMember.getWorkAddr()); //办公地点
        sysCompanyUser.setTimeEntry(new Date());
        sysCompanyUser.setRemark(excelMember.getDesc()); //备注
        sysCompanyUser.setEnable(excelMember.getEnable().equals("可用") ? (short) 1 : (short) 0);
        sysCompanyUser.setCreateTime(new Date());
        sysCompanyUser.setUpdateTime(new Date());
        sysCompanyUser.setMobile(excelMember.getMobile());
        sysCompanyUser.setUserName(excelMember.getName());
        // 根据部门名称查询部门
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getCompanyId, CurrentUserHolder.get().getCompanyId());
        wrapper.eq(SysDepartment::getName, excelMember.getDepartmentName());//部门名称 = //部门
        SysDepartment department = departmentMapper.selectOne(wrapper);
        //设置部门
        if (department != null) {
            sysCompanyUser.setDepartmentId(department.getId());
            sysCompanyUser.setDepartmentName(department.getName());
        }
        companayUserMapper.insert(sysCompanyUser);
        return sysCompanyUser;
    }
    //1.1 判断手机号 ，邮箱是否被注册，工号是否存在
    private void pendingMobileAndEmailAndWorkNumer(ExcelMember excelMember) {

        //判断手机号 ，邮箱是否被注册
        LambdaQueryWrapper<SysCompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId());
        wrapper.and(w -> {
            w.eq(SysCompanyUser::getMobile, excelMember.getMobile()) //手机号
                    .or()
                    .eq(SysCompanyUser::getEmail, excelMember.getEmail()); //邮箱
        });
        Integer count = companayUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new NcException(ResponseEnum.USER_MOBILE_EXISTS);//手机被注册
        }
        //工号是否存在
        LambdaQueryWrapper<SysCompanyUser> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId());
        wrapper2.eq(SysCompanyUser::getWorkNumber, excelMember.getWorkNumber());
        Integer count2 = companayUserMapper.selectCount(wrapper2);
        if (count2 > 0) {
            throw new NcException(ResponseEnum.WROK_NUM_EXISTS);  //工号已经存在存在
        }

    }

    /**
     * 组织架构:APP-根据手机号获取员工信息
     */
    @Override
    public SysCompanyUserDTO findCompanyUserByMobile(String mobile) {

        if (mobile !=null){
            LambdaQueryWrapper<SysCompanyUser> userWrapper =new LambdaQueryWrapper<>();
            userWrapper.eq(SysCompanyUser::getMobile, mobile);
            userWrapper.eq(SysCompanyUser::getCompanyId,CurrentUserHolder.get().getCompanyId());
            SysCompanyUser sysCompanyUser = companayUserMapper.selectOne(userWrapper);

            return BeanHelper.copyProperties(sysCompanyUser,SysCompanyUserDTO.class);
        }
        return null;
    }
    /**
     * 根据部门ID集合查询部门下的员工数量
     */
    @Override
    public Integer queryUserCountByDepartmentIds(List<Long> departmentIds) {
        // 获取传递过来的部门及其所有的子部门
        List<Long> ids = new ArrayList<Long>();
        ids.addAll(departmentIds);

        for (Long departmentId : departmentIds) {
            /**
             * 递归查询某部门下所有子部门的id
             */
            this.queryAllChildDeparmentIds(ids, departmentId);
        }
        //组装条件查询用户数量
        LambdaQueryWrapper<SysCompanyUser> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(SysCompanyUser::getDepartmentId,ids);//部门id
        wrapper.eq(SysCompanyUser::getEnable, 1); //有效标记
        Integer count = companayUserMapper.selectCount(wrapper);

        return count;
    }

    /**
     * 根据企业ID查询企业的主管理员
     */
    @Override
    public SysCompanyUserDTO queryAdminByCompanyId(Long companyId) {
        SysCompanyUser sysCompanyUser = companayUserMapper.queryAdminCompanyUser(companyId);
        return BeanHelper.copyProperties(sysCompanyUser,SysCompanyUserDTO.class);
    }
    /**
     * 查询某企业的员工列表
     */
    @Override
    public List<SysCompanyUserDTO> queryAllUserByCompanyId(Long companyId) {
        LambdaQueryWrapper<SysCompanyUser> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysCompanyUser::getEnable, 1);
        wrapper.eq(SysCompanyUser::getCompanyId, companyId);
        List<SysCompanyUser> companyUserList = companayUserMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(companyUserList)) {
            throw new NcException(ResponseEnum.USER_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(companyUserList,SysCompanyUserDTO.class);
    }

}