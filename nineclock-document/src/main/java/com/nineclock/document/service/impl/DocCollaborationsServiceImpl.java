package com.nineclock.document.service.impl;


import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nineclock.common.entity.PageResult;
import com.nineclock.document.dto.DocUserCollaborationDTO;
import com.nineclock.document.dto.DocumentUserDTO;
import com.nineclock.document.mapper.DocCollaborationsMapper;
import com.nineclock.document.mapper.DocFileMapper;
import com.nineclock.document.pojo.DocCollaborations;
import com.nineclock.document.pojo.DocFile;
import com.nineclock.document.service.DocCollaborationsService;
import com.nineclock.system.dto.SysCompanyUserDTO;
import com.nineclock.system.feign.SysCompanyUserFeign;
import com.nineclock.system.feign.SysUserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocCollaborationsServiceImpl implements DocCollaborationsService {

    @Autowired
    private DocCollaborationsMapper collaborationsMapper;

    @Autowired  //两个feign 都可以
    private SysCompanyUserFeign companyUserFeign;

    @Autowired
    private DocFileMapper fileMapper;

    @Autowired //两个feign 都可以
    private  SysUserFeign sysUserFeign;


    /**
     * 根据文件ID, 查找该文件的协作者列表数据
     * DocumentUserDTO 协作者
     * <p>
     * PageResult 分页
     */
    @Override                                               //参数1：文档id  参数2：页大小（多少页） 参数3：页码
    public PageResult<DocumentUserDTO> pagingCollaborations(Long id, Integer pageSize, Integer page) {

        // 1.分页查询协作者列表
        // 1.1 创建分页对象，设置分页参数
        IPage<DocCollaborations> ipage = new Page<>(page, pageSize);

        LambdaQueryWrapper<DocCollaborations> wrapper = new LambdaQueryWrapper<>();
        //参数1：文档id
        wrapper.eq(DocCollaborations::getFileId, id);
        // 1.3 执行查询，获取分页查询结果
        ipage = collaborationsMapper.selectPage(ipage, wrapper);
        // 2.封装结果
        // 2.1 获取当前页的数据
        List<DocCollaborations> collaborations = ipage.getRecords();


        // 2.2 数据转换，List<DocCollaborations> ---> List<DocumentUserDTO>
        List<DocumentUserDTO> documentUserDTOList = new ArrayList<>();


        //documentUserDTOList 作为返回值接收
        if (!CollectionUtils.isEmpty(collaborations)) {
           documentUserDTOList = collaborations.stream().map(docCollaborations -> {
                DocumentUserDTO documentUserDTO = new DocumentUserDTO();

                documentUserDTO.setId(docCollaborations.getCollaborationId());//设置协作者
                // 根据协作者id调用系统微服务查询协作者信息
                SysCompanyUserDTO companyUserDTO = companyUserFeign.queryCompanyUserById(docCollaborations.getCollaborationId()).getData();
                if (companyUserDTO != null) {
                    documentUserDTO.setUserName(companyUserDTO.getUserName());
                }
                return documentUserDTO;

            }).collect(Collectors.toList());
        }

        return new PageResult<>(ipage.getTotal(),ipage.getPages(),documentUserDTOList);
    }
    /**
     * 根据文件ID获取员工列表
     * Tag ：文档服务
     * 接口路径：GET/document/pagingUsers
     */
    @Override
    public List<DocUserCollaborationDTO> queryUsers(Long fileId) {
        // 调用系统微服务查询员工列表
        List<SysCompanyUserDTO> companyUserDTOList = companyUserFeign.queryAllCompanyUser().getData();
        // 根据文档id查询协作者信息
        LambdaQueryWrapper<DocCollaborations> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DocCollaborations::getFileId,fileId);
        List<DocCollaborations> docCollaborationsList = collaborationsMapper.selectList(wrapper);

        // 获取所有协作者的id集合
        List<Long> idList = docCollaborationsList.stream().map(docCollaborations -> {
            return docCollaborations.getCollaborationId();
        }).collect(Collectors.toList());

        // 获取文件信息
        DocFile docFile = fileMapper.selectById(fileId);

        // 数据封装
        List<DocUserCollaborationDTO> collaborationDTOList = companyUserDTOList.stream().map(sysCompanyUserDTO -> {
            DocUserCollaborationDTO userCollaborationDTO = new DocUserCollaborationDTO();
            userCollaborationDTO.setId(sysCompanyUserDTO.getId()); //用户id
            userCollaborationDTO.setUsername(sysCompanyUserDTO.getUserName()); //用户姓名
            userCollaborationDTO.setImgUrl(sysCompanyUserDTO.getImageUrl()); //用户头像
            userCollaborationDTO.setPhone(sysCompanyUserDTO.getMobile());      //用户手机号

            if (docFile.getCreateUserId() == sysCompanyUserDTO.getId()) {
                //如果文件创建人id 等于该员工id 拥有者
                userCollaborationDTO.setState(1);
            } else if (idList.contains(sysCompanyUserDTO.getId())) {
                //如果协作者的id集合中包含该员工的 id  协作者
                userCollaborationDTO.setState(2);
            } else {
                //0既不是拥有者也不是协作者
                userCollaborationDTO.setState(0);
            }
            return userCollaborationDTO;
        }).collect(Collectors.toList());

        return collaborationDTOList;
    }
}
