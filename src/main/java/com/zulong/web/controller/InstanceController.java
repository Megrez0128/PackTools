package com.zulong.web.controller;

import com.zulong.web.entity.Instance;
import com.zulong.web.service.InstanceService;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {
    private InstanceService instanceService;
    @Autowired
    public InstanceController(InstanceService instanceService)
    {
        this.instanceService = instanceService;
    }

    @PostMapping(value = "/instance")
    public Instance CreateInstance(@RequestParam String info){
        return new Instance();
    }

    @PostMapping(value = "/build")
    public void UpdateInstance(@RequestParam String info){

    }
}
