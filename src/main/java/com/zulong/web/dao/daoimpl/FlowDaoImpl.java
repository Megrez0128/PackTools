package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.FlowDao;
import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Flow;
import com.zulong.web.log.LoggerManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.zulong.web.config.ConstantConfig.*;
import static com.zulong.web.entity.Flow.getFlowFromResultSet;

@Service("FlowDaoImpl")
public class FlowDaoImpl implements FlowDao {

    @Autowired
    private InstanceDao instanceDao;
    private final JdbcTemplate jdbcTemplate;

    public FlowDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Flow> getFlowList(){
        // 目前认为返回的是全部的列
        try {
            String sql = "select * from flow";

            List<Flow> flowList = jdbcTemplate.query(sql, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flowList;
        } catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowList@cannot find flow list|"), e);
            return null;
        }
    }

    @Cacheable(value="flowCache", key="#flow_id+'_'+#version")
    @Override
    public Flow getFlowDetailsByFlowIdAndVersion(final int flow_id, final int version){
        String sql = "select * from flow where flow_id = ? and version = ?";
        Object[] params = new Object[]{flow_id, version};
        try{
            Flow flow = jdbcTemplate.queryForObject(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flow;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetails@record_id is invalid|flow_id=%d|version=%d", flow_id, version), e);
            return null;
        }
    }

    @Cacheable(value = "flowCache", key = "#record_id")
    @Override
    public Flow getFlowDetailsByRecordId(int record_id) {
        String sql = "select * from flow where record_id=?";
        Object[] params = new Object[]{record_id};
        try {
            Flow flow = jdbcTemplate.queryForObject(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            LoggerManager.logger().info(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetailsByRecordId@show save_time|save_time=%s", flow.getSave_time()));
            return flow;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getFlowDetailsByID@record_id is invalid|record_id=%d", record_id), e);
            return null;
        }
    }

    @CacheEvict(value="flowCache", key="#record_id")
    @Override
    public int deleteFlow(int record_id) {
        boolean flag = instanceDao.findInstanceByFlowID(record_id);
        if(flag){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.deleteFlow@flow has instances|record_id=%d", record_id));
            return DELETE_HAVE_INSTANCE;
        }
        String sql = "delete from flow where record_id=?";
        Object[] params = {record_id};
        boolean flag1 = jdbcTemplate.update(sql, params) > 0;
        if(!flag1){
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.deleteFlow@something wrong happened during deletion|record_id=%d", record_id));
            return DELETE_DATABASE_WRONG;
        }
        return DELETE_SUCCESS;
    }


    @Cacheable(value = "flowCache", key = "#record_id")
    @Override
    public Flow findFlowByRecordId(final int record_id) {
        String sql = "select * from flow where record_id=?";
        Object[] params = new Object[]{record_id};
        try {
            Flow flow = jdbcTemplate.queryForObject(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flow;
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.findFlowByRecordId@record_id is invalid|record_id=%d", record_id), e);
            return null;
        }
    }

    @SneakyThrows
    @CacheEvict(value = "flowCache", allEntries = true)
    @CachePut(value = "flowCache", key = "#flow.record_id")
    @Override
    public boolean insertFlow(Flow flow) {
        String sql = "insert into flow(record_id, flow_id, version, committed, commit_message, last_build, save_time, meta_id, graph_data, blackboard) values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {flow.getRecord_id(), flow.getFlow_id(), flow.getVersion(), flow.isCommitted(), flow.getCommit_message(),
            flow.getLast_build(), flow.getSave_time(), flow.getMeta_id(), new SerialBlob(flow.getGraph_data().getBytes()), new SerialBlob(flow.getBlackboard().getBytes())};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.insertFlow@insertion failed|record_id=%d", flow.getRecord_id()));
        }
        return flag;
    }

    @CacheEvict(value = "flowCache", allEntries = true)
    @CachePut(value = "flowCache", key = "#flow.record_id")
    @Override
    public boolean updateFlow(Flow flow) {
        String sql = "update flow set committed=?, commit_message=?, last_build=?, save_time=?, meta_id=?, graph_data=?, blackboard=? where record_id=?";
        Object[] params = {flow.isCommitted(), flow.getCommit_message(), flow.getLast_build(), flow.getSave_time(), flow.getMeta_id(), flow.getGraph_data(), flow.getBlackboard(), flow.getRecord_id()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.updateFlow@update failed|record_id=%d", flow.getRecord_id()));
        }
        return flag;
    }

    @Override
    public boolean updateLastBuild(int record_id, String last_build) {
        String sql = "update flow set last_build =? where record_id=?";
        Object[] params = {last_build, record_id};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.updateLastBuild@update failed|record_id=%d|last_build=%s", record_id, last_build));
        }
        return flag;
    }

    @Override
    public int findMaxVersion(final int flow_id) {
        //找到当前flow_id对应的最大version
        String sql = "select max(version) from flow where flow_id=?";
        Object[] params = {flow_id};
        try {            
            return jdbcTemplate.queryForObject(sql, params, Integer.class);
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.findMaxVersion@cannot find max version|flow_id=%d", flow_id), e);
            return -1;
        }
    }

    @Cacheable(value="flowCache", key="#flow_id+'_'+#version")
    @Override
    public Flow findByFlowIDAndVersion(int flow_id, int version) {
        //根据flow_id和version找到对应的flow
        String sql = "select * from flow where flow_id=? and version=?";
        Object[] params = {flow_id, version};
        try {
            Flow flow = jdbcTemplate.queryForObject(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flow;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.findByFlowIDAndVersion@cannot find flow|flow_id=%d|version=%d", flow_id, version), e);
            return null;
        }
    }
    @Override
    public List<Flow> getHistoryFlowList(final int flow_id) {
        try {
            String sql = "select * from flow where flow_id=? order by version desc";
            Object[] params = {flow_id};
            List<Flow> flowList = jdbcTemplate.query(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flowList;
        } catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getHistoryFlowList@cannot find flow list|flow_id=%d", flow_id), e);
            return null;
        }
    }
    @Override
    public Flow getNewestFlow(final int flow_id) {
        try {
            String sql = "SELECT * FROM flow WHERE flow_id =? ORDER BY version DESC LIMIT 1";
            Object[] params = {flow_id};
            Flow flow = jdbcTemplate.queryForObject(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flow;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getNewVersionFlow@cannot find the flow|flow_id=%d", flow_id), e);
            return null;
        }
    }

    @Override
    public Flow getNewVersionFlow(final int flow_id){
        try {
            String sql = "SELECT * FROM flow WHERE flow_id =? and committed = true ORDER BY version DESC LIMIT 1";
            Object[] params = {flow_id};
            Flow flow = jdbcTemplate.queryForObject(sql, params, new RowMapper<Flow>(){
                @Override
                public Flow mapRow(ResultSet resultSet, int i) throws SQLException {
                    return getFlowFromResultSet(resultSet);
                }
            });
            return flow;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowDaoImpl.getNewVersionFlow@cannot find the flow|flow_id=%d", flow_id), e);
            return null;
        }
    }
    @Override
    public int getCurrRecordId(){
        String sql = "SELECT MAX(record_id) FROM flow ";
        Integer maxRecordId = jdbcTemplate.queryForObject(sql, Integer.class);
        return maxRecordId != null ? maxRecordId : -1;
    }
    @Override
    public int getCurrFlowId(){
        String sql = "SELECT MAX(flow_id) FROM flow ";
        Integer maxFlowId = jdbcTemplate.queryForObject(sql, Integer.class);
        return maxFlowId != null ? maxFlowId : -1;
    }

    @Override
    public int getFlowIdByRecordId(final int record_id){
        String sql = "SELECT flow_id from flow where record_id=?";
        Object[] params = {record_id};
        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }
}
