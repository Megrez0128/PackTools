package com.zulong.web.dao.daoimpl;


import com.zulong.web.dao.CoreMetaDao;
import com.zulong.web.entity.CoreMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import java.util.List;


@Service("coreMetaImpl")
public class CoreMetaDaoImpl implements CoreMetaDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CoreMeta findCoreMetaByID(int coreMetaID){
        String sql = "SELECT * FROM core_meta WHERE meta_id = ?";
        List<CoreMeta> coreMetaList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CoreMeta.class), coreMetaID);
        return coreMetaList.isEmpty() ? null : coreMetaList.get(0);
    }
    
    public boolean insertCoreMeta(CoreMeta coreMeta){
        String sql = "INSERT INTO core_meta (meta_id, version, data) VALUES (?, ?, ?)";
        int result = jdbcTemplate.update(sql, coreMeta.getMeta_id(), coreMeta.getVersion(), coreMeta.getData());
        return result > 0;
    }
    
    public boolean deleteCoreMeta(int coreMetaID){
        String sql = "DELETE FROM core_meta WHERE meta_id = ?";
        int result = jdbcTemplate.update(sql, coreMetaID);
        return result > 0;
    }
    
    @Override
    public int getMaxVersion(int meta_id) {
        String sql = "SELECT MAX(version) FROM core_meta WHERE meta_id = ?";
        Integer maxVersion = jdbcTemplate.queryForObject(sql, Integer.class, meta_id);
        return maxVersion != null ? maxVersion : 0;
    }
}
