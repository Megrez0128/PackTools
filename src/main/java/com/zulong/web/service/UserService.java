package com.zulong.web.service;


import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;

import java.util.List;

public interface UserService {
    //User getUserByID(String id);
    void createUser(String user_id, boolean is_admin);
    List<Group> getAllGroups(String user_id);
    boolean removeFromGroup(String user_id, int group_id);
    boolean addToGroup(String user_id, int group_id);

    boolean findByUserID(String user_id);
    User getUserByUserId(String user_id);
}
