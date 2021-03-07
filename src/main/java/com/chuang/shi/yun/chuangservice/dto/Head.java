package com.chuang.shi.yun.chuangservice.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="head")    // 当前节点名称
public class Head {
    @XmlElement(name = "app_key")      // 子节点名称
    private String appKey;
    @XmlElement(name = "time_stamp")    // 子节点名称
    private String timeStamp;
    @XmlElement(name = "nonce_str")      // 子节点名称
    private String nonceStr;
    @XmlElement(name = "sign")     // 子节点名称
    private String sign;

// setter getter 方法

    // toString 方法
    @Override
    public String toString() {
        return "Head [appKey=" + appKey + ", timeStamp=" + timeStamp + ", nonceStr=" + nonceStr + ", sign=" + sign
                + "]";
    }
}
