package com.zulong.web.dao;

public interface AdministrationDao {
    boolean createAdministration(String user_id, Integer group_id, boolean update_allowed,boolean delete_allowed);
    boolean updateAdministration(String user_id, Integer group_id, boolean update_allowed,boolean delete_allowed);
    boolean deleteAdministration(String user_id, Integer group_id);
    boolean isUserInGroup(String user_id, Integer group_id);
}
