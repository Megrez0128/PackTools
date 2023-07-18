package com.zulong.web.controller;

import com.zulong.web.entity.Group;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static com.zulong.web.config.ConstantConfig.*;

@RestController
@RequestMapping(value = "/auth/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){ this.userService = userService; }

    @PostMapping(value = "/list")
    public Map<String, Object> getAllGroups(@RequestBody Map<String, String> request) {
        String user_id;
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try{
            user_id = request.get("user_id");
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            List<Group> grouplist = userService.getAllGroups(user_id);
            data.put("items", grouplist);
            data.put("user_id", user_id);
            response.put("code", RETURN_SUCCESS);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createUser(@RequestBody Map<String, String> request) {
        String user_id;
        boolean is_admin;
        Map<String, Object> response = new HashMap<>();
        try{
            user_id = request.get("user_id");
            is_admin = Boolean.parseBoolean(request.get("is_admin"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            userService.createUser(user_id, is_admin);
            response.put("code", RETURN_SUCCESS);
            response.put("message", "success");
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/addtogroup")
    public Map<String, Object> addToGroup(@RequestBody Map<String, String> request) {
        String user_id;
        int group_id;
        int code;
        String message;
        try{
            user_id = request.get("user_id");
            group_id = Integer.parseInt(request.get("group_id"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@params are wrong|"), e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Map<String, Object> response = new HashMap<>();
            boolean flag = userService.addToGroup(user_id, group_id);
            if(flag) { code = RETURN_SUCCESS; message = "success";}
            else { code = RETURN_DATABASE_WRONG; message = "failed";}
            response.put("code", code);
            response.put("message", message);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.addToGroup@operation failed|user_id=%s|group_id=%d", user_id, group_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/removefromgroup")
    public Map<String, Object> removeFromGroup(@RequestBody Map<String, String> request) {
        String user_id;
        int group_id;
        Map<String, Object> response = new HashMap<>();
        try{
            user_id = request.get("user_id");
            group_id = Integer.parseInt(request.get("group_id"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            boolean flag = userService.removeFromGroup(user_id, group_id);
            int code;
            String message;
            if(flag) { code = RETURN_SUCCESS; message = "success";}
            else {
                LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.removeFromGroup@operation failed|user_id=%s|group_id=%d", user_id, group_id));
                code = RETURN_DATABASE_WRONG;
                message = "failed";
            }
            response.put("code", code);
            response.put("message", message);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.removeFromGroup@operation failed|user_id=%s|group_id=%d", user_id, group_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

}
