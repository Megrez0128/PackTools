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

    public Flow getFlowDetails(int flow_id, int version){
        // TODO: 确定MySQL中Flow表的名称
        String sql = "select * from flow where flow_id = ? and version = ?";
        Object[] params = new Object[]{flow_id, version};
        try{
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetails@record_id is invalid|record_id=%d|version=%d", flow_id, version), e);
            return null;
        }
    }

    public int deleteFlow(int record_id) {
        boolean flag = instanceDao.findInstanceByFlowID(record_id);
        if(!flag){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.deleteFlow@flow has instances|record_id=%d", record_id));
            return 0;
        }
        String sql = "delete from flow where record_id=?";
        Object[] params = {record_id};
        boolean flag1 = jdbcTemplate.update(sql, params) > 0;
        if(!flag1){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.deleteFlow@something wrong happened during deletion|"));
            return 2;
        }
        return 1;
    }

    public Flow cloneFlow(int record_id, String name, String des){
        String sql = "select * from flow where record_id=?";
        Object[] params = {record_id};
        Flow sample = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
        Flow flow = new Flow(sample);
        flow.setName(name);
        flow.setDes(des);
        // TODO: 写insertFlow
        // TODO: 并且修改record_id和版本，可以在写完FlowSummary后继续补充
        return flow;
    }
}
