package com.zulong.web.controller;

import com.zulong.web.entity.Flow;
import com.zulong.web.entity.Instance;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.FlowService;
import com.zulong.web.service.InstanceService;
import com.zulong.web.utils.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.JsonObject;
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
        return ParamsUtil.getParam(request,varName,"InstanceController",functionName);
    }

    @PostMapping(value = "/report/start")
    public Map<String, Object> instanceStart(@RequestBody Map<String, Object> request) {
        String uuid;
        int flow_record_id;
        String start_time;
        boolean complete;
        boolean has_error;
        Map<String, Object> response = new HashMap<>();
        try {
            uuid = (String) getParam(request,"uuid","instanceStartNode");
            if(uuid == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "uuid is null");
            }
            start_time = (String) getParam(request,"start_time","instanceStartNode");
            if(start_time == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "start_time is null");
            }
            if( ParamsUtil.isInValidDateFormat(start_time)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "start_time is invalid");
            }
            Object tmp = getParam(request,"flow_record_id","instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_record_id is null");
            }
            flow_record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_record_id is invalid");
            }
//            tmp = getParam(request,"complete","instanceStartNode");
//            if(tmp == null) {
//                return errorResponse(response, RETURN_PARAMS_WRONG, "complete is null");
//            }
//            complete = (boolean) tmp;
            complete = false;
            tmp = getParam(request,"has_error","instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "has_error is null");
            }
            has_error = (boolean) tmp;

        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStart@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }

        //全局查询flow_id是否存在，并返回对应的flow
        try {
            Flow tmpFlow = flowService.findFlowByRecordId(flow_record_id);
            if(tmpFlow == null) {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStart@flow doesn't exist|flow_record_id=%d", flow_record_id));
                return errorResponse(response, RETURN_PARAMS_NULL, String.format("flow doesn't exist|flow_record_id = %d",flow_record_id));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStart@operation failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid), e);
            return errorResponse(response, RETURN_DATABASE_WRONG, e.getMessage());
        }
        try{
            boolean flag = instanceService.instanceStart(uuid, flow_record_id, start_time, complete, has_error);
            if(flag) {
                return successResponse(response,"success");
            }
            else {
                return errorResponse(response, RETURN_DATABASE_WRONG, "RETURN_DATABASE_WRONG");
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@create operation failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid), e);
            return errorResponse(response, RETURN_DATABASE_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/report/end")
    public Map<String, Object> instanceEnd(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        String uuid;
        int flow_record_id;
        String end_time;
        boolean complete;
        boolean has_error;
        try {
            uuid = (String) getParam(request,"uuid","instanceEndNode");
            if(uuid == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "uuid is null");
            }
            Object tmp = getParam(request,"flow_record_id","instanceEndNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_record_id is null");
            }
            flow_record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_record_id is invalid");
            }
            end_time = (String) getParam(request,"end_time","instanceStartNode");
            if(end_time == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "start_time is null");
            }
            if( ParamsUtil.isInValidDateFormat(end_time)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "end_time is invalid");
            }
