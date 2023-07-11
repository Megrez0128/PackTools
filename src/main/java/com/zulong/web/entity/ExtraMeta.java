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
    private int group_id;
    private int version;
    private HashMap<String,Object> data;
}
