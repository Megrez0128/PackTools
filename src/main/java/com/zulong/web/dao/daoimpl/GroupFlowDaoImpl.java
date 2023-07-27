package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.GroupFlowDao;
import com.zulong.web.entity.relation.GroupFlow;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("GroupFlowDaoImpl")
public class GroupFlowDaoImpl implements GroupFlowDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public GroupFlowDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public boolean hasFlowPermission(Integer group_id, Integer flow_id){
        try{
            String sql = "select count(*) from group_flow where group_id = ? and flow_id = ?";
            Object[] params = {group_id, flow_id};
            boolean flag = jdbcTemplate.queryForObject(sql, params, Integer.class) > 0;
            return flag;
        }catch (Exception e){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]GroupFlowDaoImpl.hasFlowPermission@Permission verify failed|group_id=%d|flow_id=%d",group_id, flow_id));
            return false;
        }
    }
    @Override
    public boolean insertGroupFlow(Integer group_id, Integer flow_id){
        String sql = "insert into group_flow(group_id, flow_id) values (?, ?)";
        Object[] params = {group_id, flow_id};
        boolean flag = jdbcTemplate.update(sql,params) > 0;
        if(!flag){
            LoggerManager.logger().error("[com.zulong.web.dao.daoimpl]GroupFlowDaoImpl.insertGroupFlow@insert failed|group_id=%d|flow_id=%d", group_id, flow_id);
        }
        return flag;
    }

    @Override
    public List<GroupFlow> getFlowListByGroupId(Integer group_id){
        try{
            String sql = "select * from group_flow where group_id = ?";
            Object[] params = {group_id};
            List<GroupFlow> groupFlowList = jdbcTemplate.query(sql,params,new BeanPropertyRowMapper<>(GroupFlow.class));
            return groupFlowList;
        }catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.dao.daoimpl]GroupFlowDaoImpl.getFlowListByGroupId@getFlowListByGroupId failed|group_id=%d", group_id);
            return null;
        }
    }

    @Override
    public List<Integer> getFlowIdByGroupId(Integer group_id) {
        try{
            String sql = "select flow_id from group_flow where group_id = ?";
            Object[] params = {group_id};
            List<Integer> groupFlowList = jdbcTemplate.query(sql,params,new BeanPropertyRowMapper<>(Integer.class));
            return groupFlowList;
        }catch (Exception e){
            LoggerManager.logger().error("[com.zulong.web.dao.daoimpl]GroupFlowDaoImpl.getFlowListByGroupId@getFlowIdByGroupId failed|group_id=%d", group_id);
            return null;
        }
    }
}
