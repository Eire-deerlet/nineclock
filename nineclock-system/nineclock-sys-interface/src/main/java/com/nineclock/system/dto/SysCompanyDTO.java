package com.nineclock.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysCompanyDTO implements Serializable {

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

}
