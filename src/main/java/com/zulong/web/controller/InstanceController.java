package com.zulong.web.controller;

import com.zulong.web.entity.Instance;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.InstanceService;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
        try{
            int flow_id = (int)request.get("flow_id");
            String node_id = (String)request.get("node_id");
            String option = (String)request.get("option");
            Instance instance = instanceService.CreateInstance(flow_id, node_id, option);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", instance);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", null);
            LoggerManager.logger().warn("[com.zulong.web.controller]InstanceController.CreateInstance@create operation failed|", e);
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
