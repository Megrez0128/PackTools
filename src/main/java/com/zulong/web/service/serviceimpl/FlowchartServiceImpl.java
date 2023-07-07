package com.zulong.web.service.serviceimpl;

import com.zulong.web.entity.Flowchart;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowchartService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FlowchartServiceImpl implements FlowchartService {
    int tempId = 0;


    @Override
    public User getUserInfo(String username) {
        return new User("Test", "zulong001",null,false);
    }

    /**
     * 创建流程，返回: 一个成员变量如下的对象
     * "id": tempId++,
     * "state": "success",
     * "name": name,
     * "des": des,
     * "last_build": 当前时间,
     * "inner": false
     **/
    @Override
    public Flowchart createFlowchart(String name, String des) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String current_time = df.format(new Date());// new Date()为获取当前系统时间
        LoggerManager.logger().info(String.format(
                "[com.zulong.web.service.serviceimpl]FlowchartServiceImpl.createFlowchart@create flowchart|id=%s|version=%s|name=%s|des=%s|current_time=%s|inner=%s", tempId, 0, name, des, current_time, "false"));
        return new Flowchart(tempId++, 0,"success", name, des, current_time, false);
    }

    @Override
    public boolean saveFlowchart(int fid, String graphData, String blackboard) {
        return false;
    }

    @Override
    public boolean deleteFlowchart(int fid) {
        return false;
    }
}
