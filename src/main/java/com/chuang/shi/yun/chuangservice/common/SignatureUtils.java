package com.chuang.shi.yun.chuangservice.common;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class SignatureUtils {

    private final static String CHARSET_UTF8 = "utf8";
    private final static String ALGORITHM = "UTF-8";
    private final static String SEPARATOR = "&";

    public static Map<String, String> splitQueryString(String url)
            throws URISyntaxException, UnsupportedEncodingException {
        URI uri = new URI(url);
        String query = uri.getQuery();
        String[] pairs = query.split("&");
        TreeMap<String, String> queryMap = new TreeMap<>();
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? pair.substring(0, idx) : pair;
            if (!queryMap.containsKey(key)) {
                queryMap.put(key, URLDecoder.decode(pair.substring(idx + 1), CHARSET_UTF8));
            }
        }

        return queryMap;
    }

    public static String generate(String method, Map<String, String> parameter, String accessKeySecret)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String signString = generateSignString(method, parameter);
        System.out.println("signString = " + signString);
        byte[] signBytes = hmacSHASignature(accessKeySecret + "&", signString);
        String signature = newStringByBase64(signBytes);
        System.out.println("signature = " + signature);
        if ("POST".equals(method)) {
            return signature;
        }
        return URLEncoder.encode(signature, "UTF-8");
    }

    private static String newStringByBase64(byte[] signBytes)
            throws UnsupportedEncodingException {

        if (signBytes == null || signBytes.length == 0) {
            return null;
        }
        return new String(Base64.encodeBase64(signBytes, false), CHARSET_UTF8);
    }

    private static byte[] hmacSHASignature(String secret, String signString)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (StringUtils.isEmpty(secret)) {
            throw new IOException("secret can not be empty");
        }
        if (StringUtils.isEmpty(signString)) {
            return null;
        }
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), ALGORITHM);
        mac.init(keySpec);
        return mac.doFinal(signString.getBytes(CHARSET_UTF8));
    }

    public static String getQueryParams(Map<String, String> parameter) throws UnsupportedEncodingException {
        TreeMap<String, String> sortParameter = new TreeMap<>();
        sortParameter.putAll(parameter);
        String generateQueryString = generateQueryString(sortParameter, false);
        System.out.println("generateQueryString = " + generateQueryString);
        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append(percentEncode(generateQueryString));
        stringBuilder.append(generateQueryString);

        return stringBuilder.toString();
    }

    private static String generateSignString(String method, Map<String, String> parameter) throws UnsupportedEncodingException {

        TreeMap<String, String> sortParameter = new TreeMap<>();
        sortParameter.putAll(parameter);
        String canonlizedQuery = generateQueryString(sortParameter, true);
        if (null == method) {
            throw new RuntimeException("http Method can not be empty ");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method).append(SEPARATOR);
        stringBuilder.append(percentEncode("/")).append(SEPARATOR);
        stringBuilder.append(percentEncode(canonlizedQuery));
        return stringBuilder.toString();
    }

    private static String generateQueryString(TreeMap<String, String> sortParameter, boolean isEncodeKV)
            throws UnsupportedEncodingException {
        StringBuilder canonLizedQueryString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortParameter.entrySet()) {
            if (isEncodeKV) {
                canonLizedQueryString.append(percentEncode(entry.getKey())).append("=")
                        .append(percentEncode(entry.getValue())).append("&");
            } else {
                canonLizedQueryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        if (canonLizedQueryString.length() > 1) {
            canonLizedQueryString.setLength(canonLizedQueryString.length() - 1);
        }
        return canonLizedQueryString.toString();
    }

    public static String percentEncode(String value) {
        try {
            return value == null ? null : URLEncoder.encode(value, CHARSET_UTF8).replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {

        }
        return "";
    }

    public static void main(String[] args) {
        String app_key = "testid";
        String Format = "JSON";
        String Version = "2018-05-10";
        String SignatureVersion = "1.0";
        String Action = "DescribeCdnService";

        Map<String, String> param = new HashMap<>();
        param.put("AccessKeyId", app_key);
        param.put("Format", Format);
        param.put("Version", Version);
        param.put("SignatureVersion", SignatureVersion);
        param.put("SignatureNonce", "9b7a44b-3bel-11e5-8c73-08002700c460");
        param.put("Timestamp", "2015-08-06T02:19:46Z");
        param.put("Action", Action);
        try {
            String url = generate("GET", param, "testsecret");
            System.out.println("url = " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}