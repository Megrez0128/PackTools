package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.GroupDao;
import com.zulong.web.entity.Group;
import com.zulong.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;

    public List<String> getAllUsers(int group_id){
        return groupDao.getAllUsers(group_id);
    }

    public void createGroup(int group_id, String group_name) {
        Group group = new Group(group_id, group_name);
        groupDao.insertGroup(group);
    }
}
