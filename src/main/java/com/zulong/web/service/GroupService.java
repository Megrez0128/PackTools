package com.zulong.web.service;

import com.zulong.web.entity.Group;

import java.util.List;

public interface GroupService {

    List<String> getAllUsers(int group_id);
    void createGroup(int group_id, String group_name);
}
