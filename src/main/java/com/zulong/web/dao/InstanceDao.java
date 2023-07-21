package com.zulong.web.dao;

import com.zulong.web.entity.Instance;

import java.util.List;

public interface InstanceDao {
    boolean findInstanceByFlowID(int record_id);
    Instance findInstanceByUuid(String uuid);
    List<Instance> getInstanceByFlowRecordId(int record_id);
    boolean insertInstance(Instance instance);
    boolean updateInstance(int flow_record_id,boolean complete,boolean has_error,String uuid);
}
