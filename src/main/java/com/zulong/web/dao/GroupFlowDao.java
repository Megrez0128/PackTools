package com.zulong.web.dao;

public interface GroupFlowDao {

    boolean hasFlowPermission(Integer group_id, Integer flow_id);
    
}
