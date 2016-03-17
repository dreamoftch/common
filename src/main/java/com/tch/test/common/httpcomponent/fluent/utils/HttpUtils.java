package com.tch.test.common.httpcomponent.fluent.utils;

import java.util.Map;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class HttpUtils {

    //参考：http://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
    
    public final static void main(String[] args) throws Exception {
        System.out.println(httpGet("http://www.baidu.com"));
        System.out.println(httpPost("http://www.cnblogs.com", null));
        System.out.println(httpPost2("http://www.cnblogs.com", null));
    }
    
    public static String httpGet(String url) throws Exception {
        return Request.Get(url).execute().returnContent().asString();
    }

    public static String httpPost(String url, Map<String, String> params) throws Exception {
        Request request = Request.Post(url);
        if (params != null && !params.isEmpty()) {
            Form form = Form.form();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form.add(entry.getKey(), entry.getValue());
            }
            request.bodyForm(form.build());
        }
        return request.execute().returnContent().asString();
    }
    
    public static String httpPost2(String url, Map<String, String> params) throws Exception {
        Request request = Request.Post(url);
        if (params != null && !params.isEmpty()) {
            request.bodyForm(com.tch.test.common.httpcomponent.httpclient.utils.HttpUtils.convert2NameValulePaires(params));
        }
        return request.execute().returnContent().asString();
    }

}
