//package com.zulong.web.task;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import java.io.File;
//import java.io.IOException;
//
//@Component
//public class BackupTask {
//
//    @Scheduled(cron = "0 0/1 * * * ?") // 每隔30分钟执行备份
//    public void backup() {
//        String username = "root";
//        String password = "root";
//        String database = "test_user";
//        String backupPath = "/backup"; // 备份文件保存路径
//        String backupFileName = System.currentTimeMillis() + ".sql"; // 备份文件名
//
//        try {
//            // 执行备份命令
//            ProcessBuilder pb = new ProcessBuilder(
//                    "wsl.exe", "/c",
//                    "mysqldump -u" + username + " -p" + password + " " + database + " > " + backupPath + File.separator + backupFileName);
//            pb.start();
//            System.out.println("Database backup success, file path: " + backupPath + File.separator + backupFileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}