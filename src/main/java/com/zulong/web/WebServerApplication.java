package com.zulong.web;

import com.zulong.web.log.LoggerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class WebServerApplication {

	public static void main(String[] args)
	{
		// 日志初始化
		LoggerManager.init();

		SpringApplication.run(WebServerApplication.class, args);

		LoggerManager.logger().info("[com.zulong.web]WebServerApplication.main@WebServer Application started");
	}
}
