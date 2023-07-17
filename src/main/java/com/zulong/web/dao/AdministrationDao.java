package com.zulong.web.dao;

import com.zulong.web.entity.relation.Administration;

public interface AdministrationDao {
    boolean insertAdministration(String user_id, Integer group_id, boolean update_allowed, boolean delete_allowed);
    boolean updateAdministration(String user_id, Integer group_id, boolean update_allowed,boolean delete_allowed);
    boolean createAdministration(Administration administration);
    boolean deleteAdministration(String user_id, Integer group_id);
    boolean isUserInGroup(String user_id, Integer group_id);
}
