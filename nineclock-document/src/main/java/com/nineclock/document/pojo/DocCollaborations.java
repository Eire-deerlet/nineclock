package com.nineclock.document.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("doc_collaborations")
public class DocCollaborations implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; //主键ID

    private Long fileId; //文件ID

    private Long collaborationId; //协作者ID
}
