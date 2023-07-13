package com.zulong.web.dao.daoimpl;


import com.zulong.web.dao.CoreMetaDao;
import com.zulong.web.entity.CoreMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("coreMetaImpl")
public class CoreMetaDaoImpl implements CoreMetaDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CoreMeta findCoreMetaByID(int coreMetaID){
        return null;
    }
    public boolean insertCoreMeta(CoreMeta coreMeta){return false;}
    public boolean deleteCoreMeta(int coreMetaID){return false; }
    @Override
    public int getMaxVersion(int meta_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxVersion'");
    }
}
