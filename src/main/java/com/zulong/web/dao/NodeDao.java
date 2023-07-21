package com.zulong.web.dao;

import com.zulong.web.entity.Node;

import java.util.List;

public interface NodeDao {
    //插入
    void insertNode(Node node);
    //修改，传入node_id,instance_id,end_time,options
    boolean updateNode(String node_id, String instance_id, String end_time, String options);
    List<Node> getRunningNode(String instance_id);
    List<Node> getCompleteNode(String instance_id);
}
