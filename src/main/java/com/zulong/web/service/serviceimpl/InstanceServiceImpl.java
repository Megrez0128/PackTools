package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.FlowSummaryDao;
import com.zulong.web.dao.InstanceDao;
import com.zulong.web.dao.NodeDao;
import com.zulong.web.entity.Instance;
import com.zulong.web.entity.Node;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zulong.web.config.ConstantConfig.CONST_INIT_END_TIME;


@Service
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private FlowSummaryDao flowSummaryDao;

    @Autowired
    private FlowDao flowDao;

    @Override
    public boolean instanceStart(String uuid, int flow_record_id, String start_time, boolean complete, boolean has_error) {
        //查找instance_id为uuid的instance是否存在，如果已经有instance存在则创建失败
        Instance tmp = instanceDao.findInstanceByUuid(uuid);
        if(tmp!=null) {
            return false;
        }

        //如果不存在则更新flowSummary, flow的last_build
        try{
            boolean flag = flowDao.updateLastBuild(flow_record_id,start_time)
                    && flowSummaryDao.buildUpdateFlowSummary(flowDao.getFlowIdByRecordId(flow_record_id),start_time);
            if(!flag){
                LoggerManager.logger().warn("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@UpdateFlowSummary failed");
                return false;
            }
        }catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@UpdateFlowSummary failed");
            return false;
        }
        // 如果不存在则创建新的instance
        Instance instance = new Instance();
        instance.setUuid(uuid);
        instance.setFlow_record_id(flow_record_id);
        instance.setStart_time(start_time);
        instance.setComplete(complete);
        instance.setHas_error(has_error);
        try{
            instanceDao.insertInstance(instance);
            return true;
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@insertInstance failed");
            return false;
        }
    }

    @Override
    public boolean instanceStartNode(String uuid, int flow_record_id, String node_id, String start_time, boolean has_error, String option){
        Node node = new Node();
        node.setInstance_id(uuid);
        node.setNode_id(node_id);
        node.setStart_time(start_time);
        node.setEnd_time(CONST_INIT_END_TIME);
        node.setOptions(option);
        try{
            nodeDao.insertNode(node);
        }catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@nodeDao insert failed|node_id=%s|uuid=%s", node_id, uuid));
            return false;
        }
        //查找instance_id为uuid的instance是否存在
        Instance tmp = instanceDao.findInstanceByUuid(uuid);
        if(tmp!=null) {
            return true;
        }

        //如果不存在则更新flowSummary,flow的last_build
        try{
            boolean flag = flowDao.updateLastBuild(flow_record_id,start_time)
                    && flowSummaryDao.buildUpdateFlowSummary(flowDao.getFlowIdByRecordId(flow_record_id),start_time);
            if(!flag){
                LoggerManager.logger().warn("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@UpdateFlowSummary failed");
                return false;
            }
        }catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@UpdateFlowSummary failed");
            return false;
        }
        //如果不存在则创建新instance
        Instance instance = new Instance();
        instance.setUuid(uuid);
        instance.setFlow_record_id(flow_record_id);
        instance.setStart_time(start_time);
        //instance.setComplete(complete);
        instance.setComplete(false);
        instance.setHas_error(has_error);
        try{
            instanceDao.insertInstance(instance);
            return true;
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceStartNode@insertInstance failed");
            return false;
        }
    }

    @Override
    public boolean instanceEnd(String uuid, int flow_record_id, String end_time, boolean complete, boolean has_error) {
        try{
            instanceDao.endInstance(flow_record_id, end_time, complete, has_error, uuid);
            return true;
        }catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceEndNode@updateInstance failed|flow_record_id = %s|complete = %s|has_error = %s|uuid = %s|",flow_record_id,complete ? "true" : "false",has_error ? "true" : "false",uuid));
            return false;
        }
    }


    @Override
    public boolean instanceEndNode(String uuid, int flow_record_id, String node_id, String end_time, boolean complete, boolean has_error, String option){
        try{
            boolean flag = nodeDao.updateNode(node_id, uuid, end_time, option);
            if(!flag){
                LoggerManager.logger().warn(String.format("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceEndNode@nodeDao update flag is false|node_id = %s|uuid = %s|end_time = %s|option = %s|",node_id,uuid,end_time,option));
                return false;
            }
        }catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceEndNode@nodeDao update failed|node_id = %s|uuid = %s|end_time = %s|option = %s|",node_id,uuid,end_time,option));
            return false;
        }

        try{
            instanceDao.updateInstance(flow_record_id, false, has_error, uuid);
            return true;
        }catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.service.serviceimpl]InstanceServiceImpl.instanceEndNode@updateInstance failed|flow_record_id = %s|complete = %s|has_error = %s|uuid = %s|",flow_record_id,complete ? "true" : "false",has_error ? "true" : "false",uuid));
            return false;
        }
    }

    @Override
    public Map<String, Object> findInstanceByUuid(String uuid) {
        Map<String,Object> instanceAndNodeList = new HashMap<>();
        instanceAndNodeList.put("instance",instanceDao.findInstanceByUuid(uuid));
        instanceAndNodeList.put("running_nodes",nodeDao.getRunningNode(uuid));
        instanceAndNodeList.put("complete_nodes", nodeDao.getCompleteNode(uuid));
        return instanceAndNodeList;
    }

    @Override
    public List<Instance> findInstanceByFlowRecordId(int record_id) {
        return instanceDao.getInstanceByFlowRecordId(record_id);
    }

    @Override
    public boolean insertInstance(Instance instance) {
        instanceDao.insertInstance(instance);
        return true;
    }
}
