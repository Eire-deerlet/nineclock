package com.nineclock.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DocFolderDto implements Serializable {

    private Long id;//主键ID

    private Long companyId; //企业ID

    private Long parentId; //父目录ID

    private String name; //文件夹名称

    private Integer orderNum; //排序

    private Long createUserId; //创建人ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    private Short status; //使用状态：1为可用，0为不可用

}
