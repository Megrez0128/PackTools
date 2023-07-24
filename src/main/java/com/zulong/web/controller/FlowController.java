package com.zulong.web.controller;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.FlowSummary;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.AuthenticationService;
import com.zulong.web.service.FlowService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zulong.web.utils.TokenUtils;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.zulong.web.config.ConstantConfig.*;

@RestController
@RequestMapping(value = "/myflow")
public class FlowController
{
    private final FlowService flowService;
    private final AuthenticationService authenticationService;

    @Autowired
    public FlowController(FlowService flowService,AuthenticationService authenticationService)
    {
        LoggerManager.init();
        this.flowService = flowService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createFlow(@RequestBody Map<String, String> request) {

        int meta_id; //todo:验证这个meta是否属于这个group
        String graph_data;
        String blackboard;
        int group_id; //todo:验证这个user是否属于这个group
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {
            meta_id = Integer.parseInt(request.get("meta_id"));
            graph_data = request.get("graph_data");
            blackboard = request.get("blackboard");
            group_id = Integer.parseInt(request.get("group_id"));
            name =  request.get("name");
            des =  request.get("des");
            LoggerManager.logger().debug(String.format("[com.zulong.web.controller]FlowController.createFLow@ success receive post|"));
        } catch(Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.createFlow@params are wrong|", e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }

        //判断这个curr_user_id是否属于这个group
        try{
            boolean flag = authenticationService.isUserInGroup(TokenUtils.getCurr_user_id(),group_id);
            if(!flag){
                LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.createFLow@this user may not in the group|");
                response = new HashMap<>();
                response.put("code", RETURN_PARAMS_WRONG);
                response.put("message", "this user may not in the group");
                return response;
            }
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowList@this user may not in the group|", e);
            response = new HashMap<>();
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }

        try {
            Flow flow = flowService.createFlow(graph_data, blackboard, meta_id, group_id, name, des);
            response.put("code", RETURN_SUCCESS);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.createFlow@operation failed|", e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }


    @PostMapping(value = "/clone")
    public Map<String, Object> cloneFlow(@RequestBody Map<String, Object> request) {
        //todo:验证curr_user_id是否有clone这个flow的权限
        //authenticationService.canUserUseFlow()

        int record_id;
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {
            record_id = (int) request.get("record_id");
            name = (String) request.get("name");
            des = (String) request.get("des");
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]FlowController.cloneFlow@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Flow flow = flowService.cloneFlow(record_id, name, des);
            response.put("code", RETURN_SUCCESS);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]FlowController.cloneFlow@operation failed|record_id=%d", record_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/commit")
    public Map<String, Object> commitFlow(@RequestBody Map<String, Object> request) {
        //todo:验证curr_user_id是否有commit这个flow的权限
        int record_id;
        String commit_message;
        try {
            record_id = (int) request.get("record_id");
            commit_message = (String) request.get("commit_message");
        } catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.commitFlow@params are wrong|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Flow flow = flowService.commitFlow(record_id, commit_message);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SUCCESS);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.commitFlow@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
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
        //todo:验证curr_user_id是否有删除这个flow的权限
        int record_id;
        Map<String, Object> response = new HashMap<>();
        try {
            record_id = (int) request.get("record_id");
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.deleteFlow@params are wrong|", e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            int result = flowService.deleteFlow(record_id);
            if(result == DELETE_SUCCESS) {
                response.put("code", RETURN_SUCCESS);
                response.put("message", true);
            }
            else if(result == DELETE_HAVE_INSTANCE) {
                response.put("code", RETURN_EXIST_INSTANCE);
                response.put("message", false);
            }
            else {
                response.put("code", RETURN_DATABASE_WRONG);
                response.put("message", false);
            }
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]FlowController.deleteFlow@deletion operation failed|record_id=%d", record_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
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
        //todo:验证curr_user_id是否有保存这个flow的权限

        //todo:验证curr_user_id是否属于这个group

        //todo:验证这个meta是否属于这个group
        int flow_id;
        String commit_message;
        int meta_id;
        String graph_data;
        String blackboard;
        Map<String, Object> response = new HashMap<>();
        try {
            flow_id = (int) request.get("flow_id");
            commit_message = (String) request.get("commit_message");
            meta_id = (Integer) request.get("meta_id");
            graph_data = (String) request.get("graph_data");
            blackboard = (String) request.get("blackboard");
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.saveFlow@params are wrong|", e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Flow result = flowService.saveFlow(flow_id, commit_message, meta_id, graph_data, blackboard);
            response.put("code", RETURN_SUCCESS);
            response.put("data", result);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]FlowController.saveFlow@saving operation failed|flow_id=%d", flow_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    /**
     *
     * @param request
     * 当传入record_id，调用getFlowDetailsByID
     * 当传入flow_id和version，调用getFlowDetails
     * @return
     * 返回一个Flow实例
     */
    @PostMapping(value="/detail")
    public Map<String, Object> getFlowDetails(@RequestBody Map<String, Integer> request) {
        //todo:验证curr_user_id是否有访问这个flow历史的权限，或许看的权限是所有人都有的，不需要验证
        int record_id;
        Map<String, Object> response = new HashMap<>();
        record_id = request.getOrDefault("record_id",-1);
        if(record_id != -1){
            try {
                Flow flow = flowService.getFlowDetailsByID(record_id);
                response.put("code", RETURN_SUCCESS);
                response.put("data", flow);
                return response;
            } catch (Exception e) {
                LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowDetails@operation failed|", e);
                response.put("code", RETURN_SERVER_WRONG);
                response.put("message", e.getMessage());
                return response;
            }
        }

        int flow_id;
        int version;
        try {
            flow_id = request.get("flow_id");
            version = request.get("version");
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowDetails@params are wrong|", e);

            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Flow flow = flowService.getFlowDetails(flow_id, version);
            response.put("code", RETURN_SUCCESS);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowDetails@operation failed|", e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value="/list")
    public Map<String, Object> getFlowSummaryList(@RequestBody Map<String, Integer> request) {
        int group_id;
        try{
            group_id = request.get("group_id");
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowList@params are wrong|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        //判断这个curr_user_id是否属于这个group
        try{
            boolean flag = authenticationService.isUserInGroup(TokenUtils.getCurr_user_id(),group_id);
            if(!flag){
                LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowList@this user may not in the group|");
                Map<String, Object> response = new HashMap<>();
                response.put("code", RETURN_PARAMS_WRONG);
                response.put("message", "this user may not in the group");
                return response;
            }
        }catch (Exception e){
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowList@this user may not in the group|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }

        try {
            List<FlowSummary> flowSummarylist = flowService.getFlowSummaryList(group_id);
            Map<String, Object> data = new HashMap<>();
            data.put("items", flowSummarylist);
            data.put("group_id", group_id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SUCCESS);
            response.put("data", data);
            LoggerManager.logger().info("[com.zulong.web.controller]FlowController.getFlowList@operation success|");
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowList@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value="/history")
    public Map<String, Object> getFlowHistoryList(@RequestBody Map<String, Integer> request) {
        //todo:验证curr_user_id是否有访问这个flow历史的权限，或许看的权限是所有人都有的，不需要验证
        try {
            int flow_id = request.get("flow_id");
            List<Flow> flowlist = flowService.getHistoryFlowList(flow_id);
            Map<String, Object> data = new HashMap<>();
            data.put("items", flowlist);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SUCCESS);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getFlowHistoryList@cannot get history list|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/newversion")
    public Map<String, Object> getNewVersionFlow(@RequestBody Map<String, Integer> request) {
        //todo:验证curr_user_id是否有访问这个flow最新版本的权限，或许看的权限是所有人都有的，不需要验证
        Map<String, Object> response = new HashMap<>();
        try {
            int flow_id = request.get("flow_id");
            Flow flow = flowService.getNewVersionFlow(flow_id);
            response.put("code", RETURN_SUCCESS);
            response.put("data", flow);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]FlowController.getNewVersionFlow@cannot get the flow|", e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }
}
