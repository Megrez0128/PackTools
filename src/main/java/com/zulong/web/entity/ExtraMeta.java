package com.zulong.web.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExtraMeta {
    private int id;
    private int meta_id;
    private int group;
    private int version;
    private JSONObject data;

}
