package com.zulong.web.controller;

import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){ this.userService = userService; }

    @PostMapping(value = "/list")
    public Map<String, Object> getAllGroups(@RequestBody Map<String, String> request) {
        try {
            String user_id = request.get("user_id");
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            List<Integer> grouplist = userService.getAllGroups(user_id);
            data.put("items", grouplist);
            data.put("user_id", user_id);
            response.put("code", 20000);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]UserController.getAllGroups@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createUser(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> response = new HashMap<>();
            String user_id = request.get("user_id");
            boolean is_admin = Boolean.parseBoolean(request.get("is_admin"));
            userService.createUser(user_id, is_admin);
            response.put("code", 20000);
            response.put("message", "success");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            LoggerManager.logger().warn("[com.zulong.web.controller]UserController.createUser@operation failed|", e);
            response.put("code", 50000);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/addtogroup")
    public Map<String, Object> addToGroup(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> response = new HashMap<>();
            String user_id = request.get("user_id");
            int group_id = Integer.parseInt(request.get("group_id"));
            boolean flag = userService.addToGroup(user_id, group_id);
            int code;
            String message;
            if(flag) { code = 20000; message = "success";}
            else { code = 30400; message = "failed";}
            response.put("code", code);
            response.put("message", message);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            LoggerManager.logger().warn("[com.zulong.web.controller]UserController.addToGroup@operation failed|", e);
            response.put("code", 50000);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/removefromgroup")
    public Map<String, Object> removeFromGroup(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> response = new HashMap<>();
            String user_id = request.get("user_id");
            int group_id = Integer.parseInt(request.get("group_id"));
            boolean flag = userService.removeFromGroup(user_id, group_id);
            int code;
            String message;
            if(flag) { code = 20000; message = "success";}
            else { code = 30400; message = "failed";}
            response.put("code", code);
            response.put("message", message);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            LoggerManager.logger().warn("[com.zulong.web.controller]UserController.removeFromGroup@operation failed|", e);
            response.put("code", 50000);
            response.put("message", e.getMessage());
            return response;
        }
    }

}
