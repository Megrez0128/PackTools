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
    private boolean inner;
}
