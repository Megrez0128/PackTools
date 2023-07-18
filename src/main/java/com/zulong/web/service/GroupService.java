package com.zulong.web.service;

import com.zulong.web.entity.Group;

import java.util.List;

public interface GroupService {

    List<String> getAllUsers(int group_id);
    Group createGroup(String group_name);
}
