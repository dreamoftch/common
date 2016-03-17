
package com.tch.test.common.httpcomponent.httpclient.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    //参考：http://hc.apache.org/httpcomponents-client-4.5.x/examples.html
    
    public final static void main(String[] args) throws Exception {
        System.out.println(doGet("http://www.baidu.com"));
        System.out.println(doPost("http://www.cnblogs.com", null));
    }

    public static String doGet(String url) {
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        HttpGet httpRequst = new HttpGet(url);
        try {
            httpResponse = httpclient.execute(httpRequst);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static String doPost(String url, Map<String, String> params) {
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpRequst = new HttpPost(url);
            if (params != null && !params.isEmpty()) {
                httpRequst.setEntity(new UrlEncodedFormEntity(convert2NameValulePaires(params), "UTF-8"));
            }
            httpResponse = httpclient.execute(httpRequst);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
