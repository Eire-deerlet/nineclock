package com.nineclock.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *    操作日志记录表
 * </p>
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //主键
    private String module; //模块名称
    private String operation; //描述
    private Long userId; //操作用户ID
    private String requestJson; //请求参数
    private String responseJson; //返回参数
    private Date createTime; //创建时间

}
