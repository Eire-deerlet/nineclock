package com.nineclock.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto implements Serializable {

    private List<DocFolderDto> folders = new ArrayList<DocFolderDto>(); //文件夹集合

    private List<DocFileDto> files = new ArrayList<DocFileDto>(); //文件集合

}
