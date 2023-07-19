package com.zulong.web.dao;

import com.zulong.web.entity.Meta;

import java.util.List;

public interface MetaDao {
    Meta findMetaByID(int metaId);
    public boolean insertMeta(Meta meta);
    public boolean deleteMeta(int metaId);
    public List<Meta> getAllMeta();
    int getMaxVersion(int meta_id);
}
