package com.nineclock.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档管理模块-文件历史操作记录
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocFileHistoryDto implements Serializable {

    private Long id; //主键ID

    private Long fileId; //文件ID

    private String fileName; //文件名

    private Long operateUserId; //操作人ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime; //操作时间

    private Short isCreate; //是否是创建文件 1代表创建 0代表编辑

    private String content; //文档内容




    private String userName;  //员工姓名

}