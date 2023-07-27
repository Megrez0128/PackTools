package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Instance implements Serializable {

    //private int instance_id;
    private String uuid;  // 唯一id
    private int flow_record_id;
    private String start_time;
    private String end_time;
    private Boolean complete;
    private Boolean has_error;

}
