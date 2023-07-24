package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

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
//    private byte[] graph_data;
//    private byte[] blackboard;

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

    // 模板化graph_data和blackboard在String和数据库内存储blob的转化
    public static Flow getFlowFromResultSet(ResultSet resultSet) throws SQLException {
        Flow flow = new Flow();
        flow.setRecord_id(resultSet.getInt("record_id"));
        flow.setFlow_id(resultSet.getInt("flow_id"));
        flow.setVersion(resultSet.getInt("version"));
        flow.setCommitted(resultSet.getBoolean("committed"));
        flow.setCommit_message(resultSet.getString("commit_message"));
        flow.setLast_build(resultSet.getString("last_build"));
        flow.setMeta_id(resultSet.getInt("meta_id"));
        Blob graphBlob = resultSet.getBlob("graph_data");
        String graphString = new String(graphBlob.getBytes(1, (int) graphBlob.length()));
        flow.setGraph_data(graphString);
        Blob blackboardBlob = resultSet.getBlob("blackboard");
        String blackboardString = new String(blackboardBlob.getBytes(1, (int) blackboardBlob.length()));
        flow.setBlackboard(blackboardString);
        return flow;
    }

}
