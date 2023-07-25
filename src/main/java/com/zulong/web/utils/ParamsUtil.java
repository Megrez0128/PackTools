package com.zulong.web.utils;

import com.zulong.web.log.LoggerManager;

import java.util.HashMap;
import java.util.Map;

import static com.zulong.web.config.ConstantConfig.RETURN_PARAMS_WRONG;

public class ParamsUtil {
    public static Object getParam(Map<String, Object> request, String varName, String controllerName,String functionName){
        Object tmp = request.get(varName);
        if(tmp == null || tmp.toString().isEmpty()) {
            LoggerManager.logger().error(String.format("[com.zulong.web.controller.%s]%s.%s@%s is null|",controllerName,controllerName, functionName, varName));
            return null;
        }
        return tmp;
    }
}
