package com.zulong.web.dao.daoimpl;

import com.zulong.web.dao.AdministrationDao;

public class AdministrationDaoImpl implements AdministrationDao {

    public boolean createAdministration(String user_id, Integer group_id, boolean update_allowed,boolean delete_allowed) { return true; }
    public boolean updateAdministration(String user_id, Integer group_id, boolean update_allowed,boolean delete_allowed) { return true; }
    public boolean deleteAdministration(String user_id, Integer group_id) { return true; }
    public boolean isUserInGroup(String user_id, Integer group_id) { return true; }
}
