package com.zulong.web.service;

import com.zulong.web.entity.Instance;


public interface InstanceService {
    public boolean instanceStartNode(String uuid, int flow_record_id, int node_id, String start_time, boolean complete,boolean has_error, String option);
    public boolean instanceEndNode(String uuid, int flow_record_id, int node_id, String end_time, boolean complete,boolean has_error, String option);
    public void PullAndBuild();
    public Instance findInstanceByUuid(String uuid);
}
