package com.chuang.shi.yun.chuangservice.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class TaskObject {
    @JSONField(name = "CDNTask")
    private List<CDNTaskObject> CDNTask;

    public List<CDNTaskObject> getCDNTask() {
        return CDNTask;
    }

    public void setCDNTask(List<CDNTaskObject> CDNTask) {
        this.CDNTask = CDNTask;
    }
}
