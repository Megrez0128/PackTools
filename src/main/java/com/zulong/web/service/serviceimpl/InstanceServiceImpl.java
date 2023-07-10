package com.zulong.web.service.serviceimpl;

import com.zulong.web.entity.Instance;
import com.zulong.web.service.InstanceService;
import org.springframework.stereotype.Service;


@Service
public class InstanceServiceImpl implements InstanceService {
    int tmpuuid = 0;

    @Override
    final public Instance CreateInstance(String flow_id, String node_id, String option){
        Instance instance = new Instance();
        instance.initInstance(tmpuuid++, flow_id, node_id, option);
        return instance;
    }

    @Override
    public void PullAndBuild() {

    }
}
