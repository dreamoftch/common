package com.tch.test.common.httpcomponent.httpclient.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpUtils.class);
    
    //参考：http://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
    
    public final static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
		//System.out.println(simpleHttpGet("http://www.baidu.com"));
        //System.out.println(simpleHttpPost("http1://www.cnblogs.com", null));
    	System.out.println(simpleHttpPost("http://192.168.43.142:9000/release/customerRelease", params ));
    }

    public static String simpleHttpGet(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager. 
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            return EntityUtils.toString(entity);
        } catch(Exception e){
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
            response.close();
        }
    }

    public static String simpleHttpPost(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            if (params != null && !params.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(HttpUtils.convert2NameValulePaires(params),
                        "UTF-8"));
            }
            response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            return EntityUtils.toString(entity);
        } catch(Exception e){
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
