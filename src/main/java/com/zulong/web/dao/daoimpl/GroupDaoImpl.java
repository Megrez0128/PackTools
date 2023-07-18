package com.zulong.web.dao.daoimpl;

import com.zulong.web.entity.Group;
import com.zulong.web.dao.GroupDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Service("GroupDaoImpl")
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @CacheEvict(value = "groupCache", allEntries = true)
    @CachePut(value = "groupCache", key="#group.getGroup_id()")
    @Override
    public boolean insertGroup(Group group) {
        String sql = "insert into pack_group(group_id, group_name)values(?, ?)";
        Object[] params = {group.getGroup_id(), group.getGroup_name()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]GroupDaoImpl.insertGroup@insertion failed");
        }
        return flag;
    }

    public List<String> getAllUsers(int group_id){
        String sql = "select user_id from administration where group_id=?";
        Object[] params = {group_id};
        List<String> userList = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(String.class));
        return userList;
    }

    
}
