package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flowchart {
    private int id;
    private int version;
    private String state;
    private String name;
    private String des;
    private String last_build;
    private boolean inner;

    final public int getFlowID() {
        return this.id;
    }
    final public int getFlowVersion() {
        return this.version;
    }
    final public String getState() {
        return this.state;
    }
    final public String getName() {
        return this.name;
    }
    final public String getDes() {
        return this.des;
    }
    final public String getLastBuildTime() {
        return this.last_build;
    }
    final public boolean getInner() {
        return this.inner;
    }
}
