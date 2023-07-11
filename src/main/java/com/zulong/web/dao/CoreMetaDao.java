package com.zulong.web.dao;

import com.zulong.web.entity.CoreMeta;

public interface CoreMetaDao {
    CoreMeta findCoreMetaByID(int coreMetaID);
    public boolean insertCoreMeta(CoreMeta coreMeta);
    public boolean deleteCoreMeta(int coreMetaID);
}
