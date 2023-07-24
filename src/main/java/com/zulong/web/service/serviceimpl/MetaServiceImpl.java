package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.MetaDao;
import com.zulong.web.entity.Meta;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.MetaService;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zulong.web.dao.CoreMetaDao;
import com.zulong.web.dao.ExtraMetaDao;

import java.util.List;
import java.util.Map;

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

    private int getCurrMetaId(){
        return metaDao.getCurrMetaId() != -1 ? metaDao.getCurrMetaId() : START_META_ID;
    }

    @SneakyThrows
    @Override
    public Meta createMeta(String version_display, int group_id, String data) {
        curr_meta_id = getCurrMetaId();

        Meta meta = new Meta();
        meta.setMeta_id(curr_meta_id + 1);
        meta.setGroup_id(group_id);
        meta.setVersion(START_VERSION);
        meta.setVersion_display(version_display);
        meta.setData(data);

        boolean flag = metaDao.insertMeta(meta);
        curr_meta_id++;
        if(!flag){
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.service.serviceimpl]MetaServiceImpl.createMeta@create meta|curr_meta_id=%s|group_id=%d|version_display=%s", curr_meta_id, group_id, version_display));
            return null;
        }
        return meta;
    }

    @Override
    public Map<String, Object> getAllMeta(){
        return metaDao.getAllMeta();
    }

    @Override
    public Meta updateMeta(int meta_id,int group_id,String data,String version_display){
        return metaDao.updateMeta( meta_id, group_id, data, version_display);
    }

    @Override
    public Meta getMetaDetails(int meta_id) {
        return metaDao.findMetaByID(meta_id);
    }
}
