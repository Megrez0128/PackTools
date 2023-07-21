package com.zulong.web.service;

import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;

import java.util.List;

public interface GroupService {

    List<User> getAllUsers(int group_id);
    Group createGroup(String group_name);
    boolean findGroup(int group_id);
}
