package com.zulong.web.dao;

import java.util.List;

import com.zulong.web.entity.FlowSummary;

public interface FlowSummaryDao {
    boolean insertFlowSummary(FlowSummary flowSummary);
    List<FlowSummary> getFlowSummaryList();
    FlowSummary findFlowSummaryByID(int flow_id);
	void saveUpdateFlowSummary(FlowSummary flowSummary);
    boolean cloneUpdateFlowSummary(int flow_id, String name, String des);
    boolean commitUpdateFlowSummary(int flow_id,String last_commit);
    boolean buildUpdateFlowSummary(int flow_id,String last_build);
}
