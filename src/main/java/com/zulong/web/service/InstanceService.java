package com.zulong.web.service;

import com.zulong.web.entity.Instance;

import java.util.List;
import java.util.Map;


public interface InstanceService {
    public boolean instanceStartNode(String uuid, int flow_record_id, String node_id, String start_time, boolean complete,boolean has_error, String option);
    public boolean instanceEndNode(String uuid, int flow_record_id, String node_id, String end_time, boolean complete,boolean has_error, String option);
    public Map<String,Object> findInstanceByUuid(String uuid);
    public List<Instance> findInstanceByFlowRecordId(int record_id);
    public boolean insertInstance(Instance instance);
}
