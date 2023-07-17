package com.zulong.web.service;

import com.zulong.web.entity.Flow;
import java.util.List;

public interface FlowService
{

    public Flow findFlowByRecordId(int record_id);
    public Flow createFlow( String graph_data, String blackboard, int core_meta_id, int extra_meta_id);

    public Flow saveFlow(int flow_id, String commit_message, int core_meta_id,int extra_meta_id, String graphData, String blackboard);

    public int deleteFlow(int flow_id);
    public List<Flow> getFlowList();
    public Flow getFlowDetails(int flow_id, int version);

    public Flow cloneFlow(int record_id, String name, String des);

    public Flow commitFlow(int record_id, String commit_message);
    public List<Flow> getHistoryFlowList(int flow_id);
}
