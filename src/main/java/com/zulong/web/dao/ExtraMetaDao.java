package com.zulong.web.dao;

import com.zulong.web.entity.ExtraMeta;

public interface ExtraMetaDao {
    ExtraMeta findExtraMetaByID(int extraMetaID);
    boolean insertExtraMeta(ExtraMeta extraMeta);
    boolean deleteExtraMeta(int extraMetaID);
}
