package com.chuang.shi.yun.chuangservice.util;



import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;


public class BuildUrlUtils {
    /**
     * 请求时间格式
     */
    private static String ISO8601_DATE_FORMAT="yyyy-MM-dd-'T'HH:mm:ss'Z'";
    private static final String HMAC_SHA1_ALGORITHM="HmacSHA1";
    /**
     * 字符编码
     */
    private  static String ENCODE_TYPE="utf-8";
    /**
     * 分隔符
     */
    private static String SEPARATOR="&";
    private static String EQUAL="=";

    public static Map<String,String> getAllParams(Map<String,String> publicParams,Map<String,String> privateParams){
        Map<String,String>  allParams=new HashMap<>();
        allParams.putAll(publicParams);
        allParams.putAll(privateParams);
        return  allParams;

    }
    public static String buildUrl(String domain,Map<String,String> allParams,String signatureStr){
        StringBuilder stb = new StringBuilder();
        if(StringUtils.startsWith(domain,"http://")){
            stb.append(domain);
        }else {
            stb.append("http://").append(domain);
        }
        stb.append("?");
        stb.append(getUrlEncode("Signature")).append("=").append(getUrlEncode(signatureStr));
        for(Map.Entry<String,String> e :allParams.entrySet()){
            stb.append("&").append(getUrlEncode(e.getKey())).append("=").append(getUrlEncode(e.getValue()));
        }
        return stb.toString();
    }

    private static String getUrlEncode(String value) {
        try {
            String urlEnCodeOrignStr = URLEncoder.encode(value, ENCODE_TYPE);
            String piusReplaced = urlEnCodeOrignStr.replace("+", "%20");
            String startReplaced = piusReplaced.replace("*", "%2A");
            String waveReplaced = startReplaced.replace("%7E", "~");
            return waveReplaced;
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static String signature(String method,Map<String,String> parameterMap,String secret){
        List<String> keys=new ArrayList<>(parameterMap.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String value = parameterMap.get(key);
            sb.append(SEPARATOR).append(getUrlEncode(key)).append(EQUAL).append(getUrlEncode(value));
        }
       StringBuilder signedData = new StringBuilder();
        signedData.append(getUrlEncode(method));
        signedData.append(SEPARATOR);
        signedData.append(getUrlEncode("/"));
        signedData.append(SEPARATOR);
        signedData.append(getUrlEncode(sb.substring(1)));
        String signatureString="";
        try {
            signatureString=getSignatureBase64(hmacSHA1Signature(secret,signedData.toString()));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }
        return signatureString;
    }

    private static String getSignatureBase64(byte[] bytes) throws UnsupportedEncodingException{
        if(bytes==null || bytes.length==0){
            return null;
        }
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes,false),ENCODE_TYPE);
    }

    private static byte[] hmacSHA1Signature(String secret, String baseString) throws Exception{
        if(StringUtils.isAllEmpty(secret)){
            throw new IOException("secret can not be empty");
        }
        if(StringUtils.isEmpty(baseString)){
            return null;
        }
        String key=secret+"&";
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(ENCODE_TYPE), ALGORITHM);
        mac.init(secretKeySpec);
        return mac.doFinal(baseString.getBytes(ENCODE_TYPE));
    }

    /**
     * 获取utc格式当前时间
     */
    public static String generateTimestamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0,"UTC"));
        return simpleDateFormat.format(new Date());
    }

    public static String generateRandom(){
        String s = UUID.randomUUID().toString();
        return s;
    }
}