//            tmp = getParam(request,"complete","instanceEndNode");
//            if(tmp == null) {
//                return errorResponse(response, RETURN_PARAMS_WRONG, "complete is null");
//            }
            complete = true;
            tmp = getParam(request,"has_error","instanceEndNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "has_error is null");
            }
            has_error = (boolean) tmp;

        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEnd@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
        //全局查询flow_id是否存在，并返回对应的flow
        try {
            Flow tmpFlow = flowService.findFlowByRecordId(flow_record_id);
            if(tmpFlow == null) {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEnd@flow doesn't exist|flow_record_id=%d", flow_record_id));
                return errorResponse(response, RETURN_PARAMS_NULL, String.format("Unable to find corresponding flow_record_id flow_id|flow_record_id=%d", flow_record_id));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEnd@create operation failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }

        try{
            boolean flag = instanceService.instanceEnd(uuid, flow_record_id, end_time, complete, has_error);
            if(flag) {
                return successResponse(response,"success");
            }
            else {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEnd@create operation failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid));
                return errorResponse(response, RETURN_DATABASE_WRONG, String.format("@database wrong,create operation failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEnd@create operation failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
    }

    @PostMapping(value = "/report/nodestart")
    public Map<String, Object> instanceStartNode(@RequestBody Map<String, Object> request){
        String uuid;
        int flow_record_id;
        String node_id ;
        String start_time;
        boolean complete;
        boolean has_error;
        String options;
        JsonObject optionJson = null;
        Map<String, Object> response = new HashMap<>();
        try {
            uuid = (String) getParam(request,"uuid","instanceStartNode");
            if(uuid == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "uuid is null");
            }
            node_id = (String) getParam(request,"node_id","instanceStartNode");
            if(node_id == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "node_id is null");
            }
            start_time = (String) getParam(request,"start_time","instanceStartNode");
            if(start_time == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "start_time is null");
            }
            if( ParamsUtil.isInValidDateFormat(start_time)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "start_time is invalid");
            }
            Object tmp = getParam(request,"flow_record_id","instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_record_id is null");
            }
            flow_record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_record_id is invalid");
            }
            tmp = getParam(request,"complete","instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "complete is null");
            }
            complete = (boolean) tmp;
            tmp = getParam(request,"has_error","instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "has_error is null");
            }
            has_error = (boolean) tmp;
            tmp = getParam(request, "options", "instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "options is null");
            }
            options = (String) tmp;

        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }

        //全局查询flow_id是否存在，并返回对应的flow
        try {
            Flow tmpFlow = flowService.findFlowByRecordId(flow_record_id);
            if(tmpFlow == null) {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@flow doesn't exist|flow_record_id=%d", flow_record_id));
                return errorResponse(response, RETURN_PARAMS_NULL, String.format("flow doesn't exist|flow_record_id = %d",flow_record_id));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@operation failed|flow_record_id=%d|node_id=%s", flow_record_id, node_id), e);
            return errorResponse(response, RETURN_DATABASE_WRONG, e.getMessage());
        }
        try{
            boolean flag = instanceService.instanceStartNode(uuid, flow_record_id, node_id, start_time, has_error, options);
            if(flag) {
                return successResponse(response,"success");
            }
            else {
                return errorResponse(response, RETURN_DATABASE_WRONG, "RETURN_DATABASE_WRONG");
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceStartNode@create operation failed|flow_record_id=%d|node_id=%s", flow_record_id, node_id), e);
            return errorResponse(response, RETURN_DATABASE_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/report/nodeend")
    public Map<String, Object> instanceEndNode(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        String uuid;
        int flow_record_id;
        String node_id;
        String end_time;
        boolean complete;
        boolean has_error;
        String options;
        try {
            uuid = (String) getParam(request,"uuid","instanceEndNode");
            if(uuid == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "uuid is null");
            }
            node_id = (String) getParam(request,"node_id","instanceEndNode");
            if(node_id == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "node_id is null");
            }
            end_time = (String) getParam(request,"end_time","instanceEndNode");
            if(end_time == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "end_time is null");
            }
            if( ParamsUtil.isInValidDateFormat(end_time)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "end_time is invalid");
            }
            Object tmp = getParam(request,"flow_record_id","instanceEndNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_record_id is null");
            }
            flow_record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_record_id is invalid");
            }
            tmp = getParam(request,"complete","instanceEndNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "complete is null");
            }
            complete = (boolean) tmp;
            tmp = getParam(request,"has_error","instanceEndNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "has_error is null");
            }
            has_error = (boolean) tmp;
            tmp = getParam(request, "options", "instanceStartNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "options is null");
            }
            options = (String) tmp;

        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
        //全局查询flow_id是否存在，并返回对应的flow
        try {
            Flow tmpFlow = flowService.findFlowByRecordId(flow_record_id);
            if(tmpFlow == null) {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@flow doesn't exist|flow_record_id=%d", flow_record_id));
                return errorResponse(response, RETURN_PARAMS_NULL, String.format("Unable to find corresponding flow_record_id flow_id|flow_record_id=%d", flow_record_id));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@create operation failed|flow_record_id=%d|node_id=%s", flow_record_id, node_id), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
        // 创建instance
        try{
            boolean flag = instanceService.instanceEndNode(uuid, flow_record_id, node_id, end_time, complete,has_error, options);
            if(flag) {
                return successResponse(response,"success");
            }
            else {
                LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@create operation failed|flow_record_id=%d|node_id=%s", flow_record_id, node_id));
                return errorResponse(response, RETURN_DATABASE_WRONG, String.format("@database wrong,create operation failed|flow_record_id=%d|node_id=%s", flow_record_id, node_id));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.instanceEndNode@create operation failed|flow_record_id=%d|node_id=%s", flow_record_id, node_id), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
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
            uuid = (String) getParam(request,"uuid","instanceEndNode");
            if(uuid == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "uuid is null");
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.UpdateInstance@pulling failed, params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
        try {
            instanceAndNodeList = instanceService.findInstanceByUuid(uuid);
            response.put("uuid", uuid);
            return successResponse(response,instanceAndNodeList);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.UpdateInstance@pulling instance failed|uuid=%s", uuid), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/list")
    public Map<String, Object> getInstanceList(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        int flow_record_id;
        try {
            Object tmp = getParam(request,"flow_record_id","instanceEndNode");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_NULL, "flow_record_id is null");
            }
            flow_record_id = (int) tmp;
            if( ParamsUtil.isInValidInt(flow_record_id)){
                return errorResponse(response, RETURN_PARAMS_WRONG, "flow_record_id is invalid");
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.getInstanceList@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_NULL, e.getMessage());
        }
        try {
            List<Instance> instancelist = instanceService.findInstanceByFlowRecordId(flow_record_id);
            data.put("items", instancelist);
            return successResponse(response,data);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller]InstanceController.getInstanceList@cannot get instance list|flow_record_id=%d", flow_record_id), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }
}
