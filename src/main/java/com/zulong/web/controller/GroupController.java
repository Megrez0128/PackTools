package com.zulong.web.controller;

import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.AuthenticationService;
import com.zulong.web.service.GroupService;
import com.zulong.web.utils.TokenUtils;
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
@RequestMapping(value = "/auth/group")
public class GroupController {
    private final GroupService groupService;
    private final AuthenticationService authenticationService;
    @Autowired
    public  GroupController(GroupService groupService,AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
        this.groupService = groupService;
    }

    @PostMapping(value = "/listuser")
    public Map<String, Object> getAllUsers(@RequestBody Map<String, String> request){
        Map<String, Object> response = new HashMap<>();
        if(authenticationService.isAdmin(TokenUtils.getCurr_user_id())){
        }else {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@this user is not admin|"));
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", "this user is not admin");
            return response;
        }

        Map<String, Object> data = new HashMap<>();
        int group_id;
        try{
            group_id = Integer.parseInt(request.get("group_id"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            List<User> userlist = groupService.getAllUsers(group_id);
            response.put("code", RETURN_SUCCESS);
            data.put("group_id", group_id);
            data.put("items", userlist);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]GroupController.getAllUsers@operation failed|", e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createGroup(@RequestBody Map<String, String> request) {
        String group_name;
        try{
            group_name = request.get("group_name");
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.createGroup@params are wrong|"), e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            Group return_group = groupService.createGroup(group_name);
            data.put("group_id", return_group.getGroup_id());
            data.put("group_name", return_group.getGroup_name());
            response.put("code", RETURN_SUCCESS);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.createGroup@operation failed|group_name=%s", group_name), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

}
