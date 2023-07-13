package com.zulong.web.controller;

import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){ this.userService = userService; }

}
