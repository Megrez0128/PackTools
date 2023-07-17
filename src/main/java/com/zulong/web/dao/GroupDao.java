package com.zulong.web.dao;

import com.zulong.web.entity.Group;

import java.util.List;

public interface GroupDao {
    public boolean insertGroup(Group group);
    public List<String> getAllUsers(int group_id);
}
