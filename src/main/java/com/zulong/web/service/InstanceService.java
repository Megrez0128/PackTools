package com.zulong.web.service;

import com.zulong.web.entity.Instance;


public interface InstanceService {
    public Instance CreateInstance(String uuid, int flow_id, String node_id, String start_time, boolean complete,boolean has_error, String option);
    public void PullAndBuild();
    public Instance findInstanceByID(int uuid);
}
