package com.chuang.shi.yun.chuangservice.controller;

import java.util.List;

import com.chuang.shi.yun.chuangservice.common.CloudFengUtil;
import com.chuang.shi.yun.chuangservice.dto.Body;
import com.chuang.shi.yun.chuangservice.dto.Record;
import com.chuang.shi.yun.chuangservice.dto.Records;
import com.chuang.shi.yun.chuangservice.dto.XmlRemoteDto;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



public class AA {
    /**
     * yunfeng POST 方式  用的是@RequestBody  注解 参数类型是String
     * @param
     * @return
     */
    @PostMapping("/rpt/smsStatusPullYunFeng")
    public Response smsStatusPullYunFeng(@RequestBody String msgreport) {
        Response resp = new Response();
       // log.info("yunfeng,回调参数为={}",msgreport);
        XmlRemoteDto dto = (XmlRemoteDto) CloudFengUtil.convertXmlStrToObject(XmlRemoteDto.class, msgreport);
        Body body = dto.getBody();
        Records records = body.getRecords();
        List<Record> rec = records.getRecord();
        Record record = rec.get(0);
        int count = 0;
        // ...............
        return resp;
    }
}
