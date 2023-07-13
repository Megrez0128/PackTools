package com.zulong.web.controller;

import com.zulong.web.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/mymeta")
public class MetaController {
    private MetaService metaService;

    @Autowired
    public MetaController(MetaService metaService) {this.metaService = metaService;}

    @PostMapping(value = "/create")
    public Map<String, Object> createMeta(@RequestBody Map<String, String> request) {
        Integer meta_id = Integer.valueOf(request.get("meta_id"));
        Integer group_id = Integer.valueOf(request.get("group_id"));
        String data = request.get("data");
        boolean is_success = metaService.createMeta(meta_id,group_id,data);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 20000);
        response.put("message","success");
        return response;
    }

}
