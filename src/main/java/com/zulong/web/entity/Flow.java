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

    private boolean committed;
    private String commit_message;

    private String last_build;
    private int meta_id;

    private String graph_data;
    private String blackboard;

    public Flow(int record_id, int version, boolean committed, String commit_message, String currentTime) {
        this.record_id = record_id;
        this.version = version;
        this.committed = committed;
        this.commit_message = commit_message;
        this.last_build = currentTime;
    }

    public Flow(Flow flow){
        this.flow_id = flow.flow_id;
        this.version = flow.version + 1;
        this.committed = flow.committed;
        this.commit_message = flow.commit_message;
        this.meta_id = flow.meta_id;
        this.graph_data = flow.graph_data;
        this.blackboard = flow.blackboard;
    }

    public void setLast_build(String last_build) {
        this.last_build = last_build;
    }


}
