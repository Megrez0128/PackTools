package com.zulong.web.controller;

import com.zulong.web.entity.Instance;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InstanceController {
    private InstanceService instanceService;
    @Autowired
    public InstanceController(InstanceService instanceService)
    {
        this.instanceService = instanceService;
    }

    @PostMapping(value = "/instance")
    public Map<String, Object> CreateInstance(@RequestBody Map<String, Object> request){

        int flow_id = (int)request.get("flow_id");
        String node_id = (String)request.get("node_id");
        String option = (String)request.get("option");

        // TODO: 全局查询flow_id是否存在，并返回对应的flow

        // 创建instance
        try{
            Instance instance = instanceService.CreateInstance(flow_id, node_id, option);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", instance);
            // TODO: 如果查找成功，需要更新flow的last_build
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", null);
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.CreateInstance@create operation failed|flow_id=%d|node_id=%s", flow_id, node_id), e);
            return response;
        }
    }

    /**
     * 轮询，每隔一段时间发送一次拉取请求
     * 只有在is_whole=true时才会返回Data
     * @param request
     */
    @PostMapping(value = "/build")
    public void UpdateInstance(@RequestBody Map<String, Object> request){
        try{

        } catch (Exception e) {

        }
    }
}
