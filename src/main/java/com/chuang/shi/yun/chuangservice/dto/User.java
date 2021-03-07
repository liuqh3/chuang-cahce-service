package com.chuang.shi.yun.chuangservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    private String name;
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
