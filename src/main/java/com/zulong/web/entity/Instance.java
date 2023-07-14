package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Instance {
    private String uuid;  // 唯一id,由客户端生成
    private int flow_record_id;
    private String node_id;  // 运行到的节点的id
    private String option;  // 参数，不需要处理
    private String startTime;
    private String endTime;
    private Boolean complete;
    private Boolean hasError;

//    /**
//     * 设置Instance初始化函数
//     * 固定设置startTime, complete, hasError
//     * 其余信息都在调用Instance上传时更新
//     */
    final public void initInstance(String uuid, int flow_record_id, String node_id, String option){
        this.uuid = uuid;
        this.flow_record_id = flow_record_id;
        this.node_id = node_id;
        this.option = option;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.startTime = df.format(new Date());
        this.complete = false;
        this.hasError = false;
    }

}
