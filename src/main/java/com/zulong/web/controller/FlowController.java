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

        int meta_id;
        String graph_data;
        String blackboard;
        int group_id;
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {

            Object tmp = getParam(request,"meta_id","createFlow");
            if(tmp == null) {
                  return errorResponse(response, RETURN_PARAMS_NULL, "meta_id is null");
            }
            meta_id =(int) tmp;
            if( ParamsUtil.isInValidMetaId(meta_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "meta_id is invalid");
            }
            graph_data =(String) getParam(request,"graph_data","createFlow");
            if(graph_data == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "graph_data is null");
            }
            blackboard  = (String) getParam(request,"blackboard","createFlow");
            if(blackboard == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "blackboard is null");
            }
            tmp = getParam(request,"group_id","createFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "group_id is null");
            }
            group_id  =(int) tmp;
            if( ParamsUtil.isInValidInt(group_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is invalid");
            }
            name = (String) getParam(request,"name","createFlow");
            if(name == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "name is null");
            }
            des = (String) getParam(request,"des","createFlow");
            if(des == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "des is null");
            }
        } catch(Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.createFlow@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }

        try{
            boolean flag = authenticationService.isUserInGroup(TokenUtils.getCurrUserId(token), group_id);
            if(!flag){
                LoggerManager.logger().error("[com.zulong.web.controller]FlowController.createFLow@this user may not in the group|");
                response = new HashMap<>();
                response.put("code", RETURN_PARAMS_NULL);
                response.put("message", "this user may not in the group");
                return response;
            }
        }catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.createFlow@this user may not in the group|", e);
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

        // 暂时没有设置clone的权限

        int record_id;
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"record_id","cloneFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "record_id is null");
            }
            record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "record_id is invalid");
            }
            name = (String) getParam(request,"name","cloneFlow");
            if(name == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "name is null");
            }

            des = (String) getParam(request,"des","cloneFlow");
            if(des == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "des is null");
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.cloneFlow@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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

        // 暂时没有设置commit的权限

        int record_id;
        String commit_message;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"record_id","commitFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "record_id is null");
            }
            record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "record_id is invalid");
            }
            commit_message = (String)  getParam(request,"commit_message","commitFlow");
            if(commit_message == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "commit_message is null");
            }
        } catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.commitFlow@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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
     * 接口已废弃，如需恢复使用，需参照saveFlow验证用户有无删除Flow的权限
     * @param request
     * @param token
     * @return
     */
    @PostMapping(value = "/delete")
    public Map<String, Object> deleteFlow(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {

        int record_id;
        Map<String, Object> response = new HashMap<>();
        try {
            Object tmp = getParam(request,"record_id","deleteFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "record_id is null");
            }
            record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "record_id is invalid");
            }
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.deleteFlow@params record_id is wrong|", e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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

        // 需要验证用户有无修改权限

        int flow_id;
//        String commit_message;
        int meta_id = DEFAULT_META_ID;
        String graph_data;
        String blackboard;
        String name;
        String des;
        Map<String, Object> response = new HashMap<>();
        try {
            name = (String)  getParam(request,"name","saveFlow");
            des = (String)  getParam(request,"des","saveFlow");
            Object tmp = getParam(request,"flow_id","saveFlow");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_id is null");
            }
            flow_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is invalid");
            }
            tmp = getParam(request,"meta_id","saveFlow");
            if(tmp != null) {
                meta_id = (int) tmp;
            }
            if( ParamsUtil.isInValidMetaId(meta_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "meta_id is invalid");
            }

            graph_data = (String) getParam(request,"graph_data","saveFlow");
            blackboard = (String) getParam(request,"blackboard","saveFlow");
