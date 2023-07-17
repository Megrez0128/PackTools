package com.zulong.web.controller;

import com.zulong.web.entity.Group;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.GroupService;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/group")
public class GroupController {
    private GroupService groupService;

    @PostMapping(value = "/listuser")
    public Map<String, Object> getAllUsers(@RequestBody Map<String, String> request){
        try {
            Integer group_id = Integer.parseInt(request.get("group_id"));
            List<String> userlist = groupService.getAllUsers(group_id);
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            response.put("code", 20000);
            data.put("group_id", group_id);
            data.put("items", userlist);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]GroupController.getAllUsers@operation failed|", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 50000);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/create")
    public Map<String, Object> createGroup(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            Integer group_id = Integer.parseInt(request.get("group_id"));
            String group_name = request.get("group_name");
            groupService.createGroup(group_id, group_name);
            data.put("group_id", group_id);
            data.put("group_name", group_name);
            response.put("code", 20000);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            LoggerManager.logger().warn("[com.zulong.web.controller]GroupController.createGroup@operation failed|", e);
            response.put("code", 50000);
            response.put("message", e.getMessage());
            return response;
        }
    }

}
