package com.zulong.web.service;

import com.zulong.web.entity.Instance;


public interface InstanceService {
    public Instance CreateInstance(int flow_id, String node_id, String option);
    public void PullAndBuild();
}
