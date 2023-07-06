package com.zulong.web.service;

import com.zulong.web.entity.Flowchart;
import com.zulong.web.entity.User;

public interface FlowchartService
{
    public User getUserInfo(final String username);

    Flowchart createFlowchart(String name, String des);

    boolean saveFlowchart(int fid, String graphData, String blackboard);

    boolean deleteFlowchart(int fid);
}
