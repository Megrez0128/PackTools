package com.zulong.web.controller;

import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.AuthenticationService;
import com.zulong.web.service.GroupService;
import com.zulong.web.utils.ParamsUtil;
import com.zulong.web.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zulong.web.config.ConstantConfig.*;

@RestController
@RequestMapping(value = "/auth/group")
public class GroupController {
    private final GroupService groupService;
    private final AuthenticationService authenticationService;
    @Autowired
    public  GroupController(GroupService groupService,AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
        this.groupService = groupService;
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
        return ParamsUtil.getParam(request,varName,"GroupController",functionName);
    }

    @PostMapping(value = "/listuser")
    public Map<String, Object> getAllUsers(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token){
        Map<String, Object> response = new HashMap<>();
        if(authenticationService.isAdmin(TokenUtils.getCurrUserId(token))){
        }else {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@this user is not admin|user_id = %s",TokenUtils.getCurrUserId(token)));
            return errorResponse(response, RETURN_SERVER_WRONG, String.format("this user is not admin|user_id = %s",TokenUtils.getCurrUserId(token)));
        }

        Map<String, Object> data = new HashMap<>();
        int group_id;
        try{
            Object tmp = getParam(request,"group_id","getAllUsers");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is null");
            }
            group_id = (int) tmp;
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            List<User> userlist = groupService.getAllUsers(group_id);
            data.put("group_id", group_id);
            data.put("items", userlist);
            return successResponse(response, data);
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]GroupController.getAllUsers@operation failed|", e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createGroup(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        String group_name;
        try{
            group_name = (String) getParam(request,"group_name","createGroup");
            if(group_name == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_name is null");
            }
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.createGroup@params are wrong|"), e);
            return errorResponse(response, RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Map<String, Object> data = new HashMap<>();
            Group return_group = groupService.createGroup(group_name);
            data.put("group_id", return_group.getGroup_id());
            data.put("group_name", return_group.getGroup_name());
            return successResponse(response, data);
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.createGroup@operation failed|group_name=%s", group_name), e);
            return errorResponse(response, RETURN_SERVER_WRONG, e.getMessage());
        }
    }

}
