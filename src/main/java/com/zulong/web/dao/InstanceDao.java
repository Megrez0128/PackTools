package com.zulong.web.dao;

import com.zulong.web.entity.Instance;

public interface InstanceDao {
    public boolean findInstanceByFlowID(int record_id);

    public Instance findInstanceByID(Integer instance_id);
}
