package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.NodeDao;
import com.zulong.web.entity.Node;
import org.springframework.jdbc.core.JdbcTemplate;

public class NodeDaoImpl implements NodeDao {

    private JdbcTemplate jdbcTemplate;

    public NodeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Node node) {
        String sql = "INSERT INTO node(instance_id, node_id, start_time, end_time, options) VALUES (?, ?)";
        jdbcTemplate.update(sql,  node.getInstance_id(),node.getNode_id(), node.getStart_time(), node.getEnd_time(), node.getOptions());
    }

    @Override
    public boolean update(int node_id, int instance_id, String end_time) {
        //todo:找到node_id,instance_id的记录，修改end_time
        String sql = "select * from node where node_id = ? && instance_id = ?";
        return false;
    }
    
}
