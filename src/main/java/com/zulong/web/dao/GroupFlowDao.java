package com.zulong.web.dao;

import com.sun.java.swing.plaf.windows.WindowsTextAreaUI;
import com.zulong.web.entity.relation.GroupFlow;

import java.util.List;

public interface GroupFlowDao {

    boolean hasFlowPermission(Integer group_id, Integer flow_id);

    boolean insertGroupFlow(Integer group_id, Integer flow_id);

    List<GroupFlow> getFlowListByGroupId(Integer group_id);
    List<Integer> getFlowIdByGroupId(Integer group_id);
}
