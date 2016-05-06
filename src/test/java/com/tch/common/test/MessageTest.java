package com.tch.common.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MessageTest {

	@Test
	public void testGetMessage() throws Exception{
		String messageStr = HttpClientHelper.doGet("http://localhost:9000/message/query");
		System.out.println(messageStr);
	}
	
	@Test
	public void testAddMessage() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("content", "tianchaohui");
		String messageStr = HttpClientHelper.doPost("http://localhost:9000/message/add", params);
		System.out.println(messageStr);
	}
	
	@Test
	public void testDeleteMessage() throws Exception{
		String messageStr = HttpClientHelper.doDelete("http://localhost:9000/message/delete/1");
		System.out.println(messageStr);
	}
	
	@Test
	public void testUpdateMessage() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("content", "chaohui-1");
		String messageStr = HttpClientHelper.doPut("http://localhost:9000/message/update/1", params);
		System.out.println(messageStr);
	}
	
}
