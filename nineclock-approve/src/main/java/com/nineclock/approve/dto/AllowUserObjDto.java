package com.nineclock.approve.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllowUserObjDto implements Serializable {

    private Long objectId;//对象ID

    private String objectName;//对象名称

    private String objectType;//对象类型

}
