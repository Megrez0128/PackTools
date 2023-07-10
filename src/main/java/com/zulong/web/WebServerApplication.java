package com.zulong.web;

import com.zulong.web.log.LoggerManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
// 专门给Mybatis添加的注解，或许会用到，不影响运行正确性（已经在pom中引入依赖）
@MapperScan({"com.zulong.web.dao"})
public class WebServerApplication {

	public static void main(String[] args)
	{
		// 日志初始化
		LoggerManager.init();

		SpringApplication.run(WebServerApplication.class, args);

		LoggerManager.logger().info("[com.zulong.web]WebServerApplication.main@WebServer Application started");
	}
}
