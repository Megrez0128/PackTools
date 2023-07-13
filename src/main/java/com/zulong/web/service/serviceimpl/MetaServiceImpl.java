package com.zulong.web.service.serviceimpl;

import com.zulong.web.service.MetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zulong.web.dao.CoreMetaDao;
import com.zulong.web.dao.ExtraMetaDao;

import com.zulong.web.entity.CoreMeta;
import com.zulong.web.entity.ExtraMeta;

@Service
public class MetaServiceImpl implements MetaService {
    @Autowired
    private CoreMetaDao coreMetaDao;
    @Autowired
    private ExtraMetaDao extraMetaDao;

    @Override
    public boolean createMeta(int meta_id, int group_id, String data){ 
        if(group_id == 0){
            // create core_meta
            //version自增
            //找到最大的version
            int version = coreMetaDao.getMaxVersion(meta_id);
            version++;
            
            //创建新的coreMeta对象
            CoreMeta coreMeta = new CoreMeta(meta_id, version, data);
            //插入新的meta
            coreMetaDao.insertCoreMeta(coreMeta);
            return true;
        } else {
            // create extra_meta
            //version自增
            //找到最大的version
            int version = extraMetaDao.getMaxVersion(meta_id);
            version++;

            //创建新的extraMeta对象
            ExtraMeta extraMeta = new ExtraMeta(meta_id, group_id, version, data);
            //插入新的meta
            extraMetaDao.insertExtraMeta(extraMeta);
            return true;            
        }
    }
}
