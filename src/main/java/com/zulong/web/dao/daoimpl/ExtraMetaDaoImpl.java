package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.ExtraMetaDao;
import com.zulong.web.entity.ExtraMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("extraMetaImpl")
public class ExtraMetaDaoImpl implements ExtraMetaDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public ExtraMeta findExtraMetaByID(int extraMetaID){ return null; }
    public boolean insertExtraMeta(ExtraMeta extraMeta){ return false; }
    public boolean deleteExtraMeta(int extraMetaID){ return false; }
}
