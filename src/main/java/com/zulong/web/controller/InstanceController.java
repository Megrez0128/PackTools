package com.zulong.web.controller;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.Instance;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;
import com.zulong.web.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zulong.web.config.ConstantConfig.*;

@RestController
@RequestMapping(value = "/myinstance")
public class InstanceController {
    private final InstanceService instanceService;
    private FlowService flowService;
    @Autowired
    public InstanceController(InstanceService instanceService, FlowService flowService)
    {
        this.instanceService = instanceService;
        this.flowService = flowService;
    }

    @PostMapping(value = "/report/start")
    public Map<String, Object> instanceStartNode(@RequestBody Map<String, Object> request){
        String uuid = null;
        int flow_record_id = 0;
        String node_id = null;
        String start_time = null;
        boolean complete = false;
        boolean has_error = false;
        String option = null;
        try {
            uuid = (String) request.get("uuid");
            flow_record_id = (int)request.get("flow_record_id");
            node_id = (String) request.get("node_id");
            start_time = (String)request.get("start_time");
            complete = (boolean)request.get("complete");
            has_error = (boolean)request.get("has_error");
            option = (String)request.get("option");
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@params are wrong|"), e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }

        //全局查询flow_id是否存在，并返回对应的flow
        try {
            Flow tmpFlow = flowService.findFlowByRecordId(flow_record_id);
            if(tmpFlow == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", RETURN_PARAMS_WRONG);
                response.put("message", "flow doesn't exist");
                LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@flow doesn't exist|flow_id=%d", flow_record_id));
                return response;
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("data", e.getMessage());
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@operation failed|flow_id=%d|node_id=%s", flow_record_id, node_id), e);
            return response;
        }
        try{
            instanceService.instanceStartNode(uuid, flow_record_id, node_id,start_time,complete,has_error, option);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SUCCESS);
            response.put("message", "success");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 40000);
            response.put("message", e.getMessage());
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@create operation failed|flow_id=%d|node_id=%s", flow_record_id, node_id), e);
            return response;
        }
    }

    @PostMapping(value = "/report/end")
    public Map<String, Object> instanceEndNode(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        String uuid = null;
        int flow_record_id = 0;
        String node_id = null;
        String end_time = null;
        boolean complete = false;
        boolean has_error = false;
        String option = null;
        try {
            uuid = (String)request.get("uuid");
            flow_record_id = (int)request.get("flow_record_id");
            node_id = (String)request.get("node_id");
            end_time = (String)request.get("end_time");
            complete = (boolean)request.get("complete");
            has_error = (boolean)request.get("has_error");
            option = (String)request.get("option");
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@params are wrong|"), e);;
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }

        //全局查询flow_id是否存在，并返回对应的flow
        try {
            Flow tmpFlow = flowService.findFlowByRecordId(flow_record_id);
            if(tmpFlow == null) {
                response.put("code", RETURN_PARAMS_WRONG);
                response.put("message", "failed");
                LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@flow doesn't exist|flow_id=%d", flow_record_id));
                return response;
            }
        } catch (Exception e) {
            response.put("code", 40000);
            response.put("message", e.getMessage());
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@create operation failed|flow_id=%d|node_id=%s", flow_record_id, node_id), e);
            return response;
        }
        // 创建instance
        try{
            boolean flag = instanceService.instanceEndNode(uuid, flow_record_id, node_id, end_time, complete,has_error, option);
            if(flag) {
                response.put("code", RETURN_SUCCESS);
                response.put("message", "success");
                return response;
            } else {
                response.put("code", RETURN_PARAMS_WRONG);
                response.put("message", "failed");
                return response;
            }
        } catch (Exception e) {
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("data", e.getMessage());
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@create operation failed|flow_id=%d|node_id=%s", flow_record_id, node_id), e);
            return response;
        }
    }

    /**
     * 轮询，每隔一段时间发送一次拉取请求
     * 返回时和node相关的信息用startNode和endNode两个函数处理
     * @param request
     */
    @PostMapping(value = "/build")
    public Map<String, Object> pullInstance(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        String uuid;
        Map<String,Object> instanceAndNodeList;
        try {
            uuid = (String) request.get("uuid");
        } catch (Exception e) {
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("data", null);
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.UpdateInstance@pulling failed, params are wrong|"), e);
            return response;
        }
        try {
            instanceAndNodeList = instanceService.findInstanceByUuid(uuid);
            response.put("code", RETURN_SUCCESS);
            response.put("uuid", uuid);
            response.put("data", instanceAndNodeList);
            return response;
        } catch (Exception e) {
            response.put("code", RETURN_SERVER_WRONG);
            response.put("data", null);
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.UpdateInstance@pulling instance failed|uuid=%d", uuid), e);
            return response;
        }
    }

    @PostMapping(value = "/list")
    public Map<String, Object> getInstanceList(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        int flow_record_id;
        try {
            flow_record_id = (Integer) request.get("flow_record_id");
        } catch (Exception e) {
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("data", null);
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.getInstanceList@params are wrong|"), e);
            return response;
        }
        try {
            List<Instance> instancelist = instanceService.findInstanceByFlowRecordId(flow_record_id);
            data.put("items", instancelist);
            response.put("code", RETURN_SUCCESS);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("data", null);
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]InstanceController.getInstanceList@cannot get instance list|flow_record_id=%d", flow_record_id), e);
            return response;
        }

    }

}
