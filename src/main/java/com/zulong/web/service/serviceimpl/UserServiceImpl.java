package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.AdministrationDao;
import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.User;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Integer> getAllGroups(String user_id) {
        return userDao.findAllGroups(user_id);
    }

    public boolean removeFromGroup(String user_id, int group_id) {
        return administrationDao.deleteAdministration(user_id, group_id);
    }

    public boolean addToGroup(String user_id, int group_id) {
        return administrationDao.insertAdministration(user_id, group_id, true, true);
    }
}
