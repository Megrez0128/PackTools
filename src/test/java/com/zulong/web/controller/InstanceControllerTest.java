package com.zulong.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InstanceControllerTest {
    @Autowired
    InstanceController instanceController;

    @Autowired
    private FlowController flowController;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        jdbcTemplate.execute("drop table if exists test_user");
        jdbcTemplate.execute("drop table if exists flow");
        jdbcTemplate.execute("drop table if exists flow_summary");
        jdbcTemplate.execute("drop table if exists core_meta");
        jdbcTemplate.execute("drop table if exists extra_meta");
        jdbcTemplate.execute("drop table if exists pack_group");
        jdbcTemplate.execute("drop table if exists instance");
        jdbcTemplate.execute("drop table if exists node");
        jdbcTemplate.execute("drop table if exists user");
        // 初始化数据库
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS test_user(user_id varchar(20) primary key, is_admin boolean)");
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
        jdbcTemplate.execute("drop table if exists test_user");
        jdbcTemplate.execute("drop table if exists flow");
        jdbcTemplate.execute("drop table if exists flow_summary");
        jdbcTemplate.execute("drop table if exists core_meta");
        jdbcTemplate.execute("drop table if exists extra_meta");
        jdbcTemplate.execute("drop table if exists pack_group");
        jdbcTemplate.execute("drop table if exists instance");
        jdbcTemplate.execute("drop table if exists node");
        jdbcTemplate.execute("drop table if exists user");
    }

    void createFlow() {

        Map<String, String> request = new HashMap<>();
        request.put("core_meta_id", "1");
        request.put("extra_meta_id", "2");
        request.put("graph_data", "some graph data");
        request.put("blackboard", "some blackboard data");
        request.put("name", "Test Flow");
        request.put("des", "A test flow");

        Map<String, Object> result = flowController.createFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void startNodeTest(){
        createFlow();
        Map<String,Object> request = new HashMap<>();
        request.put("uuid","start_node_tester");
        request.put("flow_record_id",1000);
        request.put("node_id", 2);
        request.put("start_time", "2021-10-01 12:00:00");
        request.put("complete", false);
        request.put("has_error", false);
        request.put("option", "test_option");

        Map<String,Object> response = instanceController.instanceStartNode(request);
        assertEquals(20000,response.get("code"));
        assertNotNull(response.get("data"));

    }
    @Test
    void endNodeTest(){
        createFlow();
        startNodeTest();
        Map<String,Object> request = new HashMap<>();
        request.put("uuid","start_node_tester");
        request.put("flow_record_id",1000);
        request.put("node_id", 2);
        request.put("end_time", "2021-10-01 12:00:00");
        request.put("complete", true);
        request.put("has_error", false);
        request.put("option", "test_option");

        Map<String,Object> response = instanceController.instanceEndNode(request);
        assertEquals(20000, response.get("code"));
        assertNotNull(response.get("data"));
    }

    @Test
    void pullInstanceTest(){
        endNodeTest();
        Map<String,Object> request = new HashMap<>();
        request.put("uuid", "start_node_tester");
        Map<String,Object> response = instanceController.pullInstance(request);
        assertEquals(20000, response.get("code"));
        assertNotNull(response.get("data"));
        assertEquals("start_node_tester", response.get("uuid"));
    }

    @Test
    void getInstanceListTest(){
        endNodeTest();
        Map<String,Object> request = new HashMap<>();
        request.put("flow_record_id", 1000);
        Map<String,Object> response = instanceController.getInstanceList(request);
        assertEquals(20000, response.get("code"));
        assertNotNull(response.get("data"));
    }

}
