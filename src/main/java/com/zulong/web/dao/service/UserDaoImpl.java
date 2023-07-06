package com.zulong.web.dao.service;

import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDaoImpl")
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User findByUserID(String UserID) {
        String sql = "select * from test_user where name=?";
        Object[] params = new Object[]{UserID};
        return jdbcTemplate.queryForObject(
                sql,
                params,
                new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public boolean addUser(User user) {
        String sql = "insert into test_user(user_id,token,projects,administrator)values(?,?,?,?)";
        Object[] params = {user.getUserID(),user.getToken(),user.getProjects(),user.getAdministrator()};
        return jdbcTemplate.update(sql, params)>0;
    }

    @Override
    public boolean deleteByUserID(String UserID) {
        String sql = "delete from test_user where user_id=?";
        Object[] params = {UserID};
        return jdbcTemplate.update(sql,params)>0;
    }
}
