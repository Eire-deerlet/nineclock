package com.nineclock.attendance.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("at_attend_group_part")
public class AttendGroupPart implements Serializable {

    //主键ID
    @TableId(type = IdType.AUTO)
    private Long id;

    //考勤组ID
    private Long attendGroupId;

    //1部门 2用户
    private Integer objectType;

    //部门ID 或者是用户ID
    private Long objectId;

    //是否参加 1：参加  2：不参加
    private Integer attendType;
}