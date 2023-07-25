package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.MetaDao;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.zulong.web.entity.Meta;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zulong.web.entity.Meta.getMetaFromResultSet;

@Service("MetaImpl")
public class MetaDaoImpl implements MetaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Cacheable(value = "metaCache", key = "#metaId")
    @Override
    public Meta findMetaByID(int metaId){
        String sql = "SELECT * FROM pack_meta WHERE meta_id = ?";
        List<Meta> metaList = jdbcTemplate.query(sql, new Object[]{metaId}, new RowMapper<Meta>() {
            @Override
            public Meta mapRow(ResultSet resultSet, int i) throws SQLException {
                return getMetaFromResultSet(resultSet);
            }
        });
        return metaList.isEmpty() ? null : metaList.get(0);
    }

    @SneakyThrows
    @CacheEvict(value = "metaCache", allEntries = true)
    @CachePut(value = "metaCache", key = "#coreMeta.meta_id")
    @Override
    public boolean insertMeta(Meta coreMeta){
        String sql = "INSERT INTO pack_meta (meta_id, version, data, group_id, version_display) VALUES (?, ?, ?, ?, ?)";
        int result = jdbcTemplate.update(sql, coreMeta.getMeta_id(), coreMeta.getVersion(), new SerialBlob(coreMeta.getData().getBytes()), coreMeta.getGroup_id(), coreMeta.getVersion_display());
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
    public List<Object> getAllMeta(){

        String sql = "SELECT * FROM pack_meta ";
        List<Meta> metaList = jdbcTemplate.query(sql, new RowMapper<Meta>() {
            @Override
            public Meta mapRow(ResultSet resultSet, int i) throws SQLException {
                return getMetaFromResultSet(resultSet);
            }
        });
        Map<String, Object> map = new HashMap<>();
        List<Object> response = new ArrayList<>();
        for (Meta meta : metaList) {
            map = new HashMap<>();
            map.put("meta_id", meta.getMeta_id());
            map.put("group_id", meta.getGroup_id());
            map.put("version", meta.getVersion());
            map.put("version_display", meta.getVersion_display());
            response.add(map);
        }
        return response;
    }

    // TODO: 为什么updateMeta是return null也没有其他操作？是不是对接了无效接口，可以删除？
    @Override
    public Meta updateMeta(int meta_id,int group_id,String data,String version_display){
        return null;
    }

    @Override
    public int getMaxVersion(final int meta_id) {
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
