package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flowchart {
    private int id;
    private int version;
    private String state;
    private String name;
    private String des;
    private String last_build;
    //TODO: 目前还不知道inner的属性以及怎么使用
    private boolean inner;

    public void setLast_build(String last_build) {
        this.last_build = last_build;
    }
}
