package com.zulong.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GroupControllerTest {
    @Autowired
    GroupController groupController;

    @Autowired
    UserController userController;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        jdbcTemplate.execute("drop table if exists administration");
        jdbcTemplate.execute("drop table if exists flow");
        jdbcTemplate.execute("drop table if exists flow_summary");
        jdbcTemplate.execute("drop table if exists core_meta");
        jdbcTemplate.execute("drop table if exists extra_meta");
        jdbcTemplate.execute("drop table if exists pack_group");
        jdbcTemplate.execute("drop table if exists instance");
        jdbcTemplate.execute("drop table if exists node");
        jdbcTemplate.execute("drop table if exists user");
        // 初始化数据库
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS administration(user_id varchar(20), group_id int, update_allowed boolean,delete_allowed boolean," +
                "  PRIMARY KEY (user_id, group_id))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS flow(record_id int primary key,flow_id int, version int, is_committed boolean, commit_message varchar(100),last_build varchar(100), core_meta_id int, extra_meta_id int, graph_data varchar(1000), blackboard varchar(1000))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS flow_summary(flow_id int primary key,name varchar(50), des varchar(200), last_build varchar(100), last_commit varchar(100), last_version int)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS core_meta (meta_id int primary key, version int, data varchar(255))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS extra_meta (meta_id int primary key, version int, group_id int, data varchar(255))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS pack_group (group_id int primary key, group_name varchar(25))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS instance (uuid varchar(25) primary key, flow_record_id int, build_time varchar(25), complete boolean, has_error boolean)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS node (instance_id varchar(20), node_id int primary key, start_time varchar(25), end_time varchar(25), options varchar(10000)) ");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS user (user_id varchar(20) primary key, is_admin boolean)");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // 删除数据库
        jdbcTemplate.execute("drop table if exists administration");
        jdbcTemplate.execute("drop table if exists flow");
        jdbcTemplate.execute("drop table if exists flow_summary");
        jdbcTemplate.execute("drop table if exists core_meta");
        jdbcTemplate.execute("drop table if exists extra_meta");
        jdbcTemplate.execute("drop table if exists pack_group");
        jdbcTemplate.execute("drop table if exists instance");
        jdbcTemplate.execute("drop table if exists node");
        jdbcTemplate.execute("drop table if exists user");
    }

    //创建用户1，用户2
    @Test
    void createUserTest(){
        Map<String, String> request = new HashMap<>();
        request.put("user_id", "createUserTest_user1");
        request.put("is_admin", String.valueOf(true));

        Map<String, String> request2 = new HashMap<>();
        request2.put("user_id", "createUserTest_user2");
        request2.put("is_admin", String.valueOf(true));

        Map<String, Object> result = userController.createUser(request);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("message"));

        Map<String, Object> result2 = userController.createUser(request2);
        assertEquals(20000, result2.get("code"));
        assertNotNull(result2.get("message"));
    }
    //创建组1，组2
    @Test
    void createGroupTest(){
        Map<String, String> request = new HashMap<>();
        request.put("group_name", "group1");

        Map<String, String> request2 = new HashMap<>();
        request2.put("group_name", "group2");

        Map<String, Object> result = groupController.createGroup(request);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        Map<String, Object> result2 = groupController.createGroup(request2);
        assertEquals(20000, result2.get("code"));
        assertNotNull(result2.get("data"));
    }
    //把用户1加入组1，组2，用户2加入组1
    @Test
    void addUserToGroupTest(){
        createUserTest();
        createGroupTest();
        Map<String, String> request = new HashMap<>();
        request.put("user_id", "createUserTest_user1");
        request.put("group_id", String.valueOf(1));

        Map<String, Object> result = userController.addToGroup(request);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("message"));

        Map<String, String> request2 = new HashMap<>();
        request2.put("user_id", "createUserTest_user2");
        request2.put("group_id", String.valueOf(1));

        Map<String, Object> result2 = userController.addToGroup(request2);
        assertEquals(20000, result2.get("code"));
        assertNotNull(result2.get("message"));
    }

    //获取组1的所有用户
    @Test
    void listUserTest(){
        addUserToGroupTest();
        Map<String, String> request = new HashMap<>();
        request.put("group_id", String.valueOf(1));

        Map<String, Object> result = groupController.getAllUsers(request, );
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

    }

    //获用户1的所有组
    @Test
    void listGroup(){
        addUserToGroupTest();
        Map<String, String> request = new HashMap<>();
        request.put("user_id", "createUserTest_user1");

        Map<String, Object> result = userController.getAllGroups(request);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }


    //将用户1从组1中移除
    @Test
    void RemoveUserFromGroupTest(){
        addUserToGroupTest();
        Map<String, String> request2 = new HashMap<>();
        request2.put("user_id", "createUserTest_user2");
        request2.put("group_id", String.valueOf(1));

        Map<String, Object> result2 = userController.removeFromGroup(request2);
        assertEquals(20000, result2.get("code"));
        assertNotNull(result2.get("message"));
    }

    //获取组1的所有用户

    //获用户1的所有组
}
