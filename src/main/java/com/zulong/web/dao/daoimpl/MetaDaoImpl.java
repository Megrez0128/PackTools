package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.MetaDao;
import com.zulong.web.entity.CoreMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.zulong.web.entity.Meta;
import java.util.List;

@Service("MetaImpl")
public class MetaDaoImpl implements MetaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable(value = "metaCache", key = "#metaId")
    @Override
    public Meta findMetaByID(int metaId){
        String sql = "SELECT * FROM pack_meta WHERE meta_id = ?";
        List<Meta> metaList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Meta.class), metaId);
        return metaList.isEmpty() ? null : metaList.get(0);
    }

    @CacheEvict(value = "metaCache", allEntries = true)
    @CachePut(value = "metaCache", key = "#coreMeta.meta_id")
    @Override
    public boolean insertMeta(Meta coreMeta){
        String sql = "INSERT INTO pack_meta (meta_id, version, data, group_id, version_display) VALUES (?, ?, ?, ?, ?)";
        int result = jdbcTemplate.update(sql, coreMeta.getMeta_id(), coreMeta.getVersion(), coreMeta.getData(), coreMeta.getGroup_id(), coreMeta.getVersion_display());
        return result > 0;
    }

    @CacheEvict(value="metaCache", key="#metaId")
    @Override
    public boolean deleteMeta(int metaId){
        String sql = "DELETE FROM pack_meta WHERE meta_id = ?";
        int result = jdbcTemplate.update(sql, metaId);
        return result > 0;
    }
    @Override
    public List<Meta> getAllMeta(){
        String sql = "SELECT * FROM pack_meta ";
        List<Meta> metaList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Meta.class));
        return metaList;
    }
    @Override
    public Meta updateMeta(int meta_id,int group_id,String data,String version_display){
        return null;
    }

    @Override
    public int getMaxVersion(int meta_id) {
        String sql = "SELECT MAX(version) FROM pack_meta WHERE meta_id = ?";
        Integer maxVersion = jdbcTemplate.queryForObject(sql, Integer.class, meta_id);
        return maxVersion != null ? maxVersion : 0;
    }

    @Override
    public int getCurrMetaId(){
        String sql = "SELECT MAX(meta_id) FROM pack_meta ";
        Integer maxMetaId = jdbcTemplate.queryForObject(sql, Integer.class);
        return maxMetaId != null ? maxMetaId : -1;
    }
}
