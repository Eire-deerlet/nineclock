package com.nineclock.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nineclock.system.dto.SysCompanyUserDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DocFileDto implements Serializable {

    private Long id; //ID

    private Long folderId; //归属的文件夹ID

    private String name; //文件名

    private String filePath; //文件路径

    private String fileSize; //文件大小

    private Short status; //是否可用 1 可用, 0 禁用

    private String content; //文档内容

    private Short permission; //协作人文件权限 0 公共读 , 1 公共读写 ,  2 私有

    private Short type; //文档类型：1 本地文档 2 云文档

    private Long companyId; //企业ID

    private Long createUserId; //创建人id

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    private Long updateUserId; //修改人ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime; //修改时间




    private SysCompanyUserDTO creator;//作者
    private List<SysCompanyUserDTO> collaborations; //文件协作者集合

    private String userName;  //用户名
    private String updateUserName;  //最后更新人名称



}
