package com.zulong.web.dao;

import com.zulong.web.entity.User;
import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findByUserID(String UserID);
    boolean insertUser(User user);
    boolean deleteByUserID(String UserID);
}
