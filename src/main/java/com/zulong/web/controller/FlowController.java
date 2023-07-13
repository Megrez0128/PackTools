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
    /**
     * 对应flow-创建流程-create接口
     * @param core_meta_id,extra_meta_id,name,des,graph_data,blackboard
     * @return
     */
    @PostMapping(value = "/create")
    public Map<String, Object> createFlow(@RequestBody Map<String, String> request) {
        try {
            Integer core_meta_id = Integer.parseInt(request.get("core_meta_id"));
            Integer extra_meta_id = Integer.parseInt(request.get("extra_meta_id"));
            String graph_data = request.get("graph_data");
            String blackboard = request.get("blackboard");
            String name = request.get("name");
            String des = request.get("des");
            LoggerManager.logger().debug(String.format("[com.zulong.web.controller]FlowController.createFlowchart@ success receive post|name=%s|des=%s",name,des));
            Flow flow = flowService.createFlow(name, des, graph_data, blackboard, core_meta_id, extra_meta_id);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", flow);

            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.createFlow@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", null);
            return response;
        }
    }

    /**
     * 后续在FlowDaoImpl中修改
     */
    @PostMapping(value = "/clone")
    public Map<String, Object> cloneFlow(@RequestBody Map<String, Object> request) {
        try {
            
            int record_id = (int) request.get("record_id");
            String name = (String) request.get("name");
            String des = (String) request.get("des");
            LoggerManager.logger().debug(String.format("[com.zulong.web.controller]FlowController.cloneFlow@ success receive post|record_id=%d|name=%s|des=%s",record_id,name,des));
            Flow flow = flowService.cloneFlow(record_id, name, des);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", flow);

            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.cloneFlow@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage()); // 修改message的值为报错信息
            return response;
        }
                }

    @PostMapping(value = "/commit")
    public Map<String, Object> commitFlow(@RequestBody Map<String, Object> request) {
        try {
            int record_id = (int) request.get("record_id");
            String commit_message = (String) request.get("commit_message");
            LoggerManager.logger().debug(String.format("[com.zulong.web.controller]FlowController.commitFlow@ success receive post|record_id=%d|commit_message=%s",record_id,commit_message));
            Flow flow = flowService.commitFlow(record_id, commit_message);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", flow);

            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.commitFlow@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage()); // 修改message的值为报错信息
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
            int record_id = (int) request.get("record_id");
            int result = flowService.deleteFlow(record_id);
            Map<String, Object> response = new HashMap<>();
            if(result == 1) {
                response.put("code", 20000);
                response.put("message", true);
            }
            if(result == 0) {
                // TODO: 表示flow有对应的Instance，不能删除，可能可以调整返回码
                response.put("code", 40100);
                response.put("message", false);
            }
            else {
                response.put("code", 30400);
                response.put("message", false);
            }
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.saveFlowchart@deletion operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage()); // 修改message的值为报错信息
            return response;
        }
    }

    /**
     * 对应flow-更新流程-save/update接口
     * @param request
     * @return
     */
    @PostMapping(value = "/save")
    public Map<String, Object> saveFlow(@RequestBody Map<String, Object> request) {
        try {
            int flow_id = (int) request.get("flow_id");
            // 这里的graph_data和blackboard理论上是json，先用String存储，不手动处理
            String commit_message = (String) request.get("commit_message");
            Integer core_meta_id = (Integer) request.get("core_meta_id");
            Integer extra_meta_id = (Integer) request.get("extra_meta_id");
            String graph_data = (String) request.get("graph_data");
            String blackboard = (String) request.get("blackboard");
            String name = (String) request.get("name");
            String des = (String) request.get("des");
            Flow result = flowService.saveFlow(flow_id, commit_message, core_meta_id, extra_meta_id, graph_data, blackboard, name, des);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", result);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.saveFlowchart@saving operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage()); // 修改message的值为报错信息
            return response;
        }
    }

    @PostMapping(value="/detail")
    public Map<String, Object> getFlowDetails(@RequestBody Map<String, Integer> request) {
        try {
            int flow_id = request.get("flow_id");
            int version = request.get("version");
            Flow flow = flowService.getFlowDetails(flow_id, version);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowDetails@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage()); // 修改message的值为报错信息
            return response;
        }
    }
    
    @PostMapping(value="/list")
    public Map<String, Object> getFlowList() {
        try {
            List<Flow> flowlist = flowService.getFlowList();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 20000);
            response.put("data", flowlist);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowList@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage()); // 修改message的值为报错信息
            return response;
        }
    }
}
