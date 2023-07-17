package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.InstanceDao;
import com.zulong.web.dao.NodeDao;
import com.zulong.web.entity.Instance;
import com.zulong.web.entity.Node;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InstanceServiceImpl implements InstanceService {

    final String CONST_INIT_END_TIME = "CONST_INIT_END_TIME";

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private NodeDao nodeDao;

    @Override
    final public boolean instanceStartNode(String uuid, int flow_record_id, int node_id, String start_time, boolean complete,boolean has_error, String option){
        //todo 插入node表，再更新instance表
        try{
            Node node = new Node();
            node.setInstance_id(uuid);
            node.setNode_id(node_id);
            node.setStart_time(start_time);
            node.setEnd_time(CONST_INIT_END_TIME);
            node.setOptions(option);
            nodeDao.insert(node);
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.dao.serviceimpl]InstanceServiceImpl.instanceStartNode@nodeDao insert failed");
            return false;
        }

        try{
            Instance instance = new Instance();
            instance.setUuid(uuid);
            instance.setFlow_record_id(flow_record_id);
            instance.setBuild_time(start_time);
            instance.setComplete(complete);
            instance.setHas_error(has_error);
            instanceDao.insertInstance(instance);
            return true;
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.dao.serviceimpl]InstanceServiceImpl.instanceStartNode@insertInstance failed");
            return false;
        }

    }

    @Override
    final public boolean instanceEndNode(String uuid, int flow_record_id, int node_id, String end_time, boolean complete,boolean has_error, String option){
        //todo 更新node表，再更新instance表
        try{
            nodeDao.update(flow_record_id, flow_record_id, end_time, option);
            LoggerManager.logger().warn("[com.zulong.web.dao.serviceimpl]InstanceServiceImpl.instanceEndNode@nodeDao update failed");
        }catch (Exception e){
            return false;
        }

        try{
            instanceDao.updateInstance(flow_record_id,complete,has_error,uuid);
            return true;
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.dao.serviceimpl]InstanceServiceImpl.instanceEndNode@updateInstance failed");
            return false;
        }


    }
    @Override
    public void PullAndBuild() {

    }

    @Override
    public Instance findInstanceByUuid(String uuid) {

        return instanceDao.findInstanceByUuid(uuid);
    }
}
