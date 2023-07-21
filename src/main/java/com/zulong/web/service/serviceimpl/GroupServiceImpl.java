package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.GroupDao;
import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    final static int START_GROUP_ID = 1023;

    int curr_group_id = START_GROUP_ID;

    @Autowired
    private GroupDao groupDao;


    private int getCurrGroupID() {
        int currGroupId = groupDao.getCurrGroupID();
        return currGroupId != 1023 ? currGroupId : START_GROUP_ID;
    }

    public List<User> getAllUsers(int group_id){
        return groupDao.getAllUsers(group_id);
    }

    public Group createGroup(String group_name) {
        curr_group_id = getCurrGroupID();
        Group group = new Group(curr_group_id + 1, group_name);
        groupDao.insertGroup(group);
        curr_group_id++;
        return group;
    }

    public boolean findGroup(int group_id) { return groupDao.findGroup(group_id); }
}
