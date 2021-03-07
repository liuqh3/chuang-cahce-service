package com.chuang.shi.yun.chuangservice.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class CDNTaskObject {
    @JSONField(name="CreationTime")
    private String CreationTime;
    @JSONField(name="ObjectPath")
    private String ObjectPath;
    @JSONField(name="Status")
    private String Status;
    @JSONField(name="TasksId")
    private String TasksId;
    @JSONField(name="ObjectType")
    private String ObjectType;
    @JSONField(name="Process")
    private String Process;

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public String getObjectPath() {
        return ObjectPath;
    }

    public void setObjectPath(String objectPath) {
        ObjectPath = objectPath;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTasksId() {
        return TasksId;
    }

    public void setTasksId(String TasksId) {
        TasksId = TasksId;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getProcess() {
        return Process;
    }

    public void setProcess(String process) {
        Process = process;
    }
}
