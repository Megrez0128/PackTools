package com.zulong.web.service;

import com.zulong.web.entity.Flow;
import java.util.List;

public interface FlowService
{

    public Flow createFlow(String name, String des, String graph_data, String blackboard, int core_meta_id, int extra_meta_id);

    public Flow saveFlow(int flow_id, String commit_message, int core_meta_id,int extra_meta_id, String graphData, String blackboard, String name, String des);

    public int deleteFlow(int flow_id);
    public List<Flow> getFlowList();
    public Flow getFlowDetails(int flow_id, int version);

    public Flow cloneFlow(int record_id, String name, String des);

    public Flow commitFlow(int record_id, String commit_message);
}
