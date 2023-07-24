package com.zulong.web.service;


import com.zulong.web.entity.Meta;

import java.util.List;
import java.util.Map;

public interface MetaService {
    Meta createMeta(String version_display, int group_id, String data);
    Map<String, Object> getAllMeta();
    Meta updateMeta(int meta_id,int group_id,String data,String version_display);
    Meta getMetaDetails(int meta_id);
}