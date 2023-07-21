//package com.zulong.web;
//
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.pool.DruidDataSourceStatValue;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@SpringBootTest
//public class DruidTest {
//
//    @Autowired
//    private DruidDataSource dataSource;
//
//    @Test
//    public void testDruidDataSource() throws SQLException {
//        // 检查初始化连接数是否为1
//        assertEquals(1, dataSource.getInitialSize());
//
//        Connection connection = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            connection = dataSource.getConnection();
//            String sql = "SELECT * FROM users";
//            pstmt = connection.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                long id = rs.getLong("id");
//                String name = rs.getString("name");
//                System.out.println("id: " + id + ", name: " + name);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } finally {
//            // 释放资源
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//                if (connection != null) connection.close();
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        }
//
//        // 检查连接池状态，确保连接池活跃连接数是1
//        DruidDataSourceStatValue statValue = dataSource.getStatValueAndReset();
//        assertEquals(1, statValue.getActiveCount());
//    }
//}