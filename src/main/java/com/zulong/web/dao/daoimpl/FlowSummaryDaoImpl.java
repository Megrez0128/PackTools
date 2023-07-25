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

    //@Cacheable(value="flowSummaryCache", key="#flow_id")  此处不能用这条注解，否则会报错
    @Override
    public FlowSummary findFlowSummaryByID(int flow_id) {
        String sql = "select * from flow_summary where flow_id=?";
        Object[] params = {flow_id};
        try {
            FlowSummary flowSummary = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(FlowSummary.class));
            return flowSummary;
        } catch (Exception e) {
            // 如果没有找到对应的记录，返回null；添加一条warn日志
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.findFlowSummaryByID@flow_id is invalid|flow_id=%d", flow_id), e);
            return null;
        }
    }

    @Override
    public boolean insertFlowSummary(FlowSummary flowSummary) {
        //插入流程摘要
        String sql = "insert into flow_summary(flow_id, name, des, last_build, last_commit, last_version) values(?,?,?,?,?,?)";
        Object[] params = {flowSummary.getFlow_id(), flowSummary.getName(), flowSummary.getDes(), flowSummary.getLast_build(), flowSummary.getLast_commit(), flowSummary.getLast_version()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.insertFlowSummary@something wrong happened during insertion|"));
        }
        return flag;
    }

    @Override
    public List<FlowSummary> getFlowSummaryList(){
        try{
            String sql = "select * from flow_summary";
            List<FlowSummary> flowSummaryList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FlowSummary.class));
            return flowSummaryList;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.getFlowSummaryList@cannot find flow summary list|"), e);
            return null;
        }
    }

    @Override
    public void saveUpdateFlowSummary(FlowSummary flowSummary) {
        try {
            // 根据flow_id更新流程摘要
            String sql = "update flow_summary set name=?, des=?, last_build=?, last_commit=?, last_version=? where flow_id=?";
            Object[] params = {flowSummary.getName(), flowSummary.getDes(), flowSummary.getLast_build(), flowSummary.getLast_commit(), flowSummary.getLast_version(), flowSummary.getFlow_id()};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.saveUpdateFlowSummary@something wrong happened during update|"));
            }
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.saveUpdateFlowSummary@something wrong happened during update|"), e);
        }
    }

    @Override
    public boolean cloneUpdateFlowSummary(int flow_id, String name, String des){
        try {
            // 根据flow_id更新流程摘要
            String sql = "update flow_summary set name=?, des=? where flow_id=?";
            Object[] params = {name, des, flow_id};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.cloneUpdateFlowSummary@something wrong happened during update|"));
            }
            return flag;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]FlowSummaryDaoImpl.cloneUpdateFlowSummary@something wrong happened during update|"), e);
            return false;
        }
    }
    @Override
    public boolean commitUpdateFlowSummary(int flow_id,String last_commit){
        try {
            // 根据flow_id更新流程摘要
            String sql = "update flow_summary set last_commit=? where flow_id=?";
            Object[] params = {last_commit, flow_id};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                // 使用占位符{}来替代字符串拼接，并指定更新失败的原因和影响
                LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]GroupDaoImpl.commitUpdateFlowSummary@update failed,cause=database error,impact=data inconsistency|flow_id={}|last_commit={}", flow_id, last_commit);
            }
            return flag;
        } catch (Exception e) {
            // 使用占位符{}来替代字符串拼接，并指定抛出异常的原因、影响和解决方案
            LoggerManager.logger().error("[com.zulong.web.dao.daoimpl]GroupDaoImpl.commitUpdateFlowSummary@throw exception,impact=data inconsistency,solution=check the exception stack trace and fix the bug|flow_id={}|last_commit={}|cause={}", flow_id, last_commit, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean buildUpdateFlowSummary(int flow_id,String last_build){
        try {
            // 根据flow_id更新流程摘要
            String sql = "update flow_summary set last_build=? where flow_id=?";
            Object[] params = {last_build, flow_id};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                // 使用占位符{}来替代字符串拼接，并指定更新失败的原因和影响
                LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]GroupDaoImpl.buildUpdateFlowSummary@update failed,cause=database error,impact=data inconsistency|flow_id={}|last_build={}|", flow_id, last_build);
            }
            return flag;
        } catch (Exception e) {
            // 使用占位符{}来替代字符串拼接，并指定抛出异常的原因、影响和解决方案
            LoggerManager.logger().error("[com.zulong.web.dao.daoimpl]GroupDaoImpl.buildUpdateFlowSummary@throw exception,impact=data inconsistency,solution=check the exception stack trace and fix the bug|flow_id={}|last_build={}|cause={}|", flow_id, last_build, e.getMessage(), e);
            return false;
        }
    }
}
