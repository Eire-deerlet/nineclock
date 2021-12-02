package com.nineclock.document.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档管理模块-文件夹实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("doc_folder")
public class DocFolder implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//主键ID

    private Long companyId; //企业ID

    private Long parentId; //父目录ID

    private String name; //文件夹名称

    private Integer orderNum; //排序

    private Long createUserId; //创建人ID

    private Date createTime; //创建时间

    private Short status; //使用状态：1为可用，0为不可用
}