package com.nineclock.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocUserCollaborationDTO implements Serializable {

    //用户id
    private Long id;

    //用户姓名
    private String username;

    //状态 0既不是拥有者也不是协作者 1是拥有者 2是协作者
    private int state;

    //用户头像
    private String imgUrl;

    //手机号
    private String phone;

}