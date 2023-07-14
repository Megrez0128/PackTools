package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Instance;
import com.zulong.web.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private InstanceDao instanceDao;


    @Override
    final public Instance instanceStartNode(String uuid, int flow_record_id, String node_id, String start_time, boolean complete,boolean has_error, String option){
        //todo 插入node表，再更新instance表
        Instance instance = new Instance();
        instance.setUuid(uuid);
        instance.setFlow_record_id(flow_record_id);
        instance.setNode_id(node_id);
        instance.setStart_time(start_time);
        instance.setComplete(complete);
        instance.setHas_error(has_error);
        instance.setOption(option);
        instanceDao.insertInstance(instance);
        return instance;
    }

    @Override
    final public Instance instanceEndNode(String uuid, int flow_record_id, String node_id, String end_time, boolean complete,boolean has_error, String option){
        //todo 更新node表，再更新instance表
        return null;
    }
    @Override
    public void PullAndBuild() {

    }

    @Override
    public Instance findInstanceByID(int uuid) {
        return instanceDao.findInstanceByID(uuid);
    }
}
