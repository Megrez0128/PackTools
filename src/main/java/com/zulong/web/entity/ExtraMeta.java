package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExtraMeta {
    private int id;
    private int meta_id;
    private int group;
    private int version;
    private HashMap<String,Object> data;
}
