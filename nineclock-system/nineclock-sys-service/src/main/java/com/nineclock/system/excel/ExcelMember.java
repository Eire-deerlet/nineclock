package com.nineclock.system.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class ExcelMember {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("部门")
    private String departmentName;

    @ExcelProperty("职位")
    private String position;

    @ExcelProperty("工号")
    private String workNumber;

    @ExcelProperty("是否部门主管")
    private String isManager;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("办公地点")
    private String workAddr;

    @ExcelProperty("备注")
    private String desc;

    @ExcelProperty("入职时间")
    private Date entryTime;

    @ExcelProperty("激活状态")
    private String enable;

    @ExcelProperty("角色")
    private String roleName;
}