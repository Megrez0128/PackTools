package com.zulong.web.dao.daoimpl;

import com.alibaba.fastjson.JSON;
import com.zulong.web.dao.ExtraMetaDao;
import com.zulong.web.entity.ExtraMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service("extraMetaImpl")
public class ExtraMetaDaoImpl implements ExtraMetaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ExtraMeta findExtraMetaByID(int extraMetaID) {
        String sql = "select * from extra_meta where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ExtraMeta.class), extraMetaID);
        } catch (DataAccessException e) {
            return null;

        }
    }

    @Override
    public boolean insertExtraMeta(ExtraMeta extraMeta) {
        String sql = "insert into extra_meta (group_id, version, data) values (?, ?, ?)";
        int result = jdbcTemplate.update(sql, extraMeta.getGroup(), extraMeta.getVersion(), JSON.toJSONString(extraMeta.getData()));
        return result > 0;
    }

    @Override
    public boolean deleteExtraMeta(int extraMetaID) {
        String sql = "delete from extra_meta where id = ?";
        int result = jdbcTemplate.update(sql, extraMetaID);
        return result > 0;
    }
}
