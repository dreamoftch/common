package com.tch.test.common.httpcomponent.httpclient.utils;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SimpleHttpUtils {

    //参考：http://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
    
    public final static void main(String[] args) throws Exception {
        System.out.println(simpleHttpGet("http://www.baidu.com"));
        System.out.println(simpleHttpPost("http://www.cnblogs.com", null));
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
        } finally {
            response.close();
        }
    }

    public static String simpleHttpPost(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (params != null && !params.isEmpty()) {
            httpPost.setEntity(new UrlEncodedFormEntity(HttpUtils.convert2NameValulePaires(params),
                "UTF-8"));
        }
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            return EntityUtils.toString(entity);
        } finally {
            response.close();
        }
    }

}
