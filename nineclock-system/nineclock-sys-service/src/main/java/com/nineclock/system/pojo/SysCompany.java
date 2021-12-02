package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *   企业信息
 * </p>
 */
@Data
@TableName("sys_company")
public class SysCompany implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //主键ID
    private String name; //企业全称 全库唯一性校验
    private String nameAbbr; //企业名称缩写
    private Long industryId; //行业id
    private String industryName; //行业名称
    private String companyScale; //人员规模
    private String locationCode; //所在地行政区域编码（区县级）
    private String locationName; //行政区划名称
    private String logo; //logo
    private Short auditStatus; //状态 1有效、2注销
    private Long mainManagerId; //主管理员id
    private Long userId; //创建人id
    private Date createTime; //创建时间

}
