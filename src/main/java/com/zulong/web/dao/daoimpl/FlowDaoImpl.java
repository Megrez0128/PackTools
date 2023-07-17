package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Flow;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
        // 目前认为返回的是全部的列
        try {
            String sql = "select * from flow";
            List<Flow> flowList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Flow.class));
            return flowList;
        } catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowList@cannot find flow list|"), e);
            return null;
        }
    }

    @Cacheable(value="flowCache", key="#flow_id+'_'+#version")
    public Flow getFlowDetails(int flow_id, int version){
        String sql = "select * from flow where flow_id = ? and version = ?";
        Object[] params = new Object[]{flow_id, version};
        try{
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetails@record_id is invalid|record_id=%d|version=%d", flow_id, version), e);
            return null;
        }
    }

    @CacheEvict(value="flowCache", key="#record_id")
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

//    @Cacheable(value = "flowCache", key = "#record_id")
//    public Flow cloneFlow(int record_id, boolean is_committed, String committed_message){
//        String sql = "select * from flow where record_id=?";
//        Object[] params = {record_id};
//        Flow sample = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
//        Flow flow = new Flow(sample);
//        flow.set_committed(is_committed);
//        flow.setCommit_message(committed_message);
//        insertFlow(flow);
//        return flow;
//    }

    @Cacheable(value = "flowCache", key = "#record_id")
    @Override
    public Flow findFlowByRecordId(int record_id) {
        String sql = "select * from flow where record_id=?";
        Object[] params = new Object[]{record_id};
        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]UserDaoImpl.findByFlowID@record_id is invalid|record_id=%d", record_id), e);
            return null;
        }
    }

    @CacheEvict(value = "flowCache", allEntries = true)
    @CachePut(value = "flowCache", key = "#flow.record_id")
    @Override
    public boolean insertFlow(Flow flow) {
        String sql = "insert into flow(record_id, flow_id, version, is_committed, commit_message, last_build, core_meta_id, extra_meta_id, graph_data, blackboard) values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {flow.getRecord_id(), flow.getFlow_id(), flow.getVersion(), flow.is_committed(), flow.getCommit_message(),
            flow.getLast_build(), flow.getCore_meta_id(), flow.getExtra_meta_id(), flow.getGraph_data(), flow.getBlackboard()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]FlowDaoImpl.insertFlow@insertion failed");
        }
        return flag;
    }

    @CacheEvict(value = "flowCache", allEntries = true)
    @CachePut(value = "flowCache", key = "#flow.record_id")
    @Override
    public boolean updateFlow(Flow flow) {
        String sql = "update flow set is_committed=?, commit_message=?, last_build=?, core_meta_id=?, extra_meta_id=?, graph_data=?, blackboard=? where record_id=?";
        Object[] params = {flow.is_committed(), flow.getCommit_message(), flow.getLast_build(), flow.getCore_meta_id(), flow.getExtra_meta_id(), flow.getGraph_data(), flow.getBlackboard(), flow.getRecord_id()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]FlowDaoImpl.updateFlow@update failed");
        }
        return flag;
    }

    @Override
    public int findMaxVersion(int flow_id) {
        //找到当前flow_id对应的最大version
        String sql = "select max(version) from flow where flow_id=?";
        Object[] params = {flow_id};
        try {            
            return jdbcTemplate.queryForObject(sql, params, Integer.class);
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.findMaxVersion@cannot find max version|flow_id=%d", flow_id), e);
            return -1;
        }
    }

    @Override
    public Flow findByFlowIDAndVersion(int flow_id, int version) {
        //根据flow_id和version找到对应的flow
        String sql = "select * from flow where flow_id=? and version=?";
        Object[] params = {flow_id, version};
        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Flow.class));
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.findByFlowIDAndVersion@cannot find flow|flow_id=%d|version=%d", flow_id, version), e);
            return null;
        }
    }

     public List<Flow> getHistoryFlowList(int flow_id) {
        try {
            String sql = "select * from flow where flow_id=? order by version desc";
            Object[] params = {flow_id};
            List<Flow> flowList = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Flow.class));
            return flowList;
        } catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowList@cannot find flow list|"), e);
            return null;
        }
    }
}
