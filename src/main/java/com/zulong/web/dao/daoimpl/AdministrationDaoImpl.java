package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.AdministrationDao;
import com.zulong.web.entity.relation.Administration;
import com.zulong.web.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("AdministrationDaoImpl")
public class AdministrationDaoImpl implements AdministrationDao {

    @Autowired
    AdministrationDao administrationDao;
    private final JdbcTemplate jdbcTemplate;

    public AdministrationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 或许已经被重写了，请调用createAdministration和deleteAdministration
    public boolean insertAdministration(String user_id, Integer group_id, boolean update_allowed, boolean delete_allowed) { return true; }
    public boolean updateAdministration(String user_id, Integer group_id, boolean update_allowed,boolean delete_allowed) {
        if(!isUserInGroup(user_id, group_id)) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.updateAdministration@the user isn't in the group|user_id=%s|group_id=%d", user_id, group_id));
            return false;
        }
        try {
            String sql = "update administration set updated_allowed=?, delete_allowed where user_id=? and group_id=?";
            Object[] params = {update_allowed, delete_allowed, user_id, group_id};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]FlowDaoImpl.updateFlow@update failed|user_id=%s|group_id=%d", user_id, group_id);
            }
            return flag;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.updateAdministration@update operation failed"), e);
            return false;
        }
    }

    public boolean createAdministration(Administration administration) {
        try {
            String sql = "insert into administration(user_id, group_id, update_allowed, delete_allowed)values(?, ?, ?, ?)";
            Object[] params = {administration.user_id, administration.group_id, administration.update_allowed, administration.delete_allowed};
            boolean flag = jdbcTemplate.update(sql, params) > 0;
            if(!flag){
                LoggerManager.logger().warn("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.createAdministration@insertion failed");
            }
            return flag;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.createAdministration@insertion failed"), e);
            return false;
        }
    }
    public boolean deleteAdministration(String user_id, Integer group_id) {
        if(!isUserInGroup(user_id, group_id)) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.deleteAdministration@the user isn't in the group|user_id=%s|group_id=%d", user_id, group_id));
            return false;
        }
        try {
            String sql = "delete from flow where user_id=? and group_id=?";
            Object[] params = {user_id, group_id};
            boolean flag1 = jdbcTemplate.update(sql, params) > 0;
            if(!flag1) {
                LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.deleteAdministration@deletion failed|user_id=%s|group_id=%d", user_id, group_id));
            }
            return flag1;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.deleteAdministration@deletion failed"), e);
            return false;
        }
    }

    @Cacheable(value="administrationCache", key="#user_id+'_'+#group_id")
    public boolean isUserInGroup(String user_id, Integer group_id) {
        try {
            String sql = "select * from flow where user_id = ? and group_id = ?";
            Object[] params = new Object[]{user_id, group_id};
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(boolean.class));
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.dao.daoimpl]AdministrationDaoImpl.isUserInGroup@cannot find connected administration|user_id=%s|group_id=%d", user_id, group_id), e);
            return false;
        }
    }
}
