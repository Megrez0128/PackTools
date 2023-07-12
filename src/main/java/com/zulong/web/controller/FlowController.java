package com.zulong.web.controller;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.User;
import com.zulong.web.exception.ClientArgIllegalException;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zulong.web.utils.ResultCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/myflow")
public class FlowController
{
    private final FlowService flowService;

    @Autowired
    public FlowController(FlowService flowService)
    {
        this.flowService = flowService;
    }


    @PostMapping(value="/list")
    public Map<String, Object> getFlowList() {
        ArrayList<HashMap> flowlist = flowService.getFlowList();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 20000);
        response.put("data", flowlist);
        return response;
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createFlow(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String des = request.get("des");
        LoggerManager.logger().debug(String.format("[com.zulong.web.controller]FlowController.createFlowchart@ success receive post|name=%s|des=%s",name,des));
        Flow flow = flowService.createFlow(name, des);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 20000);
        response.put("data", flow);

        return response;
    }

    /**
     * 对应flow-更新流程-save/update接口
     * @param request
     * @return
     */
    @PostMapping(value = "/save")
    public Map<String, Object> saveFlow(@RequestBody Map<String, Object> request) {
        try {
            int fid = (int) request.get("fid");
            // 这里的graph_data和blackboard理论上是json，先用String存储，不手动处理
            String graph_data = (String) request.get("graph_data");
            String blackboard = (String) request.get("blackboard");
            boolean result = flowService.saveFlow(fid, graph_data, blackboard);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", result);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.saveFlowchart@saving operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", null);
            return response;
        }
    }

    /**
     * 对应flow-delete接口
     * @param request
     * @return
     */
    @PostMapping(value = "/delete")
    public Map<String, Object> deleteFlow(@RequestBody Map<String, Object> request) {
        // TODO:验证有无操作该flow的权限，以及flow有无对应的instance
        // 若不满足该TODO，返回40100

        try {
            int fid = (int) request.get("fid");
            boolean result = flowService.deleteFlow(fid);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", true);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.saveFlowchart@deletion operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 30400);
            response.put("data", false);
            return response;
        }
    }
}
