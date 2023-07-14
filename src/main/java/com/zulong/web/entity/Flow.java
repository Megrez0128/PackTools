package com.zulong.web.entity;

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


    private boolean is_committed;
    private String commit_message;

    private String name; //todo: name和des放在flow_summary里面
    private String des;

    private String last_build;
    private int core_meta_id;
    private int extra_meta_id;

    private String graph_data;
    private String blackboard;

    public Flow(int record_id, int version,  String name, String des, String currentTime) {
        this.record_id = record_id;
        this.version = version;
        this.name = name;
        this.des = des;
        this.last_build = currentTime;
    }

    public Flow(Flow flow){
        this.flow_id = flow.flow_id;
        this.name = flow.name;
        this.des = flow.des;
        this.core_meta_id = flow.core_meta_id;
        this.extra_meta_id = flow.extra_meta_id;
        this.graph_data = flow.graph_data;
        this.blackboard = flow.blackboard;
    }

    public void setLast_build(String last_build) {
        this.last_build = last_build;
    }
}
