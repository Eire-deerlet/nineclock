package com.nineclock.document.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档管理模块-文件实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("doc_file")
public class DocFile implements Serializable {

    @TableId(type = IdType.AUTO)
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

    private Date createTime; //创建时间

    private Long updateUserId; //修改人ID

    private Date updateTime; //修改时间
}