package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.NodeDao;
import com.zulong.web.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zulong.web.config.ConstantConfig.CONST_INIT_END_TIME;

@Service("NodeDaoImpl")
public class NodeDaoImpl implements NodeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public NodeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertNode(Node node) {
        String sql = "INSERT INTO node(instance_id, node_id, start_time, end_time, options) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, node.getInstance_id(), node.getNode_id(), node.getStart_time(), node.getEnd_time(), node.getOptions());
    }

    @Override
    public boolean updateNode(String node_id, String instance_id, String end_time, String options) {
        String sql = "UPDATE node SET end_time = ?, options = ? WHERE node_id = ? AND instance_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, end_time, options, node_id, instance_id);
        return rowsAffected > 0;
    }

    @Override
    public List<Node> getRunningNode(String instance_id) {
        String sql = "select * from node where instance_id=? and end_time=?";
        Object[] params = {instance_id, CONST_INIT_END_TIME};
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Node.class));
    }

    @Override
    public List<Node> getCompleteNode(String instance_id) {
        String sql = "select * from node where instance_id = ? and end_time <> ?";
        Object[] params = {instance_id, CONST_INIT_END_TIME};
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Node.class));
    }
    
}
