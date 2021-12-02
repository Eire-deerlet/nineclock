package com.nineclock.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.common.constant.NcConstant;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.system.dto.SysCompanyContactConfigDTO;
import com.nineclock.system.mapper.SysCompanyContactConfigMapper;
import com.nineclock.system.pojo.SysCompanyContactConfig;
import com.nineclock.system.service.SysCompanyContactConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional //事务
public class SysCompanyContactConfigServiceImpl implements SysCompanyContactConfigService {

    @Autowired
    private SysCompanyContactConfigMapper configMapper;

    @Override
    public void addCompanyContact(SysCompanyContactConfigDTO configDTO) {
       // int i=1/0;
        // 参数校验全都不为空
        if (configDTO == null || StringUtils.isEmpty(configDTO.getName()) ||
                StringUtils.isEmpty(configDTO.getType())) {
            //ResponseEnum类
            //throw new RuntimeException(ResponseEnum.INVALID_PARAM_ERROR.getMessage());
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);


        }

        // TODO 公司ID，先固定写，以后再改 创建对象，并插入数据
        Long companyId = 1L; // TODO 公司ID，先固定写，以后再改


        SysCompanyContactConfig contactConfig = new SysCompanyContactConfig();
        contactConfig.setName(configDTO.getName());
        contactConfig.setType(configDTO.getType());
        contactConfig.setStatus((short) 1);
        contactConfig.setCompanyId(companyId);
        configMapper.insert(contactConfig);
    }

    /**
     * 企业通讯录: 查询并初始化
     *
     * @return
     */
    @Override
    public List<SysCompanyContactConfigDTO> queryCompanyContactConfig() {


        //2.生成wrapper
        LambdaQueryWrapper<SysCompanyContactConfig> wrapper = new LambdaQueryWrapper<>();


        // TODO 公司id先写死为1，后续再改造
        Long companyId = 1L;
        //3.lamaba 表达式 得到公司id
        wrapper.eq(SysCompanyContactConfig::getCompanyId, companyId);
        //1.查询参数
        List<SysCompanyContactConfig> configList = configMapper.selectList(wrapper);

        //如果数据不存在，进行初始化
        if (CollectionUtils.isEmpty(configList)) {
            //新建一个configList集合
            configList = new ArrayList<>();
            //遍历 ，默认需要的企业通讯录
            for (String fieldName : NcConstant.COMPANY_CONFIG_CONTACT_LIST) {
                //创建实体类对象写入数据
                SysCompanyContactConfig config = new SysCompanyContactConfig();


                config.setName(fieldName);
                //设为固定字段
                config.setType("fixed");

                //设为1可以使用
                config.setStatus((short)1);
                //企业id
                config.setCompanyId(companyId);

                //添加进去
                configMapper.insert(config);


            }
        }
        // 3. 如果存在对象拷贝工具类 ，返回结果数据转换，
        // 将List<SysCompanyContactConfig> 转为 List<SysCompanyContactConfigDTO>
        List<SysCompanyContactConfigDTO> dtos = BeanHelper.copyWithCollection(configList, SysCompanyContactConfigDTO.class);
        return dtos;
    }
    /**
     * 企业通讯录: 修改状态
     */
    @Override
    public void updateCompanyContactConfigStatus(Long id, Short status) {
        //如果数据都没有传入 无效的请求参数！
        if (id == null || status == null){
            throw new RuntimeException(ResponseEnum.INVALID_PARAM_ERROR.getMessage());
        }
        //创建一个实体类
        SysCompanyContactConfig contactConfig = new SysCompanyContactConfig();
        contactConfig.setId(id);
        contactConfig.setStatus(status);

        configMapper.updateById(contactConfig);
    }

    /**
     * 企业通讯录: 删除
     * @param id
     */
    @Override
    public void deleteCompanyContactConfig(Long id) {
        configMapper.deleteById(id);
    }
}