package com.zulong.web.dao;

import com.zulong.web.entity.Instance;

public interface InstanceDao {
    public boolean findInstanceByFlowID(int record_id);

    public Instance findInstanceByUuid(String uuid);
                     
    public boolean insertInstance(Instance instance);

    public boolean updateInstance(int flow_record_id,boolean complete,boolean has_error,String uuid);
}
