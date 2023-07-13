package com.zulong.web.controller;

import com.zulong.web.service.MetaService;
import com.zulong.web.service.AuthenticationService;
import com.zulong.web.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/mymeta")
public class MetaController {
    private MetaService metaService;
    private AuthenticationService authenticationService;

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
    public Map<String, Object> createMeta(@RequestBody Map<String, String> request, HttpServletRequest httpServletRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer meta_id = Integer.valueOf(request.get("meta_id"));
            Integer group_id = Integer.valueOf(request.get("group_id"));
            String data = request.get("data");
            String user_id = authenticationService.getUserIdFromToken(httpServletRequest.getHeader("Authorization")); //从token中解析出user_id
            //验证user_id和group_id是否合法        
            boolean is_valid = authenticationService.isUserInGroup(user_id, group_id);
            if (!is_valid) {
                response.put("code", "40300");
                response.put("message","user or group not authorized");
                return response;
            }
            //验证group_id和meta_id是否合法
            is_valid = authenticationService.hasMetaPermission(group_id, meta_id);
            if (!is_valid) {
                response.put("code", "40300");
                response.put("message","group or meta not authorized");
                return response;
            }
            boolean is_success = metaService.createMeta(meta_id,group_id,data);
            if (is_success) {
                response.put("code", "20000");
                response.put("message","success");
            } else {
                response.put("code", "30400");
                response.put("message","database internal error");
            }
        } catch (NumberFormatException e) {
            response.put("code", "40000");
            response.put("message","parameter error");
        } catch (Exception e) {
            response.put("code", "50000");
            response.put("message","server error");
        }
        return response;
    }

}
