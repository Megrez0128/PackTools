package com.zulong.web;

import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 新版本junit好像@Before @After都不生效了，换成Each
    @BeforeEach
    public void setUp() throws Exception {
        // 初始化数据库
//        jdbcTemplate.execute("create database test_user");
//        jdbcTemplate.execute("use test_user");
//        jdbcTemplate.execute("drop table test_user");
        jdbcTemplate.execute("create table test_user(user_id varchar(20) primary key, token varchar(50), administrator boolean)");
        jdbcTemplate.execute("insert into test_user(user_id,token,administrator)values('admin','123456',true)");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // 删除数据库
        jdbcTemplate.execute("drop table test_user");
    }

    @Test
    public void testFindAll() {
//        jdbcTemplate.execute("create table test_user(user_id varchar(20) primary key, token varchar(50), administrator boolean)");
//        jdbcTemplate.execute("insert into test_user(user_id,token,administrator)values('admin','123456',true)");
        List<User> userList = userDao.findAll();
        assertEquals(1, userList.size());
        assertEquals("admin", userList.get(0).getUser_id());
        assertEquals("123456", userList.get(0).getToken());
        assertTrue(userList.get(0).isIs_admin());
//        jdbcTemplate.execute("drop table test_user");
    }

    @Test
    public void testFindByUserID() {
//        jdbcTemplate.execute("create table test_user(user_id varchar(20) primary key, token varchar(50), administrator boolean)");
//        jdbcTemplate.execute("insert into test_user(user_id,token,administrator)values('admin','123456',true)");
        User user = userDao.findByUserID("admin");
        assertNotNull(user);
        assertEquals("admin", user.getUser_id());
        assertEquals("123456", user.getToken());
        assertTrue(user.isIs_admin());
//        jdbcTemplate.execute("drop table test_user");
    }

    @Test
    public void testAddUser() {
//        jdbcTemplate.execute("create table test_user(user_id varchar(20) primary key, token varchar(50), administrator boolean)");
//        jdbcTemplate.execute("insert into test_user(user_id,token,administrator)values('admin','123456',true)");
        User user = new User();
        user.setUser_id("test");
        user.setToken("123");
        user.setIs_admin(false);
        boolean result = userDao.insertUser(user);
        assertTrue(result);

        User newUser = userDao.findByUserID("test");
        assertNotNull(newUser);
        assertEquals("test", newUser.getUser_id());
        assertEquals("123", newUser.getToken());
        assertFalse(newUser.isIs_admin());
//        jdbcTemplate.execute("drop table test_user");
    }

    @Test
    public void testDeleteByUserID() {
//        jdbcTemplate.execute("create table test_user(user_id varchar(20) primary key, token varchar(50), administrator boolean)");
//        jdbcTemplate.execute("insert into test_user(user_id,token,administrator)values('admin','123456',true)");
        boolean result = userDao.deleteByUserID("admin");
        assertTrue(result);

        User user = userDao.findByUserID("admin");
        assertNull(user);
//        jdbcTemplate.execute("drop table test_user");
    }
}