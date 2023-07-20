package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
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
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public List<User> findAll() {
        String sql = "select * from pack_user";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        return userList;
    }

    @Override
    public User getUserByUserId(String user_id){
        String sql = "SELECT * FROM pack_user WHERE user_id = ?";
        Object[] params = new Object[]{user_id};
        try {
            User user = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(User.class));
            if(user == null){
                LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@user == null|user_id=%s", user_id));
            }
            return user;
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@user_id is invalid|user_id=%s", user_id), e);
            return null;
        }
    }
    @Cacheable(value="userCache", key="#user_id")
    @Override
    public boolean findByUserID(String user_id) {
//        String sql = "SELECT * FROM pack_user WHERE user_id = ?";
//        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), user_id);
//        return userList.isEmpty() ? null : userList.get(0);
        String sql = "select count(*) from pack_user where user_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{user_id}, Integer.class);
        return count > 0;
        //String sql = "select * from pack_user where admin=1";
        //Object[] params = new Object[]{user_id};
//        try {
//            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class));
//            if(user == null){
//                LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@user == null|user_id=%s", user_id));
//            }
//            return user;
//        } catch (Exception e) {
//            // 如果没有找到对应的记录，返回null；添加一条warn日志
//            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@user_id is invalid|user_id=%s", user_id), e);
//            return null;
//        }
//        LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@test for user state|user_id=%s", user_id));
//        //LoggerManager.logger().warn(jdbcTemplate.queryForList(sql, params));
//        List<Map<String, Object>> userList = jdbcTemplate.queryForList(sql, params);
////        List<User> result = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(User.class));
//        LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByUserID@test for user state|user_id=%s", user_id));
//        User user = new User();
//        user.setUser_id(userList.get(0).get("user_id").toString());
//        user.setAdmin((Boolean) userList.get(0).get("admin"));
//        return user;
    }

    @CacheEvict(value = "userCache", allEntries = true)
    @CachePut(value = "userCache", key = "#user.user_id")
    @Override
    public boolean insertUser(User user) {
        String sql = "insert into pack_user(user_id, admin)values(?,?)";
        Object[] params = {user.getUser_id(), user.isAdmin()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]UserDaoImpl.insertUser@insertion failed");
        }
        return flag;
    }

    @CacheEvict(value = "userCache", key = "#user_id")
    @Override
    public boolean deleteByUserID(String user_id) {
        String sql = "delete from pack_user where user_id=?";
        Object[] params = {user_id};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.deleteByUserID@deletion failed|userID=%s", user_id));
        }
        return flag;
    }

    @Override
    public List<Integer> findAllGroups(String user_id) {
        String sql = "select group_id from administration where user_id=?";
        Object[] params = {user_id};

        List<Integer> groupIDList = jdbcTemplate.queryForList(sql, params, Integer.class);
        return groupIDList;
    }
}
