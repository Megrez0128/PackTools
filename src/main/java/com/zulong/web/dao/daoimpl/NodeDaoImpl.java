package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.NodeDao;
import com.zulong.web.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("NodeDaoImpl")
public class NodeDaoImpl implements NodeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public NodeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Node node) {
        String sql = "INSERT INTO node(instance_id, node_id, start_time, end_time, options) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, node.getInstance_id(),node.getNode_id(), node.getStart_time(), node.getEnd_time(), node.getOptions());
    }

    @Override
    public boolean update(int node_id, int instance_id, String end_time, String options) {
        String sql = "UPDATE node SET end_time = ?, options = ? WHERE node_id = ? AND instance_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, end_time, options, node_id, instance_id);
        return rowsAffected > 0;
    }
    
}
