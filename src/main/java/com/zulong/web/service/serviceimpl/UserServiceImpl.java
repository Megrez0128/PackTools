package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.AdministrationDao;
import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.entity.relation.Administration;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zulong.web.config.ConstantConfig.DEFAULT_DELETE_AUTHORITY;
import static com.zulong.web.config.ConstantConfig.DEFAULT_UPDATE_AUTHORITY;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AdministrationDao administrationDao;

    public void createUser(String user_id, boolean is_admin) {
        User user = new User(user_id, is_admin);
        userDao.insertUser(user);
        return;
    }

    public List<Group> getAllGroups(String user_id) {
        return userDao.findAllGroups(user_id);
    }

    public boolean removeFromGroup(String user_id, int group_id) {
        return administrationDao.deleteAdministration(user_id, group_id);
    }

    public boolean addToGroup(String user_id, int group_id) {
        Administration administration = new Administration(DEFAULT_UPDATE_AUTHORITY, DEFAULT_DELETE_AUTHORITY, user_id, group_id);
        return administrationDao.insertAdministration(administration);
    }
}
