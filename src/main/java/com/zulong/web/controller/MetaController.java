package com.zulong.web.controller;

import com.zulong.web.entity.Meta;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.MetaService;
import com.zulong.web.service.AuthenticationService;

import com.zulong.web.utils.ParamsUtil;
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
        return ParamsUtil.getParam(request,varName,"MetaController",functionName);
    }

    /**
     * 创建meta
     * @param request 包含meta_id、group_id和data三个参数的请求体
     * @return 包含code和message两个参数的Map类型的响应体
     */
    @PostMapping(value = "/create")
    public Map<String, Object> createMeta(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String token) {
        int group_id;
        String version_display;
        String data;
        Map<String, Object> response = new HashMap<>();
        try{
            version_display = (String) getParam(request,"version_display","createMeta");
            if(version_display == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "version_display is null");
            }

            Object tmp = getParam(request,"group_id","createMeta");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is null");
            }
            group_id = (int) tmp;
            data = (String) getParam(request,"data","createMeta");
            if(data == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "data is null");
            }
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]MetaController.createMeta@params are wrong|"), e);
            return errorResponse(response,RETURN_PARAMS_WRONG,e.getMessage());
        }

        try {
            String user_id = TokenUtils.getCurrUserId(token);//从token中解析出user_id
            //验证user_id和group_id是否合法        
            boolean is_valid = authenticationService.isUserInGroup(user_id, group_id);
            if (!is_valid) {
                return errorResponse(response, RETURN_NO_AUTHORITY,"The user is not in this group");
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
                return successResponse(response,meta);
            } else {
                return errorResponse(response,RETURN_DATABASE_WRONG,"database internal error");
            }
        } catch (NumberFormatException e) {
            return errorResponse(response,RETURN_PARAMS_WRONG,"parameter error");
        } catch (Exception e) {
            return errorResponse(response,RETURN_SERVER_WRONG,"server error");
        }
    }

    @PostMapping(value = "/list")
    public Map<String, Object> getMetaList() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            List<Object> metaList= metaService.getAllMeta();
            data.put("items",metaList);
            return successResponse(response,data);
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]MetaController.getMetaList@operation failed|"), e);
            return errorResponse(response,RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/detail")
    public Map<String, Object> getMetaDetails(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        int meta_id;
        try {
            Object tmp = getParam(request,"meta_id","getMetaDetails");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "meta_id is null");
            }
            meta_id = (int) tmp;
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]MetaController.getMetaDetails@params are wrong|", e);
            return errorResponse(response,RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Meta meta = metaService.getMetaDetails(meta_id);
            return successResponse(response,meta);
        } catch (Exception e) {
            LoggerManager.logger().warn("[com.zulong.web.controller]MetaController.getMetaDetails@operation failed|", e);
            return errorResponse(response,RETURN_SERVER_WRONG, e.getMessage());
        }
    }

    @PostMapping(value = "/save")
    public Map<String, Object> saveMeta(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        int group_id;
        String version_display;
        String request_data;
        int meta_id;
        try{
            Object tmp = getParam(request,"meta_id","saveMeta");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "meta_id is null");
            }
            meta_id = (int) tmp;
            tmp = getParam(request,"group_id","saveMeta");
            if(tmp == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "group_id is null");
            }
            group_id = (int) tmp;
            version_display = (String) getParam(request,"version_display","saveMeta");
            if(version_display == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "version_display is null");
            }
            request_data =  (String) getParam(request,"data","saveMeta");
            if(request_data == null) {
                return errorResponse(response, RETURN_PARAMS_WRONG, "data is null");
            }
        }catch (Exception e){
            return errorResponse(response,RETURN_PARAMS_WRONG, e.getMessage());
        }
        try {
            Meta meta= metaService.updateMeta(meta_id,group_id,request_data,version_display);
            return successResponse(response,meta);
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]MetaController.getMetaList@operation failed|"), e);
            return errorResponse(response,RETURN_DATABASE_WRONG,"DATABASE WRONG ");
        }
    }

}
