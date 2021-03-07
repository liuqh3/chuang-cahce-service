package com.chuang.shi.yun.chuangservice.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class DescribeRefreshTaskObject {
    @JSONField(name = "tasks")
    private TaskObject tasks;
    @JSONField(name = "PageSize")
    private Long PageSize;
    @JSONField(name = "PageNumber")
    private Long PageNumber;
    @JSONField(name = "TotalCount")
    private Long TotalCount;
    @JSONField(name = "RequestId")
    private String RequestId;

    public TaskObject getTasks() {
        return tasks;
    }

    public void setTasks(TaskObject tasks) {
        this.tasks = tasks;
    }

    public Long getPageSize() {
        return PageSize;
    }

    public void setPageSize(Long pageSize) {
        PageSize = pageSize;
    }

    public Long getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(Long pageNumber) {
        PageNumber = pageNumber;
    }

    public Long getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(Long totalCount) {
        TotalCount = totalCount;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }
}
