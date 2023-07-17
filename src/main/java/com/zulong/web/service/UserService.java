package com.zulong.web.service;


import java.util.List;

public interface UserService {
    //User getUserByID(String id);
    void createUser(String user_id, boolean is_admin);
    List<Integer> getAllGroups(String user_id);
    boolean removeFromGroup(String user_id, int group_id);
    boolean addToGroup(String user_id, int group_id);
}
