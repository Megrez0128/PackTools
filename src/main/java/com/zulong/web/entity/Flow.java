package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flow implements Serializable {
    private int record_id;

    private int flow_id;
    private int version;

    private boolean is_committed;
    private String commit_message;

    private String last_build;
    private int core_meta_id;
    private int extra_meta_id;

    private String graph_data;
    private String blackboard;

    public Flow(int record_id, int version, boolean is_committed, String commit_message, String currentTime) {
        this.record_id = record_id;
        this.version = version;
        this.is_committed = is_committed;
        this.commit_message = commit_message;
        this.last_build = currentTime;
    }

    public Flow(Flow flow){
        this.flow_id = flow.flow_id;
        this.version = flow.version + 1;
        this.is_committed = flow.is_committed;
        this.commit_message = flow.commit_message;
        this.core_meta_id = flow.core_meta_id;
        this.extra_meta_id = flow.extra_meta_id;
        this.graph_data = flow.graph_data;
        this.blackboard = flow.blackboard;
    }

    public void setLast_build(String last_build) {
        this.last_build = last_build;
    }
}
