package com.zulong.web.dao;

import com.zulong.web.entity.Flow;

import java.util.List;

public interface FlowDao {
    Flow findFlowByRecordId(int record_id);
    boolean insertFlow(Flow flow);
    public List<Flow> getFlowList();
    public Flow getFlowDetails(int flow_id, int version);
    public int deleteFlow(int record_id);
    //public Flow cloneFlow(int record_id, boolean is_committed, String commit_message);
    public boolean updateFlow(Flow flow);
	int findMaxVersion(int flow_id);
	Flow findByFlowIDAndVersion(int flow_id, int version);
    public List<Flow> getHistoryFlowList(int flow_id);
}
