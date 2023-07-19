package com.zulong.web.controller;

import com.alibaba.fastjson.JSONObject;

import com.zulong.web.entity.Flow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;
import java.io.FileReader;

import com.alibaba.fastjson.parser.Feature;


@SpringBootTest
public class FlowControllerTest {
    @Autowired
    private FlowController flowController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static HashMap<String, String> readData(String filename) {
        try {
            // 读取json文件并解析
            JSONObject json = JSONObject.parseObject(String.valueOf(new FileReader(filename)), Feature.OrderedField);

            // 将数据存入HashMap
            HashMap<String, String> hashMap = new HashMap<>();
            for (String key : json.keySet()) {
                hashMap.put(key, json.get(key).toString());
            }

            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


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
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS flow(record_id int primary key,flow_id int, version int, committed boolean, commit_message varchar(100),last_build varchar(100), core_meta_id int, extra_meta_id int, graph_data varchar(1000), blackboard varchar(1000))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS flow_summary(flow_id int primary key,name varchar(50), des varchar(200), last_build varchar(100), last_commit varchar(100), last_version int)");
//        jdbcTemplate.execute("insert into test_user(user_id,is_admin)values('admin',true)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS core_meta (meta_id int primary key, version int, data varchar(255))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS extra_meta (meta_id int primary key, version int, group_id int, data varchar(255))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS pack_group (group_id int primary key, group_name varchar(25))");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS instance (uuid varchar(25) primary key, flow_record_id int, build_time varchar(25), complete boolean, has_error boolean)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS node (instance_id varchar(20), node_id int primary key, start_time varchar(25), end_time varchar(25), options varchar(10000)) ");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS user (user_id varchar(20) primary key, admin boolean)");
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

    @Test
    void createFlowTest() {

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
    void cloneFlowTest() {
        createFlowTest();
        createFlowTest();
        createFlowTest();
        createFlowTest();
        createFlowTest();

        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        //获取result.data.value[0]的record_id,然后clone
        Map<String, Object> data = (Map<String, Object>)result.get("data");
        ArrayList items = (ArrayList) data.get("items");
        Flow flow = (Flow) items.get(0);
        int record_id = flow.getRecord_id();
        String name = "test_clone_name";
        String des = "test_clone_des";
        Map<String, Object> request = new HashMap<>();
        request.put("record_id", record_id);
        request.put("name",name);
        request.put("des",des);
        result = flowController.cloneFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void listFlowTest() {
        createFlowTest();
        createFlowTest();
        createFlowTest();
        createFlowTest();
        createFlowTest();
        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void commitFlowTest(){
        createFlowTest();
        createFlowTest();
        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        //获取result.data.value[0]的record_id,然后clone
        Map<String, Object> data = (Map<String, Object>)result.get("data");
        ArrayList items = (ArrayList) data.get("items");
        Flow flow = (Flow) items.get(0);
        int record_id = flow.getRecord_id();
        String commit_message = "test_commit_commit_message";
        Map<String, Object> request = new HashMap<>();
        request.put("record_id", record_id);
        request.put("commit_message",commit_message);
        result = flowController.commitFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void saveFlowTest(){
        createFlowTest();
        createFlowTest();
        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        //获取result.data.value[0]的record_id,然后clone
        Map<String, Object> data = (Map<String, Object>)result.get("data");
        ArrayList items = (ArrayList) data.get("items");
        Flow flow = (Flow) items.get(0);
        int record_id = flow.getRecord_id();
        String commit_message = "test_commit_commit_message";
        Map<String, Object> request = new HashMap<>();
        request.put("flow_id", flow.getFlow_id());
        request.put("is_committed",flow.isCommitted());
        request.put("commit_message",commit_message);
        request.put("core_meta_id", 1);
        request.put("extra_meta_id", 2);
        request.put("graph_data", "saveFlowTest graph data");
        request.put("blackboard", "saveFlowTest blackboard data");
        request.put("name", "saveFlowTest name");
        request.put("des", "saveFlowTest test des");
        result = flowController.saveFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void historyFlowTest(){
        createFlowTest();
        createFlowTest();
        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        //获取result.data.value[0]的record_id,然后clone
        Map<String, Object> data = (Map<String, Object>)result.get("data");
        ArrayList items = (ArrayList) data.get("items");
        Flow flow = (Flow) items.get(0);
        int record_id = flow.getRecord_id();
        String commit_message = "test_commit_commit_message";
        Map<String, Object> request = new HashMap<>();
        request.put("flow_id", flow.getFlow_id());
        request.put("is_committed",flow.isCommitted());
        request.put("commit_message",commit_message);
        request.put("core_meta_id", 1);
        request.put("extra_meta_id", 2);
        request.put("graph_data", "saveFlowTest graph data");
        request.put("blackboard", "saveFlowTest blackboard data");
        request.put("name", "saveFlowTest name");
        request.put("des", "saveFlowTest test des");
        result = flowController.saveFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
        Map<String, Integer> request2 = new HashMap<>();
        request2.put("flow_id", flow.getFlow_id());
        result = flowController.getFlowHistoryList(request2);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }



    @Test
    void detailFlowTest() {
        createFlowTest();
        createFlowTest();
        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        //获取result.data.value[0]的record_id,然后clone
        Map<String, Object> data = (Map<String, Object>)result.get("data");
        ArrayList items = (ArrayList) data.get("items");
        Flow flow = (Flow) items.get(0);
        int record_id = flow.getRecord_id();
        String commit_message = "test_commit_commit_message";
        Map<String, Object> request = new HashMap<>();
        request.put("flow_id", flow.getFlow_id());
        request.put("is_committed",flow.isCommitted());
        request.put("commit_message",commit_message);
        request.put("core_meta_id", 1);
        request.put("extra_meta_id", 2);
        request.put("graph_data", "saveFlowTest graph data");
        request.put("blackboard", "saveFlowTest blackboard data");
        request.put("name", "saveFlowTest name");
        request.put("des", "saveFlowTest test des");
        result = flowController.saveFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
        Map<String, Integer> request2 = new HashMap<>();
        request2.put("flow_id", flow.getFlow_id());
        request2.put("version", flow.getVersion());
        result = flowController.getFlowDetails(request2);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
    }

    @Test
    void deleteFlowTest() {
        createFlowTest();
        createFlowTest();
        Map<String, Integer> request_group = new HashMap<>();
        request_group.put("group_id", 1);
        Map<String, Object> result = flowController.getFlowList(request_group);
        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));

        Map<String, Object> data = (Map<String, Object>)result.get("data");
        ArrayList items = (ArrayList) data.get("items");
        Flow flow = (Flow) items.get(0);
        int record_id = flow.getRecord_id();
        String commit_message = "test_commit_commit_message";
        Map<String, Object> request = new HashMap<>();
        request.put("flow_id", flow.getFlow_id());
        request.put("is_committed",flow.isCommitted());
        request.put("commit_message",commit_message);
        request.put("core_meta_id", 1);
        request.put("extra_meta_id", 2);
        request.put("graph_data", "saveFlowTest graph data");
        request.put("blackboard", "saveFlowTest blackboard data");
        request.put("name", "saveFlowTest name");
        request.put("des", "saveFlowTest test des");
        result = flowController.saveFlow(request);

        assertEquals(20000, result.get("code"));
        assertNotNull(result.get("data"));
        Map<String, Object> request2 = new HashMap<>();
        request2.put("record_id", record_id);
        result = flowController.deleteFlow(request2);
        assertEquals(20000, result.get("code"));
        assertEquals(true, result.get("message"));
    }

}
