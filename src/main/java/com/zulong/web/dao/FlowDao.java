package com.zulong.web.dao;

import com.zulong.web.entity.Flow;

import java.util.List;

public interface FlowDao {
    public List<Flow> getFlowList();

    public Flow getFlowDetails(int record_id);
}
