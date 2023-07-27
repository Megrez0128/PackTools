package com.zulong.web.utils;

import com.zulong.web.log.LoggerManager;

import javax.xml.bind.annotation.XmlType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.zulong.web.config.ConstantConfig.DEFAULT_META_ID;

public class ParamsUtil {
    public static Object getParam(Map<String, Object> request, String varName, String controllerName,String functionName){
        Object tmp = request.get(varName);
        if(tmp == null || tmp.toString().isEmpty()) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]%s.%s@%s is null|",controllerName, functionName, varName));
            return null;
        }
        return tmp;
    }

    public static boolean isInValidMetaId(int meta_id){
        return meta_id < -1;
    }

    public boolean isValidGroupId(int group_id){
        return group_id >= 0;
    }
    public boolean isValidRecordId(int record_id){
        return record_id >= 0;
    }
    public boolean isValidVersion(int version){
        return version >= 0;
    }
    public boolean isValidFlowId(int flow_id){
        return flow_id >= 0;
    }

    public static boolean isInValidInt(int num){
        return num < 0;
    }

    // 补全这个函数，校验字符串格式是否符合"yyyy-MM-dd HH:mm:ss" 的格式
    public static boolean isInValidDateFormat(String str) {
        // 使用SimpleDateFormat类来解析和格式化日期字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置lenient为false，表示严格校验日期格式
        sdf.setLenient(false);
        try {
            // 尝试将字符串解析为Date对象，如果成功则返回true，否则抛出异常
            Date date = sdf.parse(str);
            return false;
        } catch (Exception e) {
            // 捕获异常，返回false
            LoggerManager.logger().error(String.format("[com.zulong.web.utils]ParamsUtil.isValidDateFormat@Date is invalid|date=%s", str), e);
            return true;
        }
    }

}
