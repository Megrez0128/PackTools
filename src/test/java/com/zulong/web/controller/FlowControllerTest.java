package com.zulong.web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;

@SpringBootTest
public class FlowControllerTest {
    @Autowired
    private FlowController flowController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        // 初始化数据库
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS test_user(user_id varchar(20) primary key, is_admin boolean)");
        jdbcTemplate.execute("insert into test_user(user_id,is_admin)values('admin',true)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS core_meta (meta_id int, version int, data varchar(255))");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // 删除数据库
        jdbcTemplate.execute("drop table test_user");
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

}
