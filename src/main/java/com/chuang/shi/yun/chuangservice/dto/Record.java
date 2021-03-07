package com.chuang.shi.yun.chuangservice.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="record")
public class Record {

    @XmlElement(name = "mission_num")
    private String missionNum;
    @XmlElement(name = "dest_id")
    private String destId;

    @XmlElement(name = "send_status")
    private String sendStatus;
    @XmlElement(name = "receive_status")
    private String receiveStatus;

    @XmlElement(name = "batch_num")
    private String batchNum;
    @XmlElement(name = "stat_time")
    private String statTime;
    // setter  getter 方法

    //toString方法
    @Override
    public String toString() {
        return "Record [missionNum=" + missionNum + ", destId=" + destId + ", sendStatus=" + sendStatus
                + ", receiveStatus=" + receiveStatus + ", batchNum=" + batchNum + ", statTime=" + statTime + "]";
    }
}

