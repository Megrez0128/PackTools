package com.zulong.web.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flow {
    private int record_id;

    private int flow_id;
    private int version;

    private String name;
    private String des;
    private String last_build;
    private int core_meta_id;
    private int extra_meta_id;

    private JSONObject graph_data;
    private JSONObject blackboard;

    public Flow(int record_id, int version,  String name, String des, String currentTime) {
        this.record_id = record_id;
        this.version = version;
        this.name = name;
        this.des = des;
        this.last_build = currentTime;
    }

    public void setLast_build(String last_build) {
        this.last_build = last_build;
    }
}
