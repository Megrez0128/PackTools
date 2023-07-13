package com.zulong.web.service;

import com.zulong.web.entity.Flow;
import java.util.List;

public interface FlowService
{

    public Flow createFlow(String name, String des);

    public boolean saveFlow(int fid, String graphData, String blackboard);

    public int deleteFlow(int fid);
    public List<Flow> getFlowList();
    public Flow getFlowDetails(int flow_id, int version);
}
