package com.nineclock.approve.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 审批实例记录
 */
@Data
@Accessors(chain = true)
@TableName("approve_inst_record")
public class ApproveInstRecord implements Serializable {

    private String id; // id

    private String companyId; // 企业ID

    private String definitionId; // 审批定义ID

    private String definitionName; // 审批定义名称

    private String instId; // 审批实例ID

    private String instNodeId; // 审批实例节点ID

    private String nodeKey; // 节点KEY

    private String nodeType; // 节点类型

    private String handleType; // 处理类型

    private String handleUserId; // 处理人ID

    private String handleUserName; // 处理人名称

    private Date handleTime; // 处理时间

    private String handleOpinion; // 处理意见

    private String backToNodeId; // 审批-回退至节点ID

    private String countersignUserIds; // 审批-会签人员IDs

    private String countersignUserNames; // 审批-会签人员名称

    private String addsignUserIds; // 审批-加签人员IDs

    private String addsignUserNames; // 审批-加签人员名称

    private String addsignNodeType; // 审批-加签节点权限

    private String addsignNodeMode; // 审批-加签流程模式

    private String notifyUserIds; // 审批-只会人员IDs

    private String notifyUserNames; // 审批-只会人员名称

    private String trunOverUserId; // 审批-转交人员ID

    private String trunOverUserName; // 审批-转交人员名称

    private String notifiedFlag; // 知会-知会标志

    private Date createTime; // 创建时间

    private Date updateTime; // 最后更新时间


    public ApproveInstRecord() {
    }

    public ApproveInstRecord(ApproveInst approveInst, ApproveInstNode approveInstNode,String handleType, String handleOpinion,Long userId, String userName) {
        this.companyId = approveInst.getCompanyId();
        this.definitionId = approveInst.getDefinitionId();
        this.definitionName = approveInst.getDefinitionName();
        this.instId = approveInst.getId();
        this.instNodeId = approveInstNode.getId();
        this.nodeKey = approveInstNode.getNodeKey();
        this.nodeType = approveInstNode.getNodeType();
        this.handleTime = new Date();
        this.handleType = handleType;
        this.handleOpinion = handleOpinion;
        this.handleUserId = String.valueOf(userId);
        this.handleUserName = userName;
    }

}
