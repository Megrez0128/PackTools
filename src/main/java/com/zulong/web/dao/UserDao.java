package com.zulong.web.dao;

import com.zulong.web.entity.User;
import java.util.List;

public interface UserDao {
    List<User> findAll();
    boolean findByUserID(String user_id);
    User getUserByUserId(String user_id);
    boolean insertUser(User user);
    boolean deleteByUserID(String user_id);
    List<Integer> findAllGroups(String user_id);
}
