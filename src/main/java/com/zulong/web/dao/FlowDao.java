package com.zulong.web.dao;

import com.zulong.web.entity.Flow;

import java.util.List;

public interface FlowDao {
    Flow findFlowByRecordId(int record_id);
    boolean insertFlow(Flow flow);
    List<Flow> getFlowList();
    Flow getFlowDetailsByFlowIdAndVersion(int flow_id, int version);
    Flow getFlowDetailsByRecordId(int record_id);
    int deleteFlow(int record_id);
    //public Flow cloneFlow(int record_id, boolean is_committed, String commit_message);
    boolean updateFlow(Flow flow);
    boolean updateLastBuild(int record_id, String last_build);
    int findMaxVersion(int flow_id);
	Flow findByFlowIDAndVersion(int flow_id, int version);
    List<Flow> getHistoryFlowList(int flow_id);
    // NewestFlow是最新版本，NewVersionFlow是已经commit的最新版本
    Flow getNewestFlow(int flow_id);
    Flow getNewVersionFlow(int flow_id);
    int getCurrRecordId();
    int getCurrFlowId();
    int getFlowIdByRecordId(int record_id);
}
