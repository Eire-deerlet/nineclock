package com.nineclock.document.service;


import com.nineclock.document.dto.DocFolderDto;

public interface DocFolderService {

    /**
     * 新增文件夹
     * @param folderDto
     */
    void insertFolder(DocFolderDto folderDto);
}