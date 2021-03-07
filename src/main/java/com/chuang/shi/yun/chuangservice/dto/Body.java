package com.chuang.shi.yun.chuangservice.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"records"})	// 子节点
@XmlRootElement(name="body")   // 当前节点
public class Body {
    @XmlElement(name = "records")    // 子节点名称
    private Records records;

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }

    // setter  getter 方法
    // toString 方法
    @Override
    public String toString() {
        return "Body [records=" + records + "]";
    }
}

