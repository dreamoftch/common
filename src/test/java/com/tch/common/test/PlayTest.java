package com.tch.common.test;

import org.junit.Test;

import com.tch.test.common.httpcomponent.httpclient.utils.HttpUtils;

public class PlayTest {

	@Test
	public void testGetMessage() throws Exception{
		String messageStr = HttpUtils.doGet("http://localhost:9000/message/query");
		System.out.println(messageStr);
	}
	
	@Test
	public void testAddMessage() throws Exception{
		String messageStr = HttpUtils.doPost("http://localhost:9000/message/add?content=tianchaohui", null);
		System.out.println(messageStr);
	}
	
	@Test
	public void testDeleteMessage() throws Exception{
		String messageStr = HttpClientHelper.doDelete("http://localhost:9000/message/delete?id=1");
		System.out.println(messageStr);
	}
	
	@Test
	public void testUpdateMessage() throws Exception{
		String messageStr = HttpClientHelper.doPut("http://localhost:9000/message/update?id=2&content=chaohui-1", null);
		System.out.println(messageStr);
	}
	
}
