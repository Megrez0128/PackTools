package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class Instance extends Flowchart{
    private String uuid;  // TODO：暂时指定为String类型，标记唯一id
    private String flow_id;  // 流程id
    private String node_id;  // 运行到的节点的id
    private ArrayList<String> option;  // 参数，不需要处理
    // 以上信息都不需要修改
    // 指定为private类型，需要添加接口
    private String startTime;
    private String endTime;
    private Boolean complete;
    private Boolean hasError;

//    /**
//     * 设置Instance初始化函数
//     * 理论上只需要设置startTime, complete, hasError
//     * 其余信息都在调用Instance上传时更新
//     */
//    public Instance(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        this.startTime = df.format(new Date());
//        this.complete = false;
//        this.hasError = false;
//    }


}
