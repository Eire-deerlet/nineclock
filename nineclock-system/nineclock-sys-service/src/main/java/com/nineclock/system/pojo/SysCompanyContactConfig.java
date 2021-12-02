package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_company_contact_config")
public class SysCompanyContactConfig {
    @TableId(type = IdType.AUTO)
    private Long id; //主键
    private String name; //字段名称
    private String type; //字段类型 : fixed为固定字段(不可删除)，dynamic为动态字段(可删除)
    private String fieldType; //字段类型 -- Integer, String
    private Short status; //状态: 1为可用 0为不可用
    private Long companyId; //企业ID
}
