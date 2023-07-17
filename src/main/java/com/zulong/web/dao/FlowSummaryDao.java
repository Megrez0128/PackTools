package com.zulong.web.dao;

import java.util.List;

import com.zulong.web.entity.FlowSummary;

public interface FlowSummaryDao {
    public boolean insertFlowSummary(FlowSummary flowSummary);
    public List<FlowSummary> getFlowSummaryList();
    public FlowSummary findFlowSummaryByID(int flow_id);
	public void updateFlowSummary(FlowSummary flowSummary);
    public boolean updateFlowSummary(int flow_id, String name,String des);
}
