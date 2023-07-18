package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        return userList;
    }

    @Cacheable(value="userCache", key="#userID")
    public User findByUserID(String userID) {

        String sql = "select * from user where user_id=?";
        Object[] params = new Object[]{userID};
        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@userID is invalid|userID=%s", userID), e);
            return null;
        }
    }

    @CacheEvict(value = "userCache", allEntries = true)
    @CachePut(value = "userCache", key = "#user.user_id")
    @Override
    public boolean insertUser(User user) {
        String sql = "insert into user(user_id, is_admin)values(?,?)";
        Object[] params = {user.getUser_id(), user.is_admin()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]UserDaoImpl.insertUser@insertion failed");
        }
        return flag;
    }

    @CacheEvict(value = "userCache", key = "#UserID")
    @Override
    public boolean deleteByUserID(String UserID) {
        String sql = "delete from user where user_id=?";
        Object[] params = {UserID};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.insertUser@deletion failed|userID=%s", UserID));
        }
        return flag;
    }

    @Override
    public List<Group> findAllGroups(String user_id) {
        String sql = "select * from administration where user_id=?";
        Object[] params = {user_id};
        List<Group> groupList = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Group.class));
        return groupList;
    }
}
