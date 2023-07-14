package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.FlowSummaryDao;
import com.zulong.web.entity.Flow;
import com.zulong.web.entity.FlowSummary;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FlowServiceImpl implements FlowService {

    // 设置record_id的起始值
    final static int START_RECORD_ID = 1000;
    // 设置flow_id的起始值
    final static int START_FLOW_ID = 1000;
    // 设置version的起始值
    final static int START_VERSION = 0;
    // 设置last_build的起始值
    final static String START_LAST_BUILD = "1970-01-01 00:00:00";
    // 设置last_commit的起始值
    final static String START_LAST_COMMIT = "1970-01-01 00:00:00";
    @Autowired
    private FlowDao flowDao;

    @Autowired
    private FlowSummaryDao flowSummaryDao;

    int curr_record_id = START_RECORD_ID;
    int curr_flow_id = START_FLOW_ID;

    @Override
    public Flow createFlow(String name, String des, String graph_data, String blackboard, int core_meta_id, int extra_meta_id){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String current_time = df.format(new Date());// new Date()为获取当前系统时间
        LoggerManager.logger().info(String.format(
                "[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.createFlowchart@create flowchart|id=%s|version=%s|name=%s|des=%s|current_time=%s|inner=%s", curr_record_id, 0, name, des, current_time, "false"));
       // 找到最大的flow_id
        Flow flow = new Flow();
        flow.setRecord_id(curr_record_id);
        flow.setFlow_id(curr_flow_id);
        flow.setVersion(START_VERSION);
        flow.setCore_meta_id(core_meta_id);
        flow.setExtra_meta_id(extra_meta_id);
        flow.setGraph_data(graph_data);
        flow.setBlackboard(blackboard);
        flowDao.insertFlow(flow);
        FlowSummary flowSummary = new FlowSummary();
        flowSummary.setFlow_id(curr_flow_id);
        flowSummary.setName(name);
        flowSummary.setDes(des);
        flowSummary.setLast_build(START_LAST_BUILD);
        flowSummary.setLast_commit(START_LAST_COMMIT);
        flowSummary.setLast_version(START_VERSION);
        flowSummaryDao.insertFlowSummary(flowSummary);
        curr_record_id++;
        curr_flow_id++;
        return flow;
    }

    @Override public Flow findFlowByID(int record_id) {
        return flowDao.findByFlowID(record_id);
    }

    @Override
    public Flow cloneFlow(int record_id, String name, String des) {
        return flowDao.cloneFlow(record_id, name, des);
    }

    @Override
    public Flow commitFlow(int record_id, String commit_message) {
        try {
            //根据record_id找到对应的flow
            Flow flow = flowDao.findByFlowID(record_id);
            //更新flow的last_commit，is_committed
            //如果已经commit过了，就不再commit
            if(flow.is_committed()){
                LoggerManager.logger().warn(String.format("[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.commitFlow@flow has been committed|record_id=%d", record_id));
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String current_time = df.format(new Date());// new Date()为获取当前系统时间
            flow.setCommit_message(commit_message);
            flow.set_committed(true);
            flow.setLast_build(current_time);
            boolean flag = flowDao.updateFlow(flow);
            if (flag) {
                LoggerManager.logger().info(String.format("[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.commitFlow@commit flow success|record_id=%d", record_id));
                return flow;
            } else {
                LoggerManager.logger().error(String.format("[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.commitFlow@commit flow failed|record_id=%d", record_id));
                return null;
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.commitFlow@commit flow failed|record_id=%d|error=%s", record_id, e.getMessage()));
            return null;
        }
    }

    @Override
    public Flow saveFlow(int flow_id, String commit_message, int core_meta_id,int extra_meta_id, String graphData, String blackboard, String name, String des) {
        try {
            //找到当前flow_id对应的最大version
            int version = flowDao.findMaxVersion(flow_id);
            //根据flow_id和version找到对应的flow
            Flow flow = flowDao.findByFlowIDAndVersion(flow_id, version);
            //更新flow的commit_message，core_meta_id，extra_meta_id，graph_data，blackboard
            flow.setCommit_message(commit_message);
            flow.setCore_meta_id(core_meta_id);
            flow.setExtra_meta_id(extra_meta_id);
            flow.setGraph_data(graphData);
            flow.setBlackboard(blackboard);
            flow.setVersion(version + 1);
            flowDao.insertFlow(flow);

            //根据flow_id找到对应的flow_summary
            FlowSummary flowSummary = flowSummaryDao.findFlowSummaryByID(flow_id);
            //更新flow_summary的name，des，last_build，last_commit，last_version
            flowSummary.setName(name);
            flowSummary.setDes(des);
            flowSummary.setLast_version(version + 1);
            flowSummaryDao.updateFlowSummary(flowSummary);

            return flow;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.saveFlow@save flow failed|flow_id=%d|error=%s", flow_id, e.getMessage()));
            return null;
        }
    }

    /**
     * @param flow_id
     * @return
     * 返回1表示正常删除，返回0表示Flow有对应的instance实例不能删除，返回2表示发生了数据库错误
     */
    @Override
    public int deleteFlow(int flow_id) {
        return flowDao.deleteFlow(flow_id);
    }

    @Override
    public List<Flow> getFlowList(){
        return flowDao.getFlowList();
    }

    @Override
    public Flow getFlowDetails(int flow_id, int version) {
        return flowDao.getFlowDetails(flow_id, version);
    }

    @Override
    public List<Flow> getHistoryFlowList(int flow_id) {
        return flowDao.getHistoryFlowList(flow_id);
    }
}
