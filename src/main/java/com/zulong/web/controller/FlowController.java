package com.zulong.web.controller;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.User;
import com.zulong.web.exception.ClientArgIllegalException;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<Flow> flowlist = flowService.getFlowList();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 20000);
        response.put("data", flowlist);
        return response;
    }

    @PostMapping(value="/detail")
    public Map<String, Object> getFlowDetails(@RequestBody Map<String, Integer> request) {
        int flow_id = request.get("flow_id");
        int version = request.get("version");
        try {
            Flow flow = flowService.getFlowDetails(flow_id, version);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", null);
            return response;
        }
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
     * 删除Flow，先验证有无实例，否则不能删除
     * @param request
     * @return
     */
    @PostMapping(value = "/delete")
    public Map<String, Object> deleteFlow(@RequestBody Map<String, Object> request) {
        try {
            int fid = (int) request.get("fid");
            int result = flowService.deleteFlow(fid);
            Map<String, Object> response = new HashMap<>();
            if(result == 1) {
                response.put("code", 20000);
                response.put("data", true);
            }
            if(result == 0) {
                // TODO: 表示flow有对应的Instance，不能删除，可能可以调整返回码
                response.put("code", 40100);
                response.put("data", false);
            }
            else {
                response.put("code", 30400);
                response.put("data", false);
            }
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
