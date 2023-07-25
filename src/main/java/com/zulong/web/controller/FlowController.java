package com.zulong.web.controller;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.FlowSummary;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.AuthenticationService;
import com.zulong.web.service.FlowService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zulong.web.utils.ParamsUtil;
import com.zulong.web.utils.TokenUtils;
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

    public static Map<String, Object> errorResponse(final Map<String, Object> response, int errorCode, final String message) {
        response.put("code", errorCode);
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> successResponse(final Map<String, Object> response, final Object data) {
        response.put("code", RETURN_SUCCESS);
        response.put("data", data);
        return response;
    }

    public static Object getParam(Map<String, Object> request, String varName, String functionName){
        return ParamsUtil.getParam(request,varName,"FlowController", functionName);
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createFlow(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {

        int meta_id; //todo:验证这个meta是否属于这个group
        String graph_data;
        String blackboard;
        int group_id;
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {
            //meta_id = Integer.parseInt(request.get("meta_id"));
            Object tmp = getParam(request,"meta_id","createFlow");
            if(tmp == null) {
                  return errorResponse(response, RETURN_PARAMS_WRONG, "meta_id is null");
            }
            meta_id =(int) tmp;
            graph_data =(String) getParam(request,"graph_data","createFlow");
            if(graph_data == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "graph_data is null");
            }
            blackboard  = (String) getParam(request,"blackboard","createFlow");
            if(blackboard == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "blackboard is null");
            }
            tmp = getParam(request,"group_id","createFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is null");
            }
            group_id  =(int) tmp;
            name = (String) getParam(request,"name","createFlow");
            if(name == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "name is null");
            }
            des = (String) getParam(request,"des","createFlow");
            if(des == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "des is null");
            }
        } catch(Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.createFlow@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }

        //have done:判断这个curr_user_id是否属于这个group
        try{
            boolean flag = authenticationService.isUserInGroup(TokenUtils.getCurrUserId(token), group_id);
            if(!flag){
                LoggerManager.logger().error("[com.zulong.web.controller]FlowController.createFLow@this user may not in the group|");
                response = new HashMap<>();
                response.put("code", RETURN_PARAMS_WRONG);
                response.put("message", "this user may not in the group");
                return response;
            }
        }catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.getFlowList@this user may not in the group|", e);
            response = new HashMap<>();
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }

        try {
            Flow flow = flowService.createFlow(graph_data, blackboard, meta_id, group_id, name, des);
            return successResponse(response, flow);
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.createFlow@operation failed|", e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }


    @PostMapping(value = "/clone")
    public Map<String, Object> cloneFlow(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        //todo:验证curr_user_id是否有clone这个flow的权限
        //authenticationService.canUserUseFlow()

        int record_id;
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"record_id","cloneFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "record_id is null");
            }
            record_id = (int) tmp;

            name = (String) getParam(request,"name","cloneFlow");
            if(name == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "name is null");
            }

            des = (String) getParam(request,"des","cloneFlow");
            if(des == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "des is null");
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.cloneFlow@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Flow flow = flowService.cloneFlow(record_id, name, des);
            return successResponse(response, flow);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.cloneFlow@operation failed|record_id=%d", record_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/commit")
    public Map<String, Object> commitFlow(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        //todo:验证curr_user_id是否有commit这个flow的权限
        int record_id;
        String commit_message;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"record_id","commitFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "record_id is null");
            }
            record_id = (int) tmp;
            commit_message = (String)  getParam(request,"commit_message","commitFlow");
            if(commit_message == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "commit_message is null");
            }
        } catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.commitFlow@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Flow flow = flowService.commitFlow(record_id, commit_message);
            return successResponse(response, flow);
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.commitFlow@operation failed|", e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }
    
    /**
     * 删除Flow，先验证有无实例，否则不能删除
     *
     * @param request
     * @param token
     * @return
     */
    @PostMapping(value = "/delete")
    public Map<String, Object> deleteFlow(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        //todo:验证curr_user_id是否有删除这个flow的权限
        int record_id;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"record_id","deleteFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "record_id is null");
            }
            record_id = (int) tmp;
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.deleteFlow@params record_id is wrong|", e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
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
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.deleteFlow@deletion operation failed|record_id=%d", record_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    /**
     * 对应flow-更新流程-save/update接口
     * @param request
     * @param token
     * @return
     */
    @PostMapping(value = "/save")
    public Map<String, Object> saveFlow(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
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
            Object tmp = getParam(request,"flow_id","saveFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is null");
            }
            flow_id = (int) tmp;
            tmp = getParam(request,"meta_id","saveFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "meta_id is null");
            }
            meta_id = (int) tmp;
            commit_message = (String) getParam(request,"commit_message","saveFlow");
            if(commit_message == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "commit_message is null");
            }
            graph_data = (String) getParam(request,"graph_data","saveFlow");
            if(graph_data == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "graph_data is null");
            }
            blackboard = (String) getParam(request,"blackboard","saveFlow");
            if(blackboard == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "blackboard is null");
            }
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.saveFlow@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Flow result = flowService.saveFlow(flow_id, commit_message, meta_id, graph_data, blackboard);
            return successResponse(response, result);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.saveFlow@saving operation failed|flow_id=%d", flow_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    /**
     *
     * @param request
     * @return 所有中的最大
     */
    @PostMapping(value = "/maxrecord")
    public Map<String ,Object> getMaxRecordIdDetails(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        int flow_id;
        try {
            Object tmp = getParam(request,"flow_id","saveFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is null");
            }
            flow_id = (int) tmp;
            Flow flow = flowService.getNewestFlow(flow_id);
            return successResponse(response, flow);
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.getNewVersionFlow@cannot get the flow|", e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
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
    public Map<String, Object> getFlowDetails(@RequestBody Map<String, Object> request) {
        //todo:验证curr_user_id是否有访问这个flow历史的权限，或许看的权限是所有人都有的，不需要验证
        int record_id;
        Map<String, Object> response = new HashMap<>();
        record_id = (int) request.getOrDefault("record_id", -1);
//        record_id = Integer.parseInt(recordIdStr);
        if(record_id != -1){
            try {
                Flow flow = flowService.getFlowDetailsByID(record_id);
                return successResponse(response, flow);
            } catch (Exception e) {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowDetails@operation failed|record_id=%d", record_id), e);
                return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
            }
        }

        int flow_id;
        int version;
        try {
            Object tmp = getParam(request,"flow_id","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is null");
            }
            flow_id = (int) tmp;
            tmp = getParam(request,"version","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "version is null");
            }
            version = (int) tmp;
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.getFlowDetails@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Flow flow = flowService.getFlowDetails(flow_id, version);
            if(flow == null) {
                return errorResponse(response, RETURN_NO_OBJECT, "the flow doesn't exist");
            }
            return successResponse(response, flow);
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.getFlowDetails@operation failed|", e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value="/list")
    public Map<String, Object> getFlowSummaryList(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        int group_id;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"group_id","getFlowSummaryList");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is null");
            }
            group_id = (int) tmp;
        }catch (Exception e) {
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        //判断这个curr_user_id是否属于这个group
        try {
            boolean flag = authenticationService.isUserInGroup(TokenUtils.getCurrUserId(token), group_id);
            if(!flag){
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowSummaryList@this user may not in the group|group_id=%d|user_id=%s",group_id, TokenUtils.getCurrUserId(token)));
                return errorResponse(response, RETURN_PARAMS_WRONG, "this user may not in the group");
            }
        }catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowList@this user may not in the group|group_id=%d", group_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }

        try {
            List<FlowSummary> flowSummarylist = flowService.getFlowSummaryList(group_id);
            Map<String, Object> data = new HashMap<>();
            data.put("items", flowSummarylist);
            data.put("group_id", group_id);
            return successResponse(response, data);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowSummaryList@operation failed|group_id=%d", group_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value="/history")
    public Map<String, Object> getFlowHistoryList(@RequestBody Map<String, Object> request) {
        //todo:验证curr_user_id是否有访问这个flow历史的权限，或许看的权限是所有人都有的，不需要验证
        Map<String, Object> response = new HashMap<>();
        int flow_id;


        try {
            Object tmp = getParam(request,"flow_id","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is null");
            }
            flow_id = (int) tmp;
        }catch (Exception e){
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }

        try {
            List<Flow> flowlist = flowService.getHistoryFlowList(flow_id);
            Map<String, Object> data = new HashMap<>();
            data.put("items", flowlist);

            return successResponse(response, data);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowHistoryList@cannot get history list|flow_id=%d", flow_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    /**
     *
     * @param request
     * @return 已发布中的最大
     */
    @PostMapping(value = "/newversion")
    public Map<String, Object> getNewVersionFlow(@RequestBody Map<String, Object> request) {
        //todo:验证curr_user_id是否有访问这个flow最新版本的权限，或许看的权限是所有人都有的，不需要验证
        Map<String, Object> response = new HashMap<>();
        int flow_id;
        try {
            Object tmp = getParam(request,"flow_id","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is null");
            }
            flow_id = (int) tmp;
        }catch (Exception e){
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }

        try {
            Flow flow = flowService.getNewVersionFlow(flow_id);
            return successResponse(response, flow);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getNewVersionFlow@cannot get the flow|flow_id = %d",flow_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }
}
