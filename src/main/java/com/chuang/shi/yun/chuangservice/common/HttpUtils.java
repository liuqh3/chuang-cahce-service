package com.chuang.shi.yun.chuangservice.common;


import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtils {
    private static final Logger logger= LoggerFactory.getLogger(HttpUtils.class);
    public static String execute(HttpUriRequest request){
        CloseableHttpClient httpClient=null;
        CloseableHttpResponse response=null;
        String responseContent;
        try {
            //创建默认httpclient实例
            httpClient= HttpClients.createDefault();
            response= httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            responseContent =EntityUtils.toString(entity,"UTF-8");
            logger.info("Response Content{}",responseContent);
            if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                System.out.println("responseContent = " + responseContent);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }finally {
            try {
                if(response!=null){
                    response.close();
                }
            }catch (IOException e){
                logger.warn(e.getMessage());
            }
            try{
                if(httpClient!=null){
                    httpClient.close();
                }
            }catch (IOException e){
                logger.warn(e.getMessage());
            }

        }
        return  responseContent;
    }

    /**
     * 发送get请求
     */
    public static String sendHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return execute(httpGet);
    }
    /**
     * 发送post请求
     */
    public static String sendHttpPost(String url, Map<String,String> params)throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        //设置参数
        if(null!=params){
            List<NameValuePair> list=new ArrayList<>();
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            UrlEncodedFormEntity encodedFormEntity=new UrlEncodedFormEntity(list,"UTF-8");
            httpPost.setEntity(encodedFormEntity);
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        return execute(httpPost);

    }
}
