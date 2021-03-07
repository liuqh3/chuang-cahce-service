package com.chuang.shi.yun.chuangservice.dto;

import com.sun.prism.impl.Disposer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"record"})
@XmlRootElement(name="records")
public class Records {

    private List<Record> record;
    // setter  getter 方法

    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }

    //toString方法
    @Override
    public String toString() {
        return "Records [record=" + record + "]";
    }
}

