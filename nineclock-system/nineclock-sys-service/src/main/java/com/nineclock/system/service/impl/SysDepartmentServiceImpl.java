package com.nineclock.system.service.impl;



import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.system.dto.DepartmentOrUserSimpleDTO;
import com.nineclock.system.dto.SysDepartmentDTO;
import com.nineclock.system.mapper.SysCompanayUserMapper;
import com.nineclock.system.mapper.SysDepartmentMapper;
import com.nineclock.system.pojo.SysCompanyUser;
import com.nineclock.system.pojo.SysDepartment;
import com.nineclock.system.service.SysDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysDepartmentServiceImpl implements SysDepartmentService {

    @Autowired
    private SysDepartmentMapper departmentMapper;


    @Autowired
    private SysCompanayUserMapper companayUserMapper;

    /**
     * 组织架构: PC-获取组织架构列表(左侧菜单)
     */
    @Override
    public List<SysDepartmentDTO> querydepartment() {

        //查顶级部门
        LambdaQueryWrapper<SysDepartment> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getCompanyId, CurrentUserHolder.get().getCompanyId());
        wrapper.eq(SysDepartment::getParentId,0); //上级为0的部门
        List<SysDepartment> departmentList1 = departmentMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(departmentList1)){ //不为空
            // 数据转换 List<SysDepartment> --> List<SysDepartmentDTO>
            List<SysDepartmentDTO> departmentDTOList = BeanHelper.copyWithCollection(departmentList1, SysDepartmentDTO.class);
            // 递归查询子部门
            queryChildrenDepartmentList(departmentDTOList);
            return departmentDTOList;
        }
        return null;
    }


    /**
     * 迭代查询子部门
     * @param departmentDTOList
     */
    private void queryChildrenDepartmentList(List<SysDepartmentDTO> departmentDTOList) {

        for (SysDepartmentDTO departmentDTO : departmentDTOList) {
            // 根据部门id查询子部门
            //自己查自己 parentId =id
            LambdaQueryWrapper<SysDepartment> wrapper =new LambdaQueryWrapper<>();
            wrapper.eq(SysDepartment::getParentId,departmentDTO.getId());
            List<SysDepartment> departmentList2 = departmentMapper.selectList(wrapper);

            if (!CollectionUtils.isEmpty(departmentList2)){
                //第三层部门
                List<SysDepartmentDTO> childrenDepartmentDTOList =
                        BeanHelper.copyWithCollection(departmentList2, SysDepartmentDTO.class);
                // 迭代查询子部门的子部门
                queryChildrenDepartmentList(childrenDepartmentDTOList);
                // 设置子部门
                departmentDTO.setChildren(childrenDepartmentDTOList);
            }

        }
    }


    /**
     * 查询当前企业所有部门ID集合
     * @return
     */
    @Override
    public List<Long> queryDepartmentIds() {

        LambdaQueryWrapper<SysDepartment> wrapper =new LambdaQueryWrapper<>();

        wrapper.eq(SysDepartment::getCompanyId, CurrentUserHolder.get().getCompanyId());
        List<SysDepartment> departmentList = departmentMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(departmentList)){
            List<Long> idList = departmentList.stream().map(sysDepartment -> {
                return sysDepartment.getId();
            }).collect(Collectors.toList());
            return idList;
        }
        return  null;
    }
    /**
     * 组织架构:获取部门简单列表
     */
    @Override
    public List<DepartmentOrUserSimpleDTO> querySimleDepartment(Long id, Integer includeMember) {
        // 参数校验
        if (includeMember == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }

        List<DepartmentOrUserSimpleDTO> dtoList = new ArrayList<>();

        if (includeMember == 1) {
            // 查询指定部门下的员工
            LambdaQueryWrapper<SysCompanyUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysCompanyUser::getCompanyId, CurrentUserHolder.get().getCompanyId());
            wrapper.eq(SysCompanyUser::getEnable, 1);
            wrapper.eq(SysCompanyUser::getDepartmentId, id);
            List<SysCompanyUser> sysCompanyUserList = companayUserMapper.selectList(wrapper);

            if(!CollectionUtils.isEmpty(sysCompanyUserList)){
                for (SysCompanyUser companyUser : sysCompanyUserList) {
                    DepartmentOrUserSimpleDTO departmentOrUserSimpleDTO = new DepartmentOrUserSimpleDTO();
                    departmentOrUserSimpleDTO.setId(companyUser.getId());
                    departmentOrUserSimpleDTO.setName(companyUser.getUserName());
                    departmentOrUserSimpleDTO.setType(2);
                    dtoList.add(departmentOrUserSimpleDTO);
                }
            }
        }

        // 查询指定部门的子部门
        LambdaQueryWrapper<SysDepartment> deptWrapper = new LambdaQueryWrapper<>();
        deptWrapper.eq(SysDepartment::getCompanyId, CurrentUserHolder.get().getCompanyId());
        deptWrapper.eq(SysDepartment::getParentId, id);

        List<SysDepartment> departmentList = departmentMapper.selectList(deptWrapper);

        if(!CollectionUtils.isEmpty(departmentList)){
            for (SysDepartment department : departmentList) {
                DepartmentOrUserSimpleDTO departmentOrUserSimpleDTO = new DepartmentOrUserSimpleDTO();
                departmentOrUserSimpleDTO.setId(department.getId());
                departmentOrUserSimpleDTO.setName(department.getName());
                departmentOrUserSimpleDTO.setType(1);
                dtoList.add(departmentOrUserSimpleDTO);
            }
        }

        return dtoList;
    }
    /**
     * 根据员工id获得从低到高级别的部门ID集合
     */
    @Override
    public List<Long> queryDepartmentsByUserId(Long companyUserId) {
        List<Long> deptIdList = new ArrayList<>();

        // 根据员工id查询员工信息
        SysCompanyUser companyUser = companayUserMapper.selectById(companyUserId);
        if (companyUser != null) {
            // 根据部门id依次查询上级部门id集合
            getAllParentDeptIdWithSortById(companyUser.getDepartmentId(), deptIdList);
        }

        return deptIdList;
    }

    /**
     *  根据部门id依次查询上级部门id集合
     * @param departmentId
     * @param deptIdList
     */
    private void getAllParentDeptIdWithSortById(Long departmentId, List<Long> deptIdList) {
        deptIdList.add(departmentId);
        // 根据部门id查询部门信息
        SysDepartment department = departmentMapper.selectById(departmentId);
        if (department != null && department.getParentId() != 0) {
            getAllParentDeptIdWithSortById(department.getParentId(), deptIdList);
        }
    }
}
