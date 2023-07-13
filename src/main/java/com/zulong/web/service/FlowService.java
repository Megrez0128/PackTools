package com.zulong.web.service;

import com.zulong.web.entity.Flow;
import java.util.List;

public interface FlowService
{

    Flow createFlow(String name, String des);

    boolean saveFlow(int fid, String graphData, String blackboard);

    boolean deleteFlow(int fid);
    List<Flow> getFlowList();
}
