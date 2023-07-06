package com.zulong.web.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Instance extends Flowchart{
    String uuid;  // TODO：暂时指定为String类型，标记唯一id
    String flow_id;  // 流程id
    String node_id;  // 运行到的节点的id
    ArrayList<String> option;  // 参数，不需要处理
    // 以上信息都不需要修改

    String startTime;
    String endTime;
    Boolean complete;
    Boolean hasError;

    /**
     * 设置Instance初始化函数
     * 理论上只需要设置startTime, complete, hasError
     * 其余信息都在调用Instance上传时更新
     */
    public Instance(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.startTime = df.format(new Date());
        this.complete = false;
        this.hasError = false;
    }


}
