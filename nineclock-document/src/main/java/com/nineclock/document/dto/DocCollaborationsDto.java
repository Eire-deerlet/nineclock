package com.nineclock.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocCollaborationsDto implements Serializable {

    private Long fileId; //文件ID
    private Long userId; //协作者ID

}
