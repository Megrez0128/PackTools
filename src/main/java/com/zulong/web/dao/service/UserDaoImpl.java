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
        String sql = "select * from test_user";
        //todo: projects是arrayList<integer>,存在转化问题
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        return userList;
    }

    public User findByUserID(String userID) {
        String sql = "select * from test_user where user_id=?";
        Object[] params = new Object[]{userID};
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    params,
                    new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            // 没有找到对应的记录，返回null
            return null;
        }
    }

    @Override
    public boolean addUser(User user) {
        String sql = "insert into test_user(user_id,token,projects,administrator)values(?,?,?,?)";
        Object[] params = {user.getUser_id(),user.getToken(),user.getProjects(),user.isAdministrator()};
        return jdbcTemplate.update(sql, params)>0;
    }

    @Override
    public boolean deleteByUserID(String UserID) {
        String sql = "delete from test_user where user_id=?";
        Object[] params = {UserID};
        return jdbcTemplate.update(sql,params)>0;
    }
}
