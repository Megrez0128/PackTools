package com.zulong.web.service;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface FlowService
{

    Flow createFlow(String name, String des);

    boolean saveFlow(int fid, String graphData, String blackboard);

    boolean deleteFlow(int fid);
    ArrayList<HashMap> getFlowList();
}
