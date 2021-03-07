package com.chuang.shi.yun.chuangservice.dto;

import java.io.Serializable;

public class AliBaBaOpenApiCommon implements Serializable {
    /**
     * 公共参数
     */
    private String ak;
    private String sk;
    private String action;
    private String format;
    private String version;
    private String signatureMethod;
    private String getSignatureVersion;

    /**
     * 非公共参数
     */
    private String args;
    private String args2;
    private String args_uri;
    private String args2_uri;

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public String getGetSignatureVersion() {
        return getSignatureVersion;
    }

    public void setGetSignatureVersion(String getSignatureVersion) {
        this.getSignatureVersion = getSignatureVersion;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getArgs2() {
        return args2;
    }

    public void setArgs2(String args2) {
        this.args2 = args2;
    }

    public String getArgs_uri() {
        return args_uri;
    }

    public void setArgs_uri(String args_uri) {
        this.args_uri = args_uri;
    }

    public String getArgs2_uri() {
        return args2_uri;
    }

    public void setArgs2_uri(String args2_uri) {
        this.args2_uri = args2_uri;
    }
}
