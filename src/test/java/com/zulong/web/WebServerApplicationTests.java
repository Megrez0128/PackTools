package com.zulong.web;//package com.zulong.web;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zulong.web.controller.FlowController;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class WebServerApplicationTests {
//
//    @Autowired
//    private FlowController packageAnalysisController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void createFlowchartTest() throws Exception {
//        // 创建一个请求体
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("name", "test");
//        requestBody.put("des", "test");
//
//        // 发送POST请求并验证响应
//        mockMvc.perform(MockMvcRequestBuilders.post("/myflow/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestBody)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(20000))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("test"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.des").value("test"));
//    }
//
//}


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.zulong.web.log.LoggerManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WebServerApplicationTests{

	@Test
	public void query() throws Exception{
			// 向localhost:8080/demo/userinfo?username=xxx发送请求
		RestTemplate restTemplate = new RestTemplate(); // 创建 RestTemplate 实例
		String url = "http://localhost:1000/myflow/create"; // 发送请求的 URL
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "myFlowchart");
		requestBody.put("des", "Flowchart description");
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);
		Map response = responseEntity.getBody();
		//assertEquals(20000, response.get("code"));
		Map data = (Map) response.get("data");
		assertEquals(20000, data.get("code"));
		Map data2 = (Map) data.get("data");
		assertEquals("myFlowchart", data2.get("name"));
		assertEquals("Flowchart description", data2.get("des"));
		assertNotNull(data2.get("id"));
	}

	@Test
	void demoTest() {
		// 向localhost:8080/demo/userinfo?username=xxx发送请求
		RestTemplate restTemplate = new RestTemplate(); // 创建 RestTemplate 实例
		String url = "http://localhost:1000/myflow/userinfo?username=xxx"; // 发送请求的 URL
		String response = restTemplate.getForObject(url, String.class); // 发送 GET 请求并获取响应结果
		LoggerManager.init();
		LoggerManager.logger().info(response);
		System.out.println(response);
	}

}