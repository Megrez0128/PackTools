package com.zulong.web.service;


import com.zulong.web.entity.Meta;

import java.util.List;

public interface MetaService {
    boolean createMeta(String version_display, int group_id, String data);
    List<Meta> getAllMeta();
}