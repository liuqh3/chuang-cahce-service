package com.chuang.shi.yun.chuangservice.controller.chuang;

import com.chuang.shi.yun.chuangservice.service.ChuangCahceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chuangcache")
public class PublishController {
  private final static Logger logger = LoggerFactory.getLogger(PublishController.class);

  @Autowired
  private ChuangCahceService chuangCahceService;

  @RequestMapping(value = "/publishService.do",produces = {"application/xml;charset=UTF-8"},method = RequestMethod.POST)
  public Object publishService(@RequestBody String publish){
      return chuangCahceService.chuangCachePublishRefresh(publish);
  }
}
