package com.zulong.web.entity;

import com.sun.xml.internal.ws.message.stream.StreamHeader11;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class Instance extends Flowchart{
    private int uuid;  // TODO：暂时指定为int类型，标记唯一id
    private int flow_id;  // 流程id
    private String node_id;  // 运行到的节点的id
    private String option;  // 参数，不需要处理
    // 以上信息都不需要修改
    // 指定为private类型，需要添加接口
    private String startTime;
    private String endTime;
    private Boolean complete;
    private Boolean hasError;

//    /**
//     * 设置Instance初始化函数
//     * 固定设置startTime, complete, hasError
//     * 其余信息都在调用Instance上传时更新
//     */
    final public void initInstance(int uuid, int flow_id, String node_id, String option){
        this.uuid = uuid;
        this.flow_id = flow_id;
        this.node_id = node_id;
        this.option = option;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.startTime = df.format(new Date());
        this.complete = false;
        this.hasError = false;
    }



}
