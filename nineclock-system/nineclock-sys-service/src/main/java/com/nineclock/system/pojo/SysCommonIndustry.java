package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *     行业字典表 企业-行业字典表
 * </p>
 */
@Data
@TableName("sys_common_industry")
public class SysCommonIndustry implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id; //主键
    private String name;//行业名称
    private String parentId; //父行业id
    private Short status; //使用状态：1为可用，0为不可用

}
