package com.zulong.web.service;

import java.util.ArrayList;

public interface InstanceService {
    public void UploadInstance(String uuid, String flow_id, String node_id, ArrayList<String> option);
    public void PullAndBuild();
}
