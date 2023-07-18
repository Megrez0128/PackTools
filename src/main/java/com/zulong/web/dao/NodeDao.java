package com.zulong.web.dao;

import com.zulong.web.entity.Node;

public interface NodeDao {
    //插入
    void insertNode(Node node);
    //修改，传入node_id,instance_id,end_time,options
    boolean updateNode(int node_id, int instance_id, String end_time, String options);
}
