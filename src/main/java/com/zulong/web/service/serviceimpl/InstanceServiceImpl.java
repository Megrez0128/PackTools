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

    int tmpuuid = 0;

    @Override
    final public Instance CreateInstance(int flow_id, String node_id, String option){
        Instance instance = new Instance();
        instance.initInstance(tmpuuid++, flow_id, node_id, option);
        return instance;
    }

    @Override
    public void PullAndBuild() {

    }

    @Override
    public Instance findInstanceByID(int uuid) {
        return instanceDao.findInstanceByID(uuid);
    }
}
