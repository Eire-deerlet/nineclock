package com.nineclock.approve.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 审批表单字段信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnObjDto implements Serializable {

    //字段主键
    private String fieldKey;

    //组件名称
    private String lab;

    //标题
    private String title;

    //有效标记
    private String isValid;

}