//            if(blackboard == null) {
//                return errorResponse(response, RETURN_PARAMS_NULL, "blackboard is null");
//            }
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.saveFlow@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }

        String user_id;
        try{
            user_id = TokenUtils.getCurrUserId(token);
            if(user_id == null) {
                return errorResponse(response, RETURN_TOKEN_EXPIRED, "The token has expired, get user_id by token failed");
            }
        }catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]UserController.saveFlow@current token is invalid|token=%s", token), e);
            return errorResponse(response, RETURN_USER_NOT_EXISTS, e.getMessage());
        }

        // 验证用户有无修改权限
        boolean has_auth = authenticationService.canUserUpdateFlow(user_id,flow_id);
        if(!has_auth){
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]UserController.saveFlow@current token is invalid|user_id = %s|flow_id = %d",user_id,flow_id));
            return errorResponse(response,RETURN_NO_AUTHORITY,String.format("this user has not permission to update the flow|user_id = %s|flow_id = %d",user_id,flow_id));
        }

        try {
            Flow flow = flowService.saveFlow(name, des, flow_id, meta_id, graph_data, blackboard);
            if(flow == null) {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.saveFlow@saving operation failed|flow_id=%d", flow_id));
                return errorResponse(response, RETURN_DATABASE_WRONG, "failed");
            }
            Map<String,Object> result = new HashMap<>();
            result.put("record_id",flow.getFlow_id());
            result.put("flow_id",flow.getFlow_id());
            result.put("version",flow.getVersion());
            result.put("committed",flow.isCommitted());
            result.put("commit_message",flow.getCommit_message());
            result.put("last_build",flow.getLast_build());
            result.put("save_time",flow.getSave_time());
            result.put("meta_id",flow.getMeta_id());
            result.put("graph_data",flow.getGraph_data());
            result.put("blackboard",flow.getBlackboard());
            result.put("name",name);
            result.put("des",des);
            return successResponse(response, result);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.saveFlow@saving operation failed|flow_id=%d", flow_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    /**
     *
     * @param request
     * @return 返回record_id最大的Flow的详细信息
     */
    @PostMapping(value = "/maxrecord")
    public Map<String ,Object> getMaxRecordIdDetails(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        int flow_id;
        try {
            Object tmp = getParam(request,"flow_id","getMaxRecordIdDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_id is null");
            }
            flow_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is invalid");
            }
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
        int record_id;
        Map<String, Object> response = new HashMap<>();
        record_id = (int) request.getOrDefault("record_id", -1);
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
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_id is null");
            }
            flow_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is invalid");
            }
            tmp = getParam(request,"version","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "version is null");
            }
            version = (int) tmp;
            if( ParamsUtil.isInValidInt(version)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "version is invalid");
            }
        } catch (Exception e) {
            LoggerManager.logger().error("[com.zulong.web.controller]FlowController.getFlowDetails@params are wrong|", e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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
                return errorResponse(response, RETURN_PARAMS_NULL, "group_id is null");
            }
            group_id = (int) tmp;
            if( ParamsUtil.isInValidInt(group_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is invalid");
            }
        }catch (Exception e) {
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
        //判断这个curr_user_id是否属于这个group
        try {
            boolean flag = authenticationService.isUserInGroup(TokenUtils.getCurrUserId(token), group_id);
            if(!flag){
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowSummaryList@this user may not in the group|group_id=%d|user_id=%s",group_id, TokenUtils.getCurrUserId(token)));
                return errorResponse(response, RETURN_PARAMS_NULL, "this user may not in the group");
            }
        }catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]FlowController.getFlowSummaryList@this user may not in the group|group_id=%d", group_id), e);
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

        Map<String, Object> response = new HashMap<>();
        int flow_id;

        try {
            Object tmp = getParam(request,"flow_id","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_id is null");
            }
            flow_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is invalid");
            }
        }catch (Exception e){
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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

        Map<String, Object> response = new HashMap<>();
        int flow_id;
        try {
            Object tmp = getParam(request,"flow_id","getFlowDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_id is null");
            }
            flow_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_id is invalid");
            }
        }catch (Exception e){
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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
