package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDaoImpl")
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<User> findAll() {
        String sql = "select * from test_user";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        return userList;
    }

    public User findByUserID(String userID) {

        String sql = "select * from test_user where user_id=?";
        Object[] params = new Object[]{userID};
        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@userID is invalid|userID=%s", userID), e);
            return null;
        }
    }

    @Override
    public boolean insertUser(User user) {
        String sql = "insert into test_user(user_id, token, projects, administrator)values(?,?,?,?)";
        Object[] params = {user.getUser_id(), user.is_admin()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]UserDaoImpl.insertUser@insertion failed");
        }
        return flag;
    }

    @Override
    public boolean deleteByUserID(String UserID) {
        String sql = "delete from test_user where user_id=?";
        Object[] params = {UserID};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.insertUser@deletion failed|userID=%s", UserID));
        }
        return flag;
    }
}
