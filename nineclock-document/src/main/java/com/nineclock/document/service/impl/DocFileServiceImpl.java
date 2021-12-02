package com.nineclock.document.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nineclock.common.filter.CurrentUserHolder;
import com.nineclock.common.utils.BeanHelper;
import com.nineclock.document.dto.DocFileDto;
import com.nineclock.document.dto.DocFolderDto;
import com.nineclock.document.dto.DocumentDto;
import com.nineclock.document.enums.DocumentPermissionEnum;
import com.nineclock.document.mapper.DocFileMapper;
import com.nineclock.document.mapper.DocFolderMapper;
import com.nineclock.document.pojo.DocFile;
import com.nineclock.document.pojo.DocFolder;
import com.nineclock.document.service.DocFileService;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.feign.SysCompanyUserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocFileServiceImpl implements DocFileService {
    //文件mapper
    @Autowired
    private DocFileMapper docFileMapper;
    //文件夹mapper
    @Autowired
    private DocFolderMapper docFolderMapper;

    @Autowired
    private SysCompanyUserFeign companyUserFeign;


    /**
     * 查询当前目录下的文件夹及文件
     * 参数  //父文件夹ID
     */
    @Override
    public DocumentDto queryFolderAndFile(Long parentFoldId) {
        // 查询当前登录用户的企业id
        Long companyId = CurrentUserHolder.get().getCompanyId();
        //查询所有的文件
        List<DocFileDto> files = queryFiles(companyId, parentFoldId);
        //查询所有的文件夹
        List<DocFolderDto> folders = queryFolders(companyId, parentFoldId);

        return  new  DocumentDto(folders,files);
    }


    /**
     * 查询指定目录下的文件列表（具有权限的）
     *
     * @param parentFoldId
     * @param companyId
     * @return
     */
    private List<DocFileDto> queryFiles(Long companyId, Long parentFoldId) {
        //定义总集合
        List<DocFile> docFileList = new ArrayList<>();

        //1.查询文件是公共读，公共读写
        LambdaQueryWrapper<DocFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DocFile::getCompanyId, companyId); //为企业id
        wrapper.eq(DocFile::getFolderId, parentFoldId); //传入的父文件夹
        wrapper.eq(DocFile::getStatus, 1); //为没有禁用
        //查询文件是公共读，公共读写
        wrapper.in(DocFile::getPermission, DocumentPermissionEnum.ALL_READ.getPermission(),
                DocumentPermissionEnum.ALL_READ_WRITE.getPermission());
        //集合一 ：存公共读，公共读写
        List<DocFile> fileList1 = docFileMapper.selectList(wrapper);

        if (!CollectionUtils.isEmpty(fileList1)) {
            //如果不为空，就存入总集合
            docFileList.addAll(fileList1);
        }

        // 2.查询当前用户私有的文件
        LambdaQueryWrapper<DocFile> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(DocFile::getCompanyId, companyId); //为企业id
        wrapper1.eq(DocFile::getFolderId, parentFoldId); //传入的父文件夹
        wrapper1.eq(DocFile::getStatus, 1); //为没有禁用
        //私有
        wrapper1.eq(DocFile::getPermission, DocumentPermissionEnum.PRIVATE_READ_WRITE);
        //因为私有需要创建人的客户端id做标记
        wrapper1.eq(DocFile::getCreateUserId, CurrentUserHolder.get().getClientId());
        //集合二 ：私有的文件
        List<DocFile> fileList2 = docFileMapper.selectList(wrapper1);
        if (!CollectionUtils.isEmpty(fileList2)) {
            docFileList.addAll(fileList2);
        }

        //3.协作者


        List<DocFile> fileList3 = docFileMapper.selectFileListByCollaboration(companyId, parentFoldId,
                CurrentUserHolder.get().getCompanyUserId());
        if (!CollectionUtils.isEmpty(fileList3)) {
            docFileList.addAll(fileList3);
        }

        // 文件可能重复，例如当前用户既是协作者，又是公共读
        Set<DocFile> docFileHashSet = new HashSet<>(docFileList);

        // 数据转换，HashSet<DocFile> ---> Set<DocFileDto>
        Set<DocFileDto> docFileDtos = BeanHelper.copyWithCollection(docFileHashSet, DocFileDto.class);

        // 流操作，意思是遍历set集合，将每个元素进行处理后，最后转成List集合
        List<DocFileDto> fileDtoList = docFileDtos.stream().map(docFileDto -> {

            // 调用系统微服务，根据更新用户id查询用户名称
            SysCompanyUserDTO companyUserDTO = companyUserFeign.queryCompanyUserById(docFileDto.getUpdateUserId()).getData();
            if (companyUserDTO != null) {
                docFileDto.setUpdateUserName(companyUserDTO.getUserName());
            }

            return docFileDto;

        }).collect(Collectors.toList());

        return fileDtoList;
    }

    /**
     * 查询指定目录下的子文件夹列表
     *
     * @param parentFoldId
     * @param companyId
     * @return
     */
    private List<DocFolderDto> queryFolders(Long companyId, Long parentFoldId) {

        LambdaQueryWrapper<DocFolder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DocFolder::getCompanyId, companyId);
        wrapper.eq(DocFolder::getParentId, parentFoldId);
        wrapper.eq(DocFolder::getStatus, 1);

        List<DocFolder> docFolderList = docFolderMapper.selectList(wrapper);

        return BeanHelper.copyWithCollection(docFolderList, DocFolderDto.class);

    }


    /**
     * 根据文档ID查询文档
     * 接口路径：GET/document/getFileByFileId
     */
    @Override
    public DocFileDto getFileByFileId(Long fileId) {
        DocFile docFile = docFileMapper.selectById(fileId);
        DocFileDto docFileDto = BeanHelper.copyProperties(docFile, DocFileDto.class);
        return docFileDto;
    }

}
