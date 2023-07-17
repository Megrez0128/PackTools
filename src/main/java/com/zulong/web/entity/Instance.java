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
    private String build_time;
    private Boolean complete;
    private Boolean has_error;

//    /**
//     * 设置Instance初始化函数
//     * 固定设置startTime, complete, hasError
//     * 其余信息都在调用Instance上传时更新
//     */
    final public void initInstance(String uuid, int flow_record_id, String node_id, String option){
        this.uuid = uuid;
        this.flow_record_id = flow_record_id;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.build_time = df.format(new Date());
        this.complete = false;
        this.has_error = false;
    }

}
