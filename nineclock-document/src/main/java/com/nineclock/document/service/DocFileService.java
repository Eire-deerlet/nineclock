package com.nineclock.document.service;

import com.nineclock.document.dto.DocFileDto;
import com.nineclock.document.dto.DocFolderDto;
import com.nineclock.document.dto.DocumentDto;

public interface DocFileService {
    /**
     * 根据父目录ID, 查询文件夹及文件信息
     * @param parentFoldId
     * @return
     */
    public DocumentDto queryFolderAndFile(Long parentFoldId);
    /**
     * 根据文档ID查询文档
     * 接口路径：GET/document/getFileByFileId
     */
    DocFileDto getFileByFileId(Long fileId);
}
