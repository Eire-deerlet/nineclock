package com.nineclock.approve.pojo;



import com.baomidou.mybatisplus.annotation.TableName;
import com.nineclock.common.entity.UserInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 审批实例
 */
@Data
@Accessors(chain = true)
@TableName("approve_inst")
public class ApproveInst implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // id

    private String instNo; // 审批编号

    private String companyId; // 企业ID

    private String departmentid; // 部门ID

    private String departmentname; // 部门名称

    private String definitionId; // 审批定义ID

    private String definitionName; // 审批定义名称

    private String state; // 实例状态 - 0: 未开始 , 1: 开始了未结束 , 2: 结束了

    private String applyUserId; // 发起人ID

    private String applyUserName; // 发起人名称

    private Date applyTime; // 发起时间

    private String lastNodeId; // 上一个节点ID

    private String lastNodeKey; // 上一个节点KEY

    private String lastNodeType; // 上一个节点类型

    private String lastNodeUserIds; // 上一个节点审批人IDs

    private String lastNodeUserNames; // 上一个节点审批人名称

    private Date lastNodeStartTime; // 上一个节点开始时间

    private Date lastNodeEndTime; // 上一个节点完成时间

    private String currNodeId; // 当前节点ID

    private String currNodeKey; // 当前节点KEY

    private String currNodeType; // 当前节点类型

    private String currNodeUserIds; // 当前节点应处理人IDs

    private String currNodeUserNames; // 当前节点应处理人名称

    private String currNodeRealUserIds; // 当前节点已处理人IDs

    private String currNodeRealUserNames; // 当前节点已处理人名称

    private String currNodeLeftUserIds; // 当前节点剩余处理人IDs

    private String currNodeLeftUserNames; // 当前节点剩余处理人名称

    private String endNodeId; // 结束节点ID

    private String endNodeKey; // 结束节点KEY

    private String endNodeType; // 结束节点类型

    private String endNodeUserIds; // 结束节点审批人IDs

    private String endNodeUserNames; // 结束节点审批人名称

    private Date endNodeStartTime; // 结束节点开始时间

    private Date endNodeEndTime; // 结束节点完成时间

    private Date createTime; // 创建时间

    private Date updateTime; // 最后更新时间

    private String processId; // 工作流实例ID

    private String hisUserIds; // 历史审批人IDs

    private String hisUserNames; // 历史审批人名称




    public ApproveInst() {
    }

    public ApproveInst(ApproveDefinition approveDefinition, UserInfo user) {
        this.definitionId = approveDefinition.getId();
        this.definitionName = approveDefinition.getName();
        this.companyId = approveDefinition.getCompanyId();
        this.applyTime = new Date();
        this.state = "0";

        //部门设置
        this.departmentid = String.valueOf(user.getDepartmentId());
        this.departmentname = user.getDepartmentName();

        // 发起节点：
        this.applyUserId = String.valueOf(user.getId());
        this.applyUserName = user.getUsername();
        this.createTime = new Date();
        this.updateTime = new Date();
    }



    //设置当前节点相关信息
    public void setCurrNode(ApproveInstNode startNode) {
        this.currNodeId = startNode.getId();
        this.currNodeKey = startNode.getNodeKey();
        this.currNodeType = startNode.getNodeType();
        this.currNodeUserIds = startNode.getOughtUserIds();
        this.currNodeUserNames = startNode.getOughtUserNames();
    }


    //当节点结束，设置相关信息
    public void setApproveInstCurrIsEnd(ApproveInstNode approveInstNode) {
        this.endNodeId = approveInstNode.getId();
        this.endNodeKey = approveInstNode.getNodeKey();
        this.endNodeType = approveInstNode.getNodeType();
        this.endNodeStartTime = approveInstNode.getStartTime();
        this.endNodeEndTime = approveInstNode.getEndTime();
        this.endNodeUserIds = approveInstNode.getRealUserIds();
        this.endNodeUserNames = approveInstNode.getRealUserNames();

        // 把当前节点记录到 实例记录表 的上一个节点中
        this.lastNodeId = approveInstNode.getId();
        this.lastNodeKey = approveInstNode.getNodeKey();
        this.lastNodeType = approveInstNode.getNodeType();
        this.lastNodeStartTime = approveInstNode.getStartTime();
        this.lastNodeEndTime = approveInstNode.getEndTime();
        this.lastNodeUserIds = approveInstNode.getRealUserIds();
        this.lastNodeUserNames = approveInstNode.getRealUserNames();

        // 把 实例记录表 的当前节点置位空
        this.currNodeId = null;
        this.currNodeKey = null;
        this.currNodeType = null;
        this.currNodeUserIds = null;
        this.currNodeUserNames = null;

        // 状态
        this.state = "2";
    }


    //当节点未结束，设置相关信息
    public void setApproveInstCurrIsEnd(ApproveInstNode approveInstNode,ApproveInstNode nextApproveInstNode) {
        this.endNodeId = null;
        this.endNodeKey = null;
        this.endNodeType = null;
        this.endNodeStartTime = null;
        this.endNodeEndTime = null;
        this.endNodeUserIds = null;
        this.endNodeUserNames = null;

        // 把当前节点记录到 实例记录表 的上一个节点中
        this.lastNodeId = approveInstNode.getId();
        this.lastNodeKey = approveInstNode.getNodeKey();
        this.lastNodeType = approveInstNode.getNodeType();
        this.lastNodeStartTime = approveInstNode.getStartTime();
        this.lastNodeEndTime = approveInstNode.getEndTime();
        this.lastNodeUserIds = approveInstNode.getRealUserIds();
        this.lastNodeUserNames = approveInstNode.getRealUserNames();

        // 把 nextApproveInstNode 记录到 实例记录表 的当前节点
        if (nextApproveInstNode != null) {
            this.currNodeId = nextApproveInstNode.getId();
            this.currNodeKey = nextApproveInstNode.getNodeKey();
            this.currNodeType = nextApproveInstNode.getNodeType();
            this.currNodeUserIds = nextApproveInstNode.getOughtUserIds();
            this.currNodeUserNames = nextApproveInstNode.getOughtUserNames();
        }

        // 状态
        this.state="1";
    }


}
