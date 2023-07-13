package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Flow;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FlowDaoImpl")
public class FlowDaoImpl implements FlowDao {

    @Autowired
    private InstanceDao instanceDao;
    private final JdbcTemplate jdbcTemplate;

    public FlowDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Flow> getFlowList(){
        // TODO: 确定MySQL中Flow表的名称
        // 被筛选的列按照文档确定
        try {
            String sql = "select record_id, flow_id, version, name, des, core_meta_id, last_build, extra_meta_id from flow";
            List<Flow> flowList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Flow.class));
            return flowList;
        } catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetails@record_id is invalid|"), e);
            return null;
        }
    }

    public Flow getFlowDetails(int record_id){
        // TODO: 确定MySQL中Flow表的名称
        String sql = "select * from flow where record_id = ?";
        Object[] params = new Object[]{record_id};
        try{
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetails@record_id is invalid|record_id=%d", record_id), e);
            return null;
        }
    }

    public boolean deleteFlow(int record_id) {

        return false;
    }
}
