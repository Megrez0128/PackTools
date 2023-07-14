package com.zulong.web.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Node {
    public String instance_id;
    public int node_id;
    public String start_time;
    public String end_time;
    public String options;
}