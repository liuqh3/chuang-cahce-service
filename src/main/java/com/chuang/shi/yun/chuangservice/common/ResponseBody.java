package com.chuang.shi.yun.chuangservice.common;

import java.util.List;

public class ResponseBody {
    private List<String> list;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseBody() {
    }

    public ResponseBody(List<String> list,String name) {
        this.list = list;
        this.name=name;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
