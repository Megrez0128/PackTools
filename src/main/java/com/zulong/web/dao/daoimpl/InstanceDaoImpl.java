package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.InstanceDao;
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

}
