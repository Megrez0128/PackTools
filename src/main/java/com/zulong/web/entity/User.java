package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable
{
    public String UserID;
    public String token;
    public ArrayList<Group> projects;
    public boolean administrator;

    /**
     * 测试用的构造函数
     * @param id
     * @param token
     */
    public User(String id, String token) {
        this.UserID = id;
        this.token = token;
    }

    final public String getUserID() {
        return this.UserID;
    }
    final public String getToken() {
        return this.token;
    }
    final public ArrayList<Group> getProjects() {
        return this.projects;
    }
    final public boolean getAdministrator() {
        return this.administrator;
    }

}
