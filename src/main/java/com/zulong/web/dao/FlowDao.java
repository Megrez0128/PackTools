package com.zulong.web.dao;

import com.zulong.web.entity.Flow;

import java.util.List;

public interface FlowDao {
    public List<Flow> getFlowList();

    public Flow getFlowDetails(int flow_id, int version);
    public int deleteFlow(int record_id);
}
