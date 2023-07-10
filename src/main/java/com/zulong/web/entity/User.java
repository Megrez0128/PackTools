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
    private String user_id;
    private String token;
    private ArrayList<Integer> projects;  // 存储id
    private boolean administrator;

//    public User(String id, String token) {
//        this.UserID = id;
//        this.token = token;
//    }


}
