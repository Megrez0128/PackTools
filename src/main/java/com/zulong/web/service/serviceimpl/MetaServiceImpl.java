package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.MetaDao;
import com.zulong.web.entity.Meta;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.MetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zulong.web.dao.CoreMetaDao;
import com.zulong.web.dao.ExtraMetaDao;

import com.zulong.web.entity.CoreMeta;
import com.zulong.web.entity.ExtraMeta;

import java.util.List;

@Service
public class MetaServiceImpl implements MetaService {
    @Autowired
    private CoreMetaDao coreMetaDao;
    @Autowired
    private ExtraMetaDao extraMetaDao;
    @Autowired
    private MetaDao metaDao;
    // 设置meta_id的起始值
    final static int START_META_ID = 1000;
    // 设置version的起始值
    final static int START_VERSION = 0;
    int curr_meta_id = START_META_ID;
    @Override
    public boolean createMeta(String version_display, int group_id, String data){
        Meta meta = new Meta();
        meta.setMeta_id(curr_meta_id);
        meta.setGroup_id(group_id);
        meta.setVersion(START_VERSION);
        meta.setVersion_display(version_display);
        meta.setData(data);

        boolean flag = metaDao.insertMeta(meta);

        if(!flag){
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.daoimpl.serviceimpl]MetaServiceImpl.createMeta@create meta|curr_meta_id=%s|group_id=%d|version_display=%s", curr_meta_id, group_id, version_display));

        }
        return flag;
    }

    @Override
    public List<Meta> getAllMeta(){
        return metaDao.getAllMeta();
    }
}
