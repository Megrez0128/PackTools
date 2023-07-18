package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {
    private int group_id;
    private String group_name;
}
// TODO: 待补充：在设置模板之后，初始化需要单独设置一个Group用于查看模板（但没有修改权限）