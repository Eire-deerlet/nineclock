package com.nineclock.document.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nineclock.document.pojo.DocFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DocFileMapper extends BaseMapper<DocFile> {

    /**
     * 查询某用户为协作者的文件列表
     * @return
     */
    @Select("select f.* from doc_file f where f.company_id = #{companyId} " +
            "and f.folder_id = #{folderId} " +
            "and f.`status` = 1 and f.id in " +
            "(select c.file_id from doc_collaborations c where c.collaboration_id = #{collaborationId})")
    public List<DocFile> selectFileListByCollaboration(@Param("companyId") Long companyId,
                                                       @Param("folderId") Long folderId,
                                                       @Param("collaborationId") Long collaborationId);
}
