package com.zulong.web.entity;

import java.util.ArrayList;

public class Group {
    private int id;
    private String projectName;
    private ArrayList<User> members;

    final public int getGroupID(){
        return this.id;
    }
    final public String getGroupName(){
        return this.projectName;
    }

    final public ArrayList<User> getMembers(){
        return this.members;
    }
}
