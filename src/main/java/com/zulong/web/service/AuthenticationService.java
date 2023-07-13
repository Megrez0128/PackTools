package com.zulong.web.service;

public interface AuthenticationService {
    boolean isUserInGroup(String user_id, Integer group_id);
    boolean hasMetaPermission(Integer group_id, Integer meta_id);
    boolean hasFlowPermission(Integer group_id, Integer flow_id);
    boolean hasInstancePermission(Integer group_id, Integer instance_id);
}
