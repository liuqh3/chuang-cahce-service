package com.chuang.shi.yun.chuangservice.shcheduled;

import com.chuang.shi.yun.chuangservice.common.JsonParse;
import com.chuang.shi.yun.chuangservice.service.impl.ChuangCacheServiceImpl;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@EnableScheduling   //开启定时认为
@EnableAsync
public class CallbackXmlSchedule {
    private static final Logger logger = LoggerFactory.getLogger(CallbackXmlSchedule.class);
    private static RestTemplate restTemplate=new RestTemplate();
    /**
     * 任务排队执行
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void callbackXmlTask(){
        try{
            //目前key是uuid
            Set<Map.Entry<String, Map<String, Object>>> entrySet = ChuangCacheServiceImpl.allRequestInfoMap.entrySet();
            for (Map.Entry<String, Map<String, Object>> entry : entrySet) {
                String uuid = entry.getKey();
                Map<String, Object> tempMap = entry.getValue();
                String xml= JsonParse.parseJson((List<String>)tempMap.get("list_task_id"),(Elements)tempMap.get("eles_item_id"),
                        (Elements)tempMap.get("publish_path"),(String)tempMap.get("op"),(List<String>)tempMap.get("list_value"),
                        (String)tempMap.get("object_type"));
            //如果这里拼凑2，5则删除该条记录，这里需要加判断
                if (xml!=null){
                    String publish_report = (String)tempMap.get("publish_report");
                    String body = restTemplate.postForEntity(publish_report, xml, String.class).getBody();
                    logger.info("body="+body);
                    ChuangCacheServiceImpl.allRequestInfoMap.remove(uuid);
                }
            }
        }catch (Exception e){
            logger.error("定时任务执行失败"+e.getMessage(),e);
        }
    }
}
