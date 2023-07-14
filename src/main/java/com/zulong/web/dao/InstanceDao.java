package com.zulong.web.dao;

import com.zulong.web.entity.Instance;

public interface InstanceDao {
    public boolean findInstanceByFlowID(int record_id);

    public Instance findInstanceByID(int instance_id);
                     
    public void insertInstance(Instance instance);
}
