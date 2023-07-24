package com.zulong.web.controller;

import com.zulong.web.entity.Meta;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.MetaService;
import com.zulong.web.service.AuthenticationService;

import com.zulong.web.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static com.zulong.web.config.ConstantConfig.*;

@RestController
@RequestMapping(value = "/mymeta")
public class MetaController {
    private final MetaService metaService;
    private final AuthenticationService authenticationService;

    @Autowired
    public MetaController(MetaService metaService, AuthenticationService authenticationService) {
        this.metaService = metaService;
        this.authenticationService = authenticationService;
    }

    /**
     * 创建meta
     * @param request 包含meta_id、group_id和data三个参数的请求体
     * @return 包含code和message两个参数的Map类型的响应体
     */
    @PostMapping(value = "/create")
    public Map<String, Object> createMeta(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        int group_id;
        String version_display;
        String data;
        try{
            group_id = Integer.parseInt(request.get("group_id"));
            version_display = request.get("version_display");
            data = request.get("data");
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]MetaController.createMeta@params are wrong|"), e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        try {
            String user_id = TokenUtils.getCurrUserId(token);//从token中解析出user_id
            //验证user_id和group_id是否合法        
            boolean is_valid = authenticationService.isUserInGroup(user_id, group_id);
            if (!is_valid) {
                response.put("code", RETURN_NO_AUTHORITY);
                response.put("message","The user is not in this group");
                return response;
            }
            //验证group_id和meta_id是否合法
//            is_valid = authenticationService.hasMetaPermission(group_id, meta_id);
//            if (!is_valid) {
//                response.put("code", "40300");
//                response.put("message","group or meta not authorized");
//                return response;
//            }
            Meta meta = metaService.createMeta(version_display, group_id, data);
            if (meta != null) {
                response.put("code", RETURN_SUCCESS);
                response.put("data", meta);
            } else {
                response.put("code", RETURN_DATABASE_WRONG);
                response.put("message","database internal error");
            }
        } catch (NumberFormatException e) {
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message","parameter error");
        } catch (Exception e) {
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message","server error");
        }
        return response;
    }

    @PostMapping(value = "/list")
    public Map<String, Object> getMetaList() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            Map<String, Object> metaList= metaService.getAllMeta();
            data.put("items",metaList);
            response.put("code", RETURN_SUCCESS);
            response.put("data",data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]MetaController.getMetaList@operation failed|"), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message","server error");
            return response;
        }
    }

    @PostMapping(value = "/detail")
    public Map<String, Object> getMetaDetails(@RequestBody Map<String, Integer> request) {
        Map<String, Object> response = new HashMap<>();
        int meta_id;
        try {
            meta_id = request.get("meta_id");
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]MetaController.getMetaDetails@params are wrong|", e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            Meta meta = metaService.getMetaDetails(meta_id);
            response.put("code", RETURN_SUCCESS);
            response.put("data", meta);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]MetaController.getMetaDetails@operation failed|", e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/save")
    public Map<String, Object> saveMeta(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        int group_id;
        String version_display;
        String request_data;
        int meta_id;
        try{
            group_id = Integer.parseInt(request.get("group_id"));
            meta_id = Integer.parseInt(request.get("meta_id"));
            version_display = request.get("version_display");
            request_data = request.get("data");
        }catch (Exception e){
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message","parameter error");
            return response;
        }
        try {
            Meta meta= metaService.updateMeta(meta_id,group_id,request_data,version_display);
            response.put("code", RETURN_SUCCESS);
            response.put("data",meta);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]MetaController.getMetaList@operation failed|"), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message","server error");
            return response;
        }
    }

}
