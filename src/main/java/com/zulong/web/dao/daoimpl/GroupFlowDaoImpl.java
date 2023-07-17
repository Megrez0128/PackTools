package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.GroupFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("GroupFlowDaoImpl")
public class GroupFlowDaoImpl implements GroupFlowDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public GroupFlowDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasFlowPermission(Integer group_id, Integer flow_id){ return true; }
}
