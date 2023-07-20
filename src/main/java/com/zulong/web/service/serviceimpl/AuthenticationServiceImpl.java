package com.zulong.web.service.serviceimpl;

import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zulong.web.service.AuthenticationService;
import com.zulong.web.dao.GroupFlowDao;
import com.zulong.web.dao.ExtraMetaDao;
import com.zulong.web.dao.InstanceDao;
import com.zulong.web.dao.AdministrationDao;

import com.zulong.web.entity.Instance;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
        
    @Autowired
    private GroupFlowDao groupFlowDao;
    
    @Autowired
    private ExtraMetaDao extraMetaDao;
    
    @Autowired
    private InstanceDao instanceDao;
    
    @Autowired
    private AdministrationDao administrationDao;

    @Autowired
    private UserService userService;

    @Override
    public boolean isUserInGroup(String user_id, Integer group_id) {
        //查询Administration表，判断user_id和group_id是否存在于该表中
        boolean is_in = administrationDao.isUserInGroup(user_id, group_id);
        if(is_in){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasMetaPermission(Integer group_id, Integer meta_id) {
        //查询extra_meta表，判断meta_id和group_id是否存在于该表中
        boolean is_in = extraMetaDao.hasMetaPermission(group_id, meta_id);
        if(is_in){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasFlowPermission(Integer group_id, Integer flow_id) {
        //查询group_flow表，判断flow_id和group_id是否存在于该表中
        boolean is_in = groupFlowDao.hasFlowPermission(group_id, flow_id);
        if(is_in){
            return true;
        }
        return false;
    }

    /**
     * 查询一个group有无instance对应的权限
     * @param group_id
     * @param uuid
     * @return
     */
    @Override
    public boolean hasInstancePermission(Integer group_id, String uuid) {
        //先查询instance表，找到Instance_id对应的flow_id
        Instance instance = instanceDao.findInstanceByUuid(uuid);
        if(instance == null){
            return false;
        }
        //再查询group_flow表，判断flow_id和group_id是否存在于该表中
        boolean is_in = groupFlowDao.hasFlowPermission(group_id, instance.getFlow_record_id());
        if(is_in){
            return true;
        }
        return false;
    }

    /**
     * 表示用户能否使用对应的Flow，从该函数可以拓展到instance的使用
     * @param user_id
     * @param flow_id
     * @return
     */
    @Override
    public boolean canUserUseFlow(String user_id, Integer flow_id) {
        List<Group> grouplist = userService.getAllGroups(user_id);
        for(Group group : grouplist) {
            if(hasFlowPermission(group.getGroup_id(), flow_id)) return true;
            else continue;
        }
        return false;
    }

    @Override
    public boolean isAdmin(String user_id) {
        User user = userService.getUserByUserId(user_id);
        return user.isAdmin();
    }

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    public String getUserIdFromToken(String header) {

        //TODO: 从token中解析出user_id
        //暂时先写死，有一些保留的user_id，就像有一些保留的group_id一样
        String user_id = "admin";
        return user_id;        
    }
}
