package com.chuang.shi.yun.chuangservice.controller;

import com.chuang.shi.yun.chuangservice.common.ResponseBody;
import com.chuang.shi.yun.chuangservice.dto.Item;
import com.chuang.shi.yun.chuangservice.dto.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @RequestMapping(value = "user.xml" , produces = {"application/xml;charset=UTF-8"}, method = RequestMethod.POST)
    public User getUser(String name,String id){
        User user = new User();
        Item item = new Item();
        item.setName(name);
        item.setItem_id(id);
        user.setItem(item);
        return user;
    }
    @RequestMapping("getList")
    public  ResponseBody  getList(){
        ResponseBody responseBody = new ResponseBody();
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        responseBody.setList(list);
        responseBody.setName("liuqh");
        return responseBody;
    }
}
