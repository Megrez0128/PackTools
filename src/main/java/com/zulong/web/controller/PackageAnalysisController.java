package com.zulong.web.controller;

import com.zulong.web.entity.Flowchart;
import com.zulong.web.entity.User;
import com.zulong.web.exception.ClientArgIllegalException;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowchartService;

import java.util.HashMap;
import java.util.Map;

import com.zulong.web.utils.ResultCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/myflow")
public class PackageAnalysisController
{
    private final FlowchartService flowchartService;

    @Autowired
    public PackageAnalysisController(FlowchartService flowchartService)
    {
        this.flowchartService = flowchartService;
    }

    @GetMapping(value = "/userinfo")
    public User getUserBasicInfo(@RequestParam String username)
    {
        LoggerManager.logger().info(String.format("[com.zulong.web.controller]PackageAnalysisController.getUserBasicInfo@ success run into getUserBasicInfo function"));
        if (StringUtils.isBlank(username))
        {
            ClientArgIllegalException e = new ClientArgIllegalException(ResultCode.PARAM_IS_INVALID);
            LoggerManager.logger().error(String.format(
                    "[com.zulong.web.controller]PackageAnalysisController.getUserBasicInfo@param invalid|username=%s"), e);
        }

        User userInfo = flowchartService.getUserInfo(username);
        if (userInfo == null)
        {
            ClientArgIllegalException e = new ClientArgIllegalException(ResultCode.USER_NOT_EXIST);
            LoggerManager.logger().error(String.format(
                    "[com.zulong.web.controller]PackageAnalysisController.getUserBasicInfo@user info not exist|username=%s"), e);
        }
        return userInfo;
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createFlowchart(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String des = request.get("des");
        LoggerManager.logger().debug(String.format("[com.zulong.web.controller]PackageAnalysisController.createFlowchart@ success receive post|name=%s|des=%s",name,des));
        Flowchart flowchart = flowchartService.createFlowchart(name, des);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 20000);
        response.put("data", flowchart);

        return response;
    }

    @PostMapping(value = "/save")
    public Map<String, Object> saveFlowchart(@RequestBody Map<String, Object> request) {
        try {
            int fid = (int) request.get("fid");
            //todo: 这里的graph_data和blackboard理论上是json，先用String存储，不手动处理
            String graph_data = (String) request.get("graph_data");
            String blackboard = (String) request.get("blackboard");
            boolean result = flowchartService.saveFlowchart(fid, graph_data, blackboard);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", result);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]PackageAnalysisController.saveFlowchart@saving operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", null);
            return response;
        }
    }

    @PostMapping(value = "/delete")
    public Map<String, Object> deleteFlowchart(@RequestBody Map<String, Object> request) {
        try {
            int fid = (int) request.get("fid");
            boolean result = flowchartService.deleteFlowchart(fid);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", result);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]PackageAnalysisController.saveFlowchart@deletion operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", null);
            return response;
        }
    }
}
