package com.zulong.web.dao.daoimpl;

import com.zulong.web.entity.Group;
import com.zulong.web.dao.GroupDao;
import org.springframework.stereotype.Service;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
@Service("GroupDaoImpl")
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean insertGroup(Group group) {
        String sql = "insert into group(user_id, token, projects, administrator)values(?,?,?,?)";
        Object[] params = {group.getGroup_id(), group.getGroup_name()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]GroupDaoImpl.insertGroup@insertion failed");
        }
        return flag;
    }
}
