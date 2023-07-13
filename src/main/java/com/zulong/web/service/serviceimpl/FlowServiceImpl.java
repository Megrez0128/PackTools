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
    public Flow createFlow(String name, String des, String graph_data, String blackboard, int core_meta_id, int extra_meta_id){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String current_time = df.format(new Date());// new Date()为获取当前系统时间
        LoggerManager.logger().info(String.format(
                "[com.zulong.web.daoimpl.serviceimpl]FlowServiceImpl.createFlowchart@create flowchart|id=%s|version=%s|name=%s|des=%s|current_time=%s|inner=%s", tempId, 0, name, des, current_time, "false"));
        return new Flow(tempId++, 0, name, des, current_time);
    }

    @Override
    public Flow saveFlow(int flow_id, String commit_message, int core_meta_id,int extra_meta_id, String graphData, String blackboard, String name, String des) {
        return null;
    }

    /**
     *
     * @param flow_id
     * @return
     * 返回1表示正常删除，返回0表示Flow有对应的instance实例不能删除，返回2表示发生了数据库错误
     */
    @Override
    public int deleteFlow(int flow_id) {
        return flowDao.deleteFlow(flow_id);
    }

    public List<Flow> getFlowList(){
        List<Flow> flowlist = flowDao.getFlowList();
        return flowlist;
    }

    public Flow getFlowDetails(int flow_id, int version) {
        return flowDao.getFlowDetails(flow_id, version);
    }

    @Override
    public Flow cloneFlow(int record_id, String name, String des) {
        return flowDao.cloneFlow(record_id, name, des);
    }

    @Override
    public Flow commitFlow(int record_id, String commit_message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'commitFlow'");
    }
}
