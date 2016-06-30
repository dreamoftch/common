package com.tch.common.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class T1 {

	public static void main(String[] args) {
		Map<String, String> map = new LinkedHashMap<String, String>();
//		Map<String, String> map = new HashMap<String, String>();
		map.put("zhangsan1", "zhangsan");
		map.put("zhangsan2", "zhangsan");
		map.put("zhangsan3", "zhangsan");
		map.put("zhangsan4", "zhangsan");
		map.put("zhangsan5", "zhangsan");
		for(String key : map.keySet()){
			System.out.println("key:" + key + ", value:" + map.get(key));
		}
	}
	
}
