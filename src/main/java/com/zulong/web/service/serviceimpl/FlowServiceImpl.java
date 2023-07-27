package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.FlowSummaryDao;
import com.zulong.web.dao.GroupFlowDao;
import com.zulong.web.entity.Flow;
import com.zulong.web.entity.FlowSummary;
import com.zulong.web.entity.relation.GroupFlow;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.zulong.web.config.ConstantConfig.DEFAULT_META_ID;

@Service
public class FlowServiceImpl implements FlowService {

    // 设置record_id的起始值
    final static int START_RECORD_ID = 1000;
    // 设置flow_id的起始值
    final static int START_FLOW_ID = 1000;
    // 设置version的起始值
    final static int START_VERSION = 0;
    // 设置last_build的默认值
    final static String DEFAULT_LAST_BUILD = "yyyy-MM-dd HH:mm:ss";
    // 设置last_commit的默认值
    final static String DEFAULT_LAST_COMMIT = "yyyy-MM-dd HH:mm:ss";
    // 设置save_time的默认值
    final static String DEFAULT_SAVE_TIME = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private FlowDao flowDao;

    @Autowired
    private FlowSummaryDao flowSummaryDao;

    @Autowired
    private GroupFlowDao groupFlowDao;


    int curr_record_id = START_RECORD_ID;
    int curr_flow_id = START_FLOW_ID;

    private int getStartRecordId(){
        int currRecordId = flowDao.getCurrRecordId();
        return  currRecordId != -1 ? currRecordId :START_RECORD_ID;
    }

    private int getStartFlowId(){
        int currFlowId = flowDao.getCurrFlowId();
        return  currFlowId != -1 ? currFlowId :START_FLOW_ID;
    }
    @Override
    public Flow createFlow(String graph_data, String blackboard, int meta_id, int group_id, String name, String des){
        curr_record_id = getStartRecordId();

        curr_flow_id = getStartFlowId();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String current_time = df.format(new Date());
        // 找到最大的flow_id
        Flow flow = new Flow();
        flow.setRecord_id(curr_record_id + 1);
        flow.setFlow_id(curr_flow_id + 1);
        flow.setVersion(START_VERSION);
        flow.setSave_time(current_time);
        flow.setMeta_id(meta_id);
        flow.setGraph_data(graph_data);
        flow.setBlackboard(blackboard);
        flowDao.insertFlow(flow);
        curr_record_id++;
        //创建对应的flow_summary
        FlowSummary flowSummary = new FlowSummary();
        flowSummary.setFlow_id(curr_flow_id + 1);
        flowSummary.setLast_build(DEFAULT_LAST_BUILD);
        flowSummary.setLast_commit(DEFAULT_LAST_COMMIT);
        flowSummary.setLast_version(START_VERSION);
        flowSummary.setName(name);
        flowSummary.setDes(des);
        boolean flag = flowSummaryDao.insertFlowSummary(flowSummary);
        if(!flag){
            flowDao.deleteFlow(curr_record_id);
            return null;
        }
        curr_flow_id++;
        //创建对应的flow_group
        groupFlowDao.insertGroupFlow(group_id, curr_flow_id);
        //todo:还要把这个加入管理员所在的组
        return flow;
    }

    @Override public Flow findFlowByRecordId(int record_id) {
        return flowDao.findFlowByRecordId(record_id);
    }

    @Override
    public Flow cloneFlow(int record_id, String name, String des) {
        curr_record_id = getStartRecordId();

        //找到record_id为record_id的flow
        Flow flow = flowDao.findFlowByRecordId(record_id);
        //更新record_id,version
        flow.setRecord_id(curr_record_id + 1);
        curr_record_id++;
        int version = flow.getVersion();
        flow.setVersion(version + 1);
        //修改is_committed 为 false, commit_message为null
        flow.setCommitted(false);
        flow.setCommit_message("");
        flow.setLast_build(DEFAULT_LAST_BUILD);
        flow.setSave_time(DEFAULT_SAVE_TIME);
        //插入新记录
        flowDao.insertFlow(flow);
        
        //找到flow_id对应的flow_summary
        flowSummaryDao.cloneUpdateFlowSummary(flow.getFlow_id(),name,des);

        return flow;
    }

