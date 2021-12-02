package com.nineclock.approve.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nineclock.system.dto.SysUserDTO;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 审批实例节点
 */
@Data
@Accessors(chain = true)
@TableName("approve_inst_node")
public class ApproveInstNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // id

    private String nextNodeId;

    private String companyId; // 企业ID

    private String definitionId; // 审批定义ID

    private String definitionName; // 审批定义名称

    private String instId; // 审批实例ID

    private String nodeKey; // 节点KEY

    private String nodeType; // 节点类型

    private String oughtUserIds; // 应处理人IDs

    private String oughtUserNames; // 应处理人名称

    private String realUserIds; // 实际处理人IDs

    private String realUserNames; // 实际处理人名称

    private Date inTime; // 节点进入时间

    private Date startTime; // 节点处理开始时间

    private Date endTime; // 节点处理完成时间

    private String dataFrom; // 节点来源

    private String removeFlag; // 减签标志

    private Integer seq; // 序号

    private Date createTime; // 创建时间

    private Date updateTime; // 最后更新时间

    private Integer state;//当前节点审批状态  -1：发起审批，0：待审批，1：审批通过，2：审批回退，3：知会


    @TableField(exist = false)
    private List<SysUserDTO> oughtUsers; //应处理人集合 , 不参与数据库映射

    public ApproveInstNode() {
    }

    public ApproveInstNode(ApproveInst approveInst) {
        this.instId = approveInst.getId();
        this.companyId = approveInst.getCompanyId();
        this.definitionId = approveInst.getDefinitionId();
        this.definitionName = approveInst.getDefinitionName();
    }

    public ApproveInstNode(ApproveDefinition approveDefinition) {
        this.companyId = approveDefinition.getCompanyId();
        this.definitionId = approveDefinition.getId();
        this.definitionName = approveDefinition.getName();
        this.dataFrom = "1";
        this.removeFlag = "0" ;
        this.state = 0;
    }
}
