package com.zulong.web.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Node {
    public String instance_id;
    public String node_id;
    public String start_time;
    public String end_time;
    public String options;

    public static Node getNodeFromResultSet(ResultSet resultSet) throws SQLException {
        Node node = new Node();
        node.setInstance_id(resultSet.getString("instance_id"));
        node.setNode_id(resultSet.getString("node_id"));
        node.setStart_time(resultSet.getString("start_time"));
        node.setEnd_time(resultSet.getString("end_time"));
        Blob dataBlob = resultSet.getBlob("options");
        String dataString = new String(dataBlob.getBytes(1, (int) dataBlob.length()));
        node.setOptions(dataString);
        return node;
    }
}