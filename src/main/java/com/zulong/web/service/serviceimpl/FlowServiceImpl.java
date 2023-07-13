package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.entity.Flow;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class FlowServiceImpl implements FlowService {

    @Autowired
    private FlowDao flowDao;

    int tempId = 0;

    @Override
    public Flow createFlow(String name, String des) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String current_time = df.format(new Date());// new Date()为获取当前系统时间
        LoggerManager.logger().info(String.format(
                "[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.createFlowchart@create flowchart|id=%s|version=%s|name=%s|des=%s|current_time=%s|inner=%s", tempId, 0, name, des, current_time, "false"));
        return new Flow(tempId++, 0, name, des, current_time);
    }

    @Override
    public boolean saveFlow(int fid, String graphData, String blackboard) {
        return false;
    }

    /**
     *
     * @param fid
     * @return
     * 返回1表示正常删除，返回0表示Flow有对应的instance实例不能删除，返回2表示发生了数据库错误
     */
    @Override
    public int deleteFlow(int fid) {
        return flowDao.deleteFlow(fid);
    }

    public List<Flow> getFlowList(){
        List<Flow> flowlist = flowDao.getFlowList();
        return flowlist;
    }

    public Flow getFlowDetails(int flow_id, int version) {
        return flowDao.getFlowDetails(flow_id, version);
    }
}
