package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Instance;

import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("InstanceDaoImpl")
public class InstanceDaoImpl implements InstanceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public InstanceDaoImpl(JdbcTemplate jdbcTemplate){ this.jdbcTemplate = jdbcTemplate; }

    /**
     * 通过flow_record_id，查找该流程有无对应的instance
     * TODO: 暂时设定Instance对应的表名为instance
     * @param record_id
     * @return
     */
    public boolean findInstanceByFlowID(int record_id){
        String sql = "select count(*) from instance where flow_record_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{record_id}, Integer.class);
        return count > 0;
    }

    @Override
    public Instance findInstanceByUuid(String uuid) {
        String sql = "select * from instance where uuid = ?";
        Instance instance = jdbcTemplate.queryForObject(sql, new Object[]{uuid}, Instance.class);
        return instance;
    }

    @Override
    public boolean insertInstance(Instance instance) {
        String sql = "insert into instance (uuid, flow_record_id, start_time, complete, has_error) values (?, ?, ?, ?, ?)";
        Object[] params = {instance.getUuid(), instance.getFlow_record_id(),instance.getBuild_time(),instance.getComplete(),instance.getHas_error()};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.insertInstance@insert failed");
        }
        return flag;
    }

    @Override
    public boolean updateInstance(int flow_record_id,boolean complete,boolean has_error,String uuid) {
        String sql = "update instance set flow_record_id=?, complete=?, has_error=? where uuid=?";
        Object[] params = {flow_record_id,complete,has_error,uuid};
        boolean flag = jdbcTemplate.update(sql, params) > 0;
        if(!flag){
            LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]InstanceDaoImpl.updateInstance@update failed");
        }
        return flag;
    }
}
