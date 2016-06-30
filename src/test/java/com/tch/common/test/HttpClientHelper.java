package com.tch.common.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpClientHelper {
    public static final Logger logger = LoggerFactory
            .getLogger(HttpClientHelper.class);
 
    public static void main(String[] args) throws Exception {
    	Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
		//System.out.println(simpleHttpGet("http://www.baidu.com"));
        //System.out.println(simpleHttpPost("http1://www.cnblogs.com", null));
    	System.out.println(doPostInJson("http://192.168.43.142:9000/release/customerRelease", params ));
    	/*
    	Map<String, String> params = new HashMap<>();
        params.put("account", "18521736087");
        params.put("password", "123456");
    	System.out.println(doPostInJson("http://localhost:9000/user/login", params ));*/
	}
    
    /**
     * @description 发送Http请求
     * @param request
     * @return
     * @throws Exception 
     */
    private static String sendRequest(HttpUriRequest httpRequst) throws Exception {
    	String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpRequst);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
            } else {
            	HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
                throw new ClientProtocolException("Unexpected response status: " + status + ", result: " + result);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
 
    /**
     * @description 向指定的URL发起一个put请求
     * @param uri
     * @param values
     * @return
     * @throws IOException
     */
    public static String doPut(String url,Map<String, String> params)
            throws Exception {
        HttpPut request = new HttpPut(url);
        if (params != null) {
            request.setEntity(new UrlEncodedFormEntity(convert2NameValulePaires(params), "UTF-8"));
        }
        return sendRequest(request);
    }
    
    public static String doPutInJson(String url,Map<String, String> params)
            throws Exception {
        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");
        if (params != null && !params.isEmpty()) {
        	request.setEntity(new StringEntity(JSON.toJSONString(params)));
        }
        return sendRequest(request);
    }
 
    /**
     * @description 向指定的URL发起一个GET请求并以String类型返回数据，获取数据总线数据
     * @param url
     * @return
     * @throws Exception 
     */
    public static String doGet(String url) throws Exception {
        HttpGet request = new HttpGet(url);
        return sendRequest(request);
    }
 
    /**
     * @description 向指定的URL发起一个post请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params) throws Exception {
        HttpPost request = new HttpPost(url);
        if (params != null && !params.isEmpty()) {
        	request.setEntity(new UrlEncodedFormEntity(convert2NameValulePaires(params), "UTF-8"));
        }
        return sendRequest(request);
    }
    
    public static String doPostInJson(String url, Map<String, String> params) throws Exception {
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");
        if (params != null && !params.isEmpty()) {
        	request.setEntity(new StringEntity(JSON.toJSONString(params)));
        }
        System.out.println(JSON.toJSONString(params));
        System.out.println(request);
        return sendRequest(request);
    }
    
    public static String doDelete(String url) throws Exception {
        HttpDelete request = new HttpDelete(url);
        return sendRequest(request);
    }
    
    public static List<NameValuePair> convert2NameValulePaires(Map<String, String> params){
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nameValuePairs;
    }

}