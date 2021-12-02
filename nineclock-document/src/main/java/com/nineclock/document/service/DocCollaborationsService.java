package com.nineclock.document.service;

import com.nineclock.common.entity.PageResult;
import com.nineclock.document.dto.DocUserCollaborationDTO;
import com.nineclock.document.dto.DocumentUserDTO;

import java.util.List;

public interface DocCollaborationsService {

    /**
     * 根据文件ID, 查找该文件的协作者列表数据
     */
    PageResult<DocumentUserDTO> pagingCollaborations(Long id, Integer pageSize, Integer page);

    /**
     * 根据文件ID获取员工列表
     * Tag ：文档服务
     * 接口路径：GET/document/pagingUsers
     */
    List<DocUserCollaborationDTO> queryUsers(Long fileId);
}