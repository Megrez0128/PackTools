package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.FlowSummaryDao;
import com.zulong.web.entity.FlowSummary;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FlowSummaryDaoImpl")
public class FlowSummaryDaoImpl implements FlowSummaryDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FlowSummaryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Cacheable(value="flowSummaryCache", key="#flow_id")
    @Override
    public FlowSummary findFlowSummaryByID(int flow_id) {
        String sql = "select * from flow where flow_id=?";
        Object[] params = new Object[]{flow_id};
        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(FlowSummary.class));
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.findFlowSummaryByID@flow_id is invalid|flow_id=%d", flow_id), e);
            return null;
        }
    }

    @Override
    public boolean insertFlowSummary(FlowSummary flowSummary) {
        //插入流程摘要
        String sql = "insert into flow_summary(flow_id, name, des, last_build, last_commit, last_version) values(?,?,?,?,?)";
        Object[] params = {flowSummary.getFlow_id(), flowSummary.getName(), flowSummary.getDes(), flowSummary.getLast_build(), flowSummary.getLast_commit(), flowSummary.getLast_version()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.insertFlowSummary@something wrong happened during insertion|"));
        }
        return flag;
    }

    @Override
    public List<FlowSummary> getFlowSummaryList(){
        try{
            String sql = "select * from flowsummary";
            List<FlowSummary> flowSummaryList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FlowSummary.class));
            return flowSummaryList;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.getFlowSummaryList@cannot find flow summary list|"), e);
            return null;
        }
    }

    @Override
    public void updateFlowSummary(FlowSummary flowSummary) {
        try {
            // 根据flow_id更新流程摘要
            String sql = "update flow_summary set name=?, des=?, last_build=?, last_commit=?, last_version=? where flow_id=?";
            Object[] params = {flowSummary.getName(), flowSummary.getDes(), flowSummary.getLast_build(), flowSummary.getLast_commit(), flowSummary.getLast_version(), flowSummary.getFlow_id()};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.updateFlowSummary@something wrong happened during update|"));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.updateFlowSummary@something wrong happened during update|"), e);
        }
    }
}
