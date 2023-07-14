package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.InstanceDao;
import com.zulong.web.entity.Instance;
import com.zulong.web.log.LoggerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
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
    public Instance findInstanceByID(int uuid) {
        String sql = "select count(*) from instance where uuid = ?";
        Instance instance = jdbcTemplate.queryForObject(sql, new Object[]{uuid}, Instance.class);
        return instance;
    }

    @Override
    public void insertInstance(Instance instance) {
        String sql = "insert into instance (uuid, flow_record_id, node_id, start_time, end_time, complete, has_error) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, instance.getUuid(), instance.getFlow_record_id(),instance.getNode_id(),instance.getStart_time(),instance.getEnd_time(),instance.getComplete(),instance.getHas_error());
    }

}
