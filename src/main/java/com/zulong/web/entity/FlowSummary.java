package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowSummary implements Serializable {
    public int flow_id;
    public String name;
    public String des;
    public String last_build;
    public String last_commit;
    public int last_version;
}