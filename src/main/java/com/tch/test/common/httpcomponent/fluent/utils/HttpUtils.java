package com.tch.test.common.httpcomponent.fluent.utils;

import java.util.Map;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    //参考：http://hc.apache.org/httpcomponents-client-4.5.x/quickstart.html
    
    public final static void main(String[] args) throws Exception {
        System.out.println(httpGet("http://www.baidu.com"));
        System.out.println(httpPost("http://www.cnblogs.com", null));
        System.out.println(httpPost2("http://www.cnblogs.com", null));
    }
    
    public static String httpGet(String url) throws Exception {
        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    public static String httpPost(String url, Map<String, String> params) throws Exception {
        try {
            Request request = Request.Post(url);
            if (params != null && !params.isEmpty()) {
                Form form = Form.form();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    form.add(entry.getKey(), entry.getValue());
                }
                request.bodyForm(form.build());
            }
            return request.execute().returnContent().asString();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }
    
    public static String httpPost2(String url, Map<String, String> params) throws Exception {
        try {
            Request request = Request.Post(url);
            if (params != null && !params.isEmpty()) {
                request.bodyForm(com.tch.test.common.httpcomponent.httpclient.utils.HttpUtils.convert2NameValulePaires(params));
            }
            return request.execute().returnContent().asString();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

}
