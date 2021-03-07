package com.chuang.shi.yun.chuangservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class BuildCdnUrlUtils extends BuildUrlUtils {
    private static final Logger logger= LoggerFactory.getLogger(BuildCdnUrlUtils.class);
    private static final String accessKeyId="LTAI45fkELKwljLczVq8RnR";
    private static final String accessKeySecret="JJH3tlYoEnGUa5N8LPZTv7sNgIUVH";
    private static final String domain="cdn.aliyuncs.com";

    private static final String liveAccessKeySecret="";
    private static final String liveAccessKeyId="";
    private static final String liveDomain="";

    public static String getFinalUrl(String method, Map<String ,String> publicParams,Map<String,String> privateParams){
        Map<String, String> allParams = getAllParams(publicParams, privateParams);
        //生成签名
        String signature = signature(method, allParams, accessKeySecret);
        String finalUrl = buildUrl(domain, allParams, signature);
        return finalUrl;
    }
    public static String getLiveFinalUrl(String method, Map<String ,String> publicParams,Map<String,String> privateParams){
        Map<String, String> allParams = getAllParams(publicParams, privateParams);
        //生成签名
        String signature = signature(method, allParams, liveAccessKeySecret);
        String finalUrl = buildUrl(domain, allParams, signature);
        return finalUrl;
    }
    public static Map<String,String> buildPublicParameters(){
       return buildParameter(accessKeyId);
    }

    public static Map<String,String> buildLivePublicParameters(){
        return buildParameter(liveAccessKeyId);
    }

    public static Map<String,String> buildParameter(String key){
        Map<String,String> publicParams=new HashMap<>(7,1.0F);
        publicParams.put("Format","JSON");
        publicParams.put("AccessKeyId",key);
        publicParams.put("SignatureMethod","HMAC-SHA1");
        publicParams.put("Timestamp",generateTimestamp());
        publicParams.put("SignatureVersion","1.0");
        publicParams.put("SignatureNonce",generateRandom());
        return publicParams;
    }

    public static Map<String,String> build20180510PublicParameters(){
        return buildDatePublicParameters("2018-05-10");
    }

    public static Map<String,String> build20161101PublicParameters() {
        return buildDatePublicParameters("2016-11-01");
    }
    public static Map<String,String> build20141111PublicParameters() {
        return buildDatePublicParameters("2014-11-11");
    }

    public static Map<String,String> buildDatePublicParameters(String date){
        Map<String, String> publicParams = buildPublicParameters();
        publicParams.put("Version", date);
        return publicParams;
    }
}
