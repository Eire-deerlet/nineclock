package com.nineclock.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.approve.dto.ApproveDefinitionBaseDataDto;
import com.nineclock.approve.dto.ApproveDefinitionDto;
import com.nineclock.approve.dto.ApproveGroupDefinitionDto;
import com.nineclock.approve.mapper.ApproveDefinitionMapper;
import com.nineclock.approve.pojo.ApproveDefinition;
import com.nineclock.approve.service.ApproveDefinitionService;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ApproveDefinitionServiceImpl implements ApproveDefinitionService {

    @Autowired
    private ApproveDefinitionMapper approveDefinitionMapper;


    /**
     * 流程定义: 查询列表
     */
    @Override
    public List<ApproveGroupDefinitionDto> queryApproveGroupDefinitio() {
        // 1.获取登录用户的企业id
        Long companyId = CurrentUserHolder.get().getCompanyId();

        LambdaQueryWrapper<ApproveDefinition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApproveDefinition::getCompanyId, companyId);
        wrapper.eq(ApproveDefinition::getIsValid, 1);  // 有效标记
        List<ApproveDefinition> definitionList = approveDefinitionMapper.selectList(wrapper);

        // 3.封装返回结果
        // 3.1 按照分类进行分组

        if (!CollectionUtils.isEmpty(definitionList)) {
            // 审批类型 -(1:出勤休假, 2:财务, 3:人事, 4:行政, 5:其他)
            //key:为分类 groupType  value:审批定义 definitionList
            Map<String, List<ApproveDefinition>> map = definitionList.stream().collect(Collectors.groupingBy(ApproveDefinition::getGroupType));
            List<ApproveGroupDefinitionDto> definitionDtoList = new ArrayList<>();

            // 遍历所有的key
            for (String groupType : map.keySet()) {
                //批流程返回数据对象
                ApproveGroupDefinitionDto definitionDto = new ApproveGroupDefinitionDto();
                definitionDto.setGroupType(groupType);
                definitionDto.setDefinitionList(map.get(groupType));

                definitionDtoList.add(definitionDto);

            }
            return definitionDtoList;
        }
        return null;
    }
    /**
     * 流程定义: 新增/修改
     */

    @Override
    public void saveApproveDefinition(ApproveDefinitionDto approveDefinitionDto) {
        //健壮性判断
        if (approveDefinitionDto == null || approveDefinitionDto.getBaseData() == null ||
                approveDefinitionDto.getTableData() == null || approveDefinitionDto.getFlowData() == null) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        // 2.保存流程定义信息
        ApproveDefinitionBaseDataDto baseData = approveDefinitionDto.getBaseData();
        // 流程id为空，即新增
        if (StringUtils.isEmpty(baseData.getId())){

        }
    }
}
