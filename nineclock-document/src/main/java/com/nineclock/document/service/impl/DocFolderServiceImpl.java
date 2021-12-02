package com.nineclock.document.service.impl;
import java.util.Date;

import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.document.dto.DocFolderDto;
import com.nineclock.document.mapper.DocFolderMapper;
import com.nineclock.document.pojo.DocFolder;
import com.nineclock.document.service.DocFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocFolderServiceImpl implements DocFolderService {

    @Autowired
    private DocFolderMapper folderMapper;

    /**
     * 新增文件夹
     * 接口路径：POST/document/insertFolder
     */
    @Override
    public void insertFolder(DocFolderDto folderDto) {
        if (folderDto == null){
            throw new NcException(ResponseEnum.INVALID_FILE_TYPE);
        }

        DocFolder docFolder = new DocFolder();

        docFolder.setCompanyId(CurrentUserHolder.get().getCompanyId()); //企业id
        docFolder.setParentId(folderDto.getParentId()); //父目录id
        docFolder.setName(folderDto.getName()); //文件名

        docFolder.setCreateUserId(CurrentUserHolder.get().getCompanyUserId()); //创建人id
        docFolder.setCreateTime(new Date());
        docFolder.setStatus((short)1); //状态

        folderMapper.insert(docFolder);

    }
}