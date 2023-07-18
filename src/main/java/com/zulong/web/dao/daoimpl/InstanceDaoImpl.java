package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Instance;

import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("InstanceDaoImpl")
public class InstanceDaoImpl implements InstanceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public InstanceDaoImpl(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    /**
     * 通过flow_record_id，查找该流程有无对应的instance
     * @param record_id
     * @return
     */
    public boolean findInstanceByFlowID(int record_id){
        String sql = "select count(*) from instance where flow_record_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{record_id}, Integer.class);
        return count > 0;
    }

    @Cacheable(value="instanceCache", key="#uuid+'_'+#flow_record_id")
    public List<Instance> getInstanceByFlowRecordId(int record_id) {
        String sql = "select * from instance where flow_record_id = ?";
        Object[] params = {record_id};
        List<Instance> instanceList = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Instance.class));
        return instanceList;
    }

    @Cacheable(value = "instanceCache", key = "#uuid")
    @Override
    public Instance findInstanceByUuid(String uuid) {
        String sql = "select * from instance where uuid = ?";
        Object[] params = {uuid};
        Instance instance = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Instance.class));
        LoggerManager.logger().debug("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.findInstanceByUuid@insert failed|%s",instance);
        return instance;
    }

    @Override
    public boolean insertInstance(Instance instance) {
        String sql = "insert into instance (uuid, flow_record_id, build_time, complete, has_error) values (?, ?, ?, ?, ?)";
        Object[] params = {instance.getUuid(), instance.getFlow_record_id(), instance.getBuild_time(), instance.getComplete(), instance.getHas_error()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.insertInstance@insert failed");
        }
        return flag;
    }

    @Override
    public boolean updateInstance(int flow_record_id, boolean complete, boolean has_error, String uuid) {
        String sql = "update instance set flow_record_id=?, complete=?, has_error=? where uuid=?";
        Object[] params = {flow_record_id,complete,has_error,uuid};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.updateInstance@update failed");
        }
        return flag;
    }
}
