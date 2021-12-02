package com.nineclock.document.controller;

import com.nineclock.common.entity.PageResult;
import com.nineclock.common.entity.Result;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.document.dto.*;
import com.nineclock.document.service.DocCollaborationsService;
import com.nineclock.document.service.DocFileService;
import com.nineclock.document.service.DocFolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "APP文件管理接口", tags = "文档服务")
public class DocumentController {

    @Autowired
    private DocFileService docFileService;

    @Autowired
    private DocFolderService docFolderService;


    @Autowired
    private DocCollaborationsService docCollaborationsService;

    /**
     * 查询当前目录下的文件夹及文件
     * 接口路径：GET/document/listFolderAndFile
     */
    @GetMapping("/listFolderAndFile")
    @ApiOperation("查询当前目录下的文件夹及文件")                                        //父文件夹ID
    public Result<DocumentDto> queryFolderAndFile(@RequestParam(defaultValue = "0") Long parentFoldId) {
        DocumentDto documentDto = docFileService.queryFolderAndFile(parentFoldId);
        return Result.success(documentDto);
    }

    /**
     * 新增文件夹
     * 接口路径：POST/document/insertFolder
     */
    @ApiOperation("新增文件夹")
    @PostMapping("insertFolder")
    public Result insertFolder(@RequestBody DocFolderDto docFolderDto) {
        docFolderService.insertFolder(docFolderDto);
        return Result.success(ResponseEnum.SUCCESS);
    }

    /**
     * 根据文档ID查询文档
     * 接口路径：GET/document/getFileByFileId
     */
    @ApiOperation("根据文档ID查询文档")
    @GetMapping("/getFileByFileId")
    public Result<DocFileDto> getFileByFileId(@RequestParam("id") Long fileId) {
        DocFileDto docFileDto = docFileService.getFileByFileId(fileId);
        return Result.success(docFileDto);
    }

    /**
     * 查询文档协作者列表 需要分页
     * 接口路径：GET/document/pagingCollaborations
     */
    @ApiOperation("查询文档协作者列表")
    @GetMapping("pagingCollaborations")
    public Result<PageResult<DocumentUserDTO>> pagingCollaborations(@RequestParam Long id,
                                                                    @RequestParam(defaultValue = "1") Integer page,
                                                                    @RequestParam(defaultValue = "7") Integer pageSize) {
        PageResult<DocumentUserDTO> result=   docCollaborationsService.pagingCollaborations(id,pageSize,page);
    return Result.success(result);
    }

    /**
     * 根据文件ID获取员工列表
     * Tag ：文档服务
     * 接口路径：GET/document/pagingUsers
     */
    @ApiOperation("根据文件ID获取员工列表")
    @GetMapping("pagingUsers")
    public Result<List<DocUserCollaborationDTO>> queryUsers(@RequestParam("id") Long fileId) {
        List<DocUserCollaborationDTO> list = docCollaborationsService.queryUsers(fileId);
        return Result.success(list);

    }
}