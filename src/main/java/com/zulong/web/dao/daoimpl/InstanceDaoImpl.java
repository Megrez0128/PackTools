package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Instance;

import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
     * 在Flow相关操作时会使用，不返回instance实例
     * @param record_id
     * @return
     */
    final public boolean findInstanceByFlowID(int record_id){
        String sql = "select count(*) from instance where flow_record_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{record_id}, Integer.class);
        return count > 0;
    }

    //@Cacheable(value="instanceCache", key="#uuid+'_'+#flow_record_id")
    final public List<Instance> getInstanceByFlowRecordId(int record_id) {
        String sql = "select * from instance where flow_record_id = ?";
        Object[] params = {record_id};
        List<Instance> instanceList = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Instance.class));
        return instanceList;
    }

    //@Cacheable(value = "instanceCache", key = "#uuid")
    @Override
    final public Instance findInstanceByUuid(String uuid) {
        try {
            String sql = "select * from instance where uuid = ?";
            Object[] params = {uuid};
            Instance instance = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Instance.class));
            return instance;
        } catch (Exception e) {
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.findInstanceByUuid@select failed|uuid=%s", uuid));
            return null;
        }
    }

//    @CacheEvict(value = "instanceCache", allEntries = true)
//    @CachePut(value = "instanceCache", key = "#instance.uuid")
    @Override
    final public boolean insertInstance(Instance instance) {
        String sql = "insert into instance (uuid, flow_record_id, start_time, complete, has_error) values (?, ?, ?, ?, ?)";
        Object[] params = {instance.getUuid(), instance.getFlow_record_id(), instance.getStart_time(), false, instance.getHas_error()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.insertInstance@insert failed|uuid=%s", instance.getUuid()));
        }
        return flag;
    }

    @Override
    public boolean updateInstance(int flow_record_id, boolean complete, boolean has_error, String uuid) {
        String sql = "update instance set flow_record_id=?, complete=?, has_error=? where uuid=?";
        Object[] params = {flow_record_id, complete, has_error, uuid};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.updateInstance@update failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid));
        }
        return flag;
    }

    @Override
    public boolean endInstance(int flow_record_id, String end_time, boolean complete, boolean has_error, String uuid) {
        String sql = "update instance set flow_record_id=?, end_time=?, complete=?, has_error=? where uuid=?";
        Object[] params = {flow_record_id, end_time, complete, has_error, uuid};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().error(String.format("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.endInstance@update failed|flow_record_id=%d|uuid=%s", flow_record_id, uuid));
        }
        return flag;
    }
}