    @Override
    public Flow commitFlow(int record_id, String commit_message) {
        try {
            //根据record_id找到对应的flow
            Flow flow = flowDao.findFlowByRecordId(record_id);
            //更新flow的last_commit，is_committed

            //如果已经commit过了，就不再commit
            if(flow.isCommitted()){
                LoggerManager.logger().warn(String.format("[com.zulong.web.service.serviceimpl]FlowServiceImpl.commitFlow@flow has been committed|record_id=%d", record_id));
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String current_time = df.format(new Date());// new Date()为获取当前系统时间
            flow.setCommit_message(commit_message);
            flow.setCommitted(true);
//            flow.setLast_build(current_time);
            boolean flag = flowDao.updateFlow(flow) && flowSummaryDao.commitUpdateFlowSummary(curr_flow_id,current_time);
            if(!flag) {
                LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]FlowServiceImpl.commitFlow@commit flow failed|record_id=%d", record_id));
                return null;
            }
            return flow;

        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]FlowServiceImpl.commitFlow@commit flow failed|record_id=%d|error=%s", record_id, e.getMessage()));
            return null;
        }
    }

    @Override
    public Flow saveFlow(String name, String des, int flow_id, int meta_id, String graphData, String blackboard) {
        try {
            curr_record_id = getStartRecordId();
            //找到当前flow_id对应的最大version
            int version = flowDao.findMaxVersion(flow_id);
            if(version == -1 ){
                LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]FlowServiceImpl.saveFlow@flow_id not exists|flow_id=%d", flow_id));
                return null;
            }
            //根据flow_id和version找到对应的flow实例
            Flow flow = flowDao.getFlowDetailsByFlowIdAndVersion(flow_id, version);
            //更新flow的commit_message，core_meta_id，extra_meta_id，graph_data，blackboard
            flow.setRecord_id(curr_record_id + 1);
            flow.setCommitted(false);
            //flow.setCommit_message(flow.getCommit_message());
            // meta_id可以为空，若meta_id=0说明没有传入该参数，即不变
            if(meta_id == DEFAULT_META_ID) meta_id = flow.getMeta_id();
            flow.setMeta_id(meta_id);
            if(graphData != null) flow.setGraph_data(graphData);
            if(blackboard != null) flow.setBlackboard(blackboard);
            flow.setLast_build(DEFAULT_LAST_BUILD);
            flow.setVersion(version + 1);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String current_time = df.format(new Date());// new Date()为获取当前系统时间
            flow.setSave_time(current_time);
            flowDao.insertFlow(flow);

            //根据flow_id找到对应的flow_summary
            FlowSummary flowSummary = flowSummaryDao.findFlowSummaryByID(flow_id);
            flowSummary.setLast_version(version + 1);
            if(name != null) flowSummary.setName(name);
            if(des != null) flowSummary.setDes(des);
            flowSummaryDao.saveUpdateFlowSummary(flowSummary);
            curr_record_id++;
            return flow;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]FlowServiceImpl.saveFlow@save flow failed|flow_id=%d|error=%s", flow_id, e.getMessage()));
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
    public List<Flow> getFlowList(int group_id) {
        List<GroupFlow> groupFlowList = groupFlowDao.getFlowListByGroupId(group_id);
        List<Flow> flowList = flowDao.getFlowList();

        Set<Integer> flowIdSet = new HashSet<>();
        for (GroupFlow groupFlow : groupFlowList) {
            flowIdSet.add(groupFlow.getFlow_id());
        }
        // 也可以改成返回flowSummaryList
        List<Flow> filteredFlowList = new ArrayList<>();
        for (Flow flow : flowList) {
            if (flowIdSet.contains(flow.getFlow_id())) {
                filteredFlowList.add(flow);
            }
        }
        return filteredFlowList;
    }
    @Override
    public List<FlowSummary> getFlowSummaryList(int group_id) {
        List<GroupFlow> groupFlowList = groupFlowDao.getFlowListByGroupId(group_id);
        List<FlowSummary> flowSummaryList = flowSummaryDao.getFlowSummaryList();

        Set<Integer> flowIdSet = new HashSet<>();
        for (GroupFlow groupFlow : groupFlowList) {
            flowIdSet.add(groupFlow.getFlow_id());
        }

        List<FlowSummary> filteredFlowSummaryList = new ArrayList<>();
        for (FlowSummary flowSummary : flowSummaryList) {
            if (flowIdSet.contains(flowSummary.getFlow_id())) {
                filteredFlowSummaryList.add(flowSummary);
            }
        }
        return filteredFlowSummaryList;
    }

    @Override
    final public Flow getFlowDetails(int flow_id, int version) {
        return flowDao.getFlowDetailsByFlowIdAndVersion(flow_id, version);
    }

    @Override
    final public Flow getFlowDetailsByID(int record_id) { return flowDao.getFlowDetailsByRecordId(record_id); }
    @Override
    final public List<Flow> getHistoryFlowList(int flow_id) {
        return flowDao.getHistoryFlowList(flow_id);
    }

    @Override
    final public Flow getNewestFlow(int flow_id){ return flowDao.getNewestFlow(flow_id); }

    @Override
    final public Flow getNewVersionFlow(int flow_id){
        return flowDao.getNewVersionFlow(flow_id);
    }
}
