package com.zulong.web.controller;

import com.zulong.web.entity.Group;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.AuthenticationService;
import com.zulong.web.service.UserService;
import com.zulong.web.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static com.zulong.web.config.ConstantConfig.*;

@RestController
@RequestMapping(value = "/auth/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService){
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/listgroup")
    public Map<String, Object> getAllGroups(@RequestBody Map<String, String> request) {
        String user_id;
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try{
            user_id = TokenUtils.getCurr_user_id();
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@please login first|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            List<Group> groupList = userService.getAllGroups(user_id);
            data.put("items", groupList);
            data.put("user_id", user_id);
            response.put("code", RETURN_SUCCESS);
            response.put("data", data);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.getAllGroups@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity createUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        if(authenticationService.isAdmin(TokenUtils.getCurr_user_id())){

        }else {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@this user is not admin|"));
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", "this user is not admin");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        String user_id;
        boolean is_admin;

        try{
            user_id = request.get("user_id");
            is_admin = Boolean.parseBoolean(request.get("is_admin"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        try {
            userService.createUser(user_id, is_admin);
            response.put("code", RETURN_SUCCESS);
            response.put("message", "success");

        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        try {
            User user = new User();
            user.setAdmin(is_admin);
            user.setUser_id(user_id);
            String token = TokenUtils.sign(user);
            //将令牌放置在响应头中返回
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authentication", "Bearer " + token);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authentication", "Bearer " + token)
                    .body(response);
        }catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity loginUser(@RequestBody Map<String, String> request) {
        String user_id;
        Map<String, Object> response = new HashMap<>();
        try{
            user_id = request.get("user_id");
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        User user = null;
        try {
            user = userService.getUserByUserId(user_id);
            if(user != null){
                LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@User does not exist|user_id=%s", user_id));
                response.put("code", RETURN_SERVER_WRONG);
                response.put("message","User does not exist" );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            response.put("code", RETURN_SUCCESS);
            response.put("message", "success");
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        try {
            String token = TokenUtils.sign(user);
            //将令牌放置在响应头中返回
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authentication", "Bearer " + token);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authentication", "Bearer " + token)
                    .body(response);
        }catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.createUser@operation failed|user_id=%s", user_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @PostMapping(value = "/addtogroup")
    public Map<String, Object> addToGroup(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        if(authenticationService.isAdmin(TokenUtils.getCurr_user_id())){

        }else {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@this user is not admin|"));
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", "this user is not admin");
            return response;
        }
        String user_id;
        int group_id;
        int code;
        String message;
        try{
            user_id = request.get("user_id");
            group_id = Integer.parseInt(request.get("group_id"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.addToGroup@params are wrong|"), e);
            response = new HashMap<>();
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            response = new HashMap<>();
            boolean flag = userService.addToGroup(user_id, group_id);
            if(flag) { code = RETURN_SUCCESS; message = "success";}
            else { code = RETURN_DATABASE_WRONG; message = "failed";}
            response.put("code", code);
            response.put("message", message);
            return response;
        } catch (Exception e) {
            response = new HashMap<>();
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.addToGroup@operation failed|user_id=%s|group_id=%d", user_id, group_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    @PostMapping(value = "/removefromgroup")
    public Map<String, Object> removeFromGroup(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        if(authenticationService.isAdmin(TokenUtils.getCurr_user_id())){

        }else {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]GroupController.getAllUsers@this user is not admin|"));
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", "this user is not admin");
            return response;
        }
        String user_id;
        int group_id;
        try{
            user_id = request.get("user_id");
            group_id = Integer.parseInt(request.get("group_id"));
        }catch (Exception e){
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.removeFromGroup@params are wrong|"), e);
            response.put("code", RETURN_PARAMS_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
        try {
            boolean flag = userService.removeFromGroup(user_id, group_id);
            int code;
            String message;
            if(flag) { code = RETURN_SUCCESS; message = "success";}
            else {
                LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.removeFromGroup@operation failed|user_id=%s|group_id=%d", user_id, group_id));
                code = RETURN_DATABASE_WRONG;
                message = "failed";
            }
            response.put("code", code);
            response.put("message", message);
            return response;
        } catch (Exception e) {
            LoggerManager.logger().warn(String.format("[com.zulong.web.controller]UserController.removeFromGroup@operation failed|user_id=%s|group_id=%d", user_id, group_id), e);
            response.put("code", RETURN_SERVER_WRONG);
            response.put("message", e.getMessage());
            return response;
        }
    }

    //todo:将要废弃，因为仅仅用于返回user_id为admin的用户的token
    @PostMapping(value = "/gettoken")
    public ResponseEntity getToken(@RequestBody Map<String, String> request){
        String user_id = request.get("user_id");
        User admin_user = new User();
        admin_user.setAdmin(true);
        admin_user.setUser_id(user_id);

        if (user_id.equals("admin")) {
            //身份验证成功，生成JWT令牌
            String token = TokenUtils.sign(admin_user);
            //将令牌放置在响应头中返回
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authentication", "Bearer " + token);
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        //身份验证失败，返回401状态码
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
