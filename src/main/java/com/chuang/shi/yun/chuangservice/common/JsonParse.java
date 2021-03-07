package com.chuang.shi.yun.chuangservice.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chuang.shi.yun.chuangservice.dto.CDNTaskObject;
import com.chuang.shi.yun.chuangservice.dto.DescribeRefreshTaskObject;
import com.chuang.shi.yun.chuangservice.dto.TaskObject;
import com.chuang.shi.yun.chuangservice.util.BuildCdnUrlUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class JsonParse {
    private static final Logger logger = LoggerFactory.getLogger(JsonParse.class);

    public static String parseJson(List<String> list_task_id, Elements eles_item_id, Elements publish_path, String op, List<String> list_value, String object_type) {
        logger.info("publish_path:" + publish_path);
        for (int i = 0; i < list_task_id.size(); i++) {
            String s = list_task_id.get(i);
            logger.info("s=" + s);
        }
        //CountDownLatch
        //CyclicBarrier
        //Semaphore
        //Exchanger
        //自己造的数据
     /*   DescribeRefreshTaskObject task = new DescribeRefreshTaskObject();
        TaskObject taskObject = new TaskObject();
        List<CDNTaskObject> cdn=new ArrayList<>();
        CDNTaskObject cdnTask = new CDNTaskObject();
        cdnTask.setCreationTime("2020-09-20");
        cdnTask.setObjectPath("a/b/c/test/text");
        cdnTask.setObjectType("file");
        cdnTask.setStatus("Complete");
        cdnTask.setProcess("100%");
        cdnTask.setTasksId("1234567");

        CDNTaskObject cdnTask2 = new CDNTaskObject();
        cdnTask2.setCreationTime("2020-09-20");
        cdnTask2.setObjectPath("a/b/c/test/text");
        cdnTask2.setObjectType("file");
        cdnTask2.setStatus("Complete");
        cdnTask2.setProcess("100%");
        cdnTask2.setTasksId("1234560");

        cdn.add(cdnTask);
        cdn.add(cdnTask2);
        taskObject.setCDNTask(cdn);
        task.setTasks(taskObject);
        task.setPageNumber(Long.valueOf(1L));
        task.setPageSize(Long.valueOf(1L));
        task.setTotalCount(Long.valueOf(1L));
        task.setRequestId("123345h");

        String jsonString = JSONObject.toJSONString(task);
        System.out.println("jsonString = " + jsonString);*/

        List<String> list = new ArrayList<>();
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("ccsc");
        if (list_task_id != null) {
            //用item进行遍历，或者用task遍历
            for (int i = 0; i < eles_item_id.size(); i++) {
                //当一个task过来的时候或调用getDescribeRefreshTasks
                String publish_url = getElementPath(publish_path, i);
                logger.info("publish_url = " + publish_url);
                String describeRefreshTasks = null;
                if (list_task_id.size() == 0) {
                    String str = list_task_id.get(i);
                    logger.info("str = " + str);
                    describeRefreshTasks = getDescribeRefreshTasks(null, publish_url, "preload");
                } else {
                    String str = list_task_id.get(i);
                    logger.info("str = " + str);
                    describeRefreshTasks = getDescribeRefreshTasks(list_task_id.get(i), publish_url, object_type);
                    logger.info("describeRefreshTasks = " + describeRefreshTasks);
                }
                String data = parseStringJson(describeRefreshTasks);
                JSONObject jsonObject = JSONObject.parseObject(data);
                logger.info("jsonObject = " + jsonObject);
                String requestId = jsonObject.getString("RequestId");
                logger.info("requestId = " + requestId);
                String tasks = jsonObject.getString("Tasks");
                if (tasks == null) {
                    Document requestDoc2 = DocumentHelper.createDocument();
                    Element root22 = requestDoc2.addElement("ccsc");
                    for (int m = 0; m < eles_item_id.size(); m++) {
                        Element paramsElement = root22.addElement("item_id");
                        String value = list_value.get(m);
                        logger.info("value = " + value);
                        paramsElement.addAttribute("value", list_value.get(m));

                        if (Objects.equals(op, "publish")) {
                            paramsElement.addElement("op_name").addText(op);
                            paramsElement.addElement("op_status").addText("publish failed");
                        }

                        if (Objects.equals(op, "delete")) {
                            paramsElement.addElement("op_name").addText(op);
                            paramsElement.addElement("op_status").addText("delete failed");
                        }

                        if (Objects.equals(op, "update")) {
                            paramsElement.addElement("op_name").addText(op);
                            paramsElement.addElement("op_status").addText("update failed");
                        }
                    }
                    root22.asXML();
                }
                JSONObject jsonObject2 = null;
                //  JSONObject jsonObject12= jsonObject.parseObject(tasks);
                JSONArray cdnTaskArray = jsonObject2.getJSONArray("CDNTask");
                logger.info("cdnTaskArray = " + cdnTaskArray);
                for (int j = 0; j < cdnTaskArray.size(); j++) {
                    String status = cdnTaskArray.getJSONObject(j).getString("Status");
                    //todo 还没有写完
                    list.add(status);
                }
                String state = parseStatus(list);
                Element paramsElement = root.addElement("item_id");
                paramsElement.addAttribute("value", list_value.get(i));
                if (Objects.equals(op, "publish")) {
                    paramsElement.addElement("op_name").addText(op);
                    if (Objects.equals(state, "Complete")) {
                        paramsElement.addElement("op_status").addText("sync finish");
                    }
                    if (Objects.equals(state, "Refreshing")) {
                        paramsElement.addElement("op_status").addText("doing");
                        return null;
                    }
                    if (Objects.equals(state, "Failed")) {
                        paramsElement.addElement("op_status").addText("downing failed");
                    }
                    if (Objects.equals(state, "Pending")) {
                        paramsElement.addElement("op_status").addText("doing");
                        return null;
                    }
                }

                if (Objects.equals(op, "update")) {
                    paramsElement.addElement("op_name").addText(op);
                    if (Objects.equals(state, "Complete")) {
                        paramsElement.addElement("op_status").addText("update finish");
                    }
                    if (Objects.equals(state, "Refreshing")) {
                        paramsElement.addElement("op_status").addText("doing");
                        return null;
                    }
                    if (Objects.equals(state, "Failed")) {
                        paramsElement.addElement("op_status").addText("downing failed");
                    }
                    if (Objects.equals(state, "Pending")) {
                        paramsElement.addElement("op_status").addText("doing");
                        return null;
                    }
                }

                if (Objects.equals(op, "delete")) {
                    paramsElement.addElement("op_name").addText(op);
                    if (Objects.equals(state, "Complete")) {
                        paramsElement.addElement("op_status").addText("delete finish");
                    }
                    if (Objects.equals(state, "Refreshing")) {
                        paramsElement.addElement("op_status").addText("doing");
                        return null;
                    }
                    if (Objects.equals(state, "Failed")) {
                        paramsElement.addElement("op_status").addText("downing failed");
                    }
                    if (Objects.equals(state, "Pending")) {
                        paramsElement.addElement("op_status").addText("doing");
                        return null;
                    }
                }
                list.clear();
            }
            return root.asXML();
        } else {
            for (int i = 0; i < eles_item_id.size(); i++) {
                Element paramsElement = root.addElement("item_id");
                paramsElement.addAttribute("value", list_value.get(i));
                if (Objects.equals(op, "publish")) {
                    paramsElement.addElement("op_name").addText(op);
                    paramsElement.addElement("op_status").addText("download failed");
                }
                if (Objects.equals(op, "delete")) {
                    paramsElement.addElement("op_name").addText(op);
                    paramsElement.addElement("op_status").addText("delete failed");
                }
                if (Objects.equals(op, "update")) {
                    paramsElement.addElement("op_name").addText(op);
                    paramsElement.addElement("op_status").addText("download failed");
                }
            }
        }
        return root.asXML();
    }

    private static String parseStatus(List<String> list) {
        Set<String> set_status = new HashSet<>();
        String state = null;
        if (set_status.size() == 1) {
            if (set_status.contains("Complete")) {
                state = "Complete";
            }
            if (set_status.contains("Refreshing")) {
                state = "Refreshing";
            }
            if (set_status.contains("Failed")) {
                state = "Failed";
            }
            if (set_status.contains("Pending")) {
                state = "Pending";
            }
        } else if (set_status.contains("Failed")) {
            state = "Failed";
        } else if (set_status.contains("Pending")) {
            state = "Pending";
        } else if (set_status.contains("Refreshing")) {
            state = "Refreshing";
        } else {
            state = "Complete";
        }
        return state;
    }

    private static String parseStringJson(String str) {
        String data2 = JSON.toJSONString(str);
        String data3 = data2.replace("\\", "");
        String data4 = data3.substring(1, data3.length() - 1);
        logger.info("data4 = " + data4);
        //String data5 = data4.substring(1, data4.length() - 1);
        //logger.info("data5 = "+data5);
        return data4;
    }

    private static String getDescribeRefreshTasks(String taskId, String publish_url, String object_type) {
        String domainName = "cdn.aliyuncs.com";
        String app_key = "LTAI45fkELKwljLczVq8RnR";
        String Format = "JSON";
        String Version = "2018-05-10";
        String SignatureMethod = "HMAC-SHA1";
        String SignatureVersion = "1.0";
        //String Action="DescribeRefreshTasks";

        Map<String, String> param = new HashMap<>();
        param.put("Action", "DescribeRefreshTasks");
        param.put("TaskId", taskId);
        if (publish_url.contains("vshywbhls.tc.qq.com")) {
            param.put("DomainName", "ltscsy.qq.com");
        } else if (publish_url.contains("videohywb.tc.qq.com")) {
            param.put("DomainName", "ltscsy.qq.com");
        } else {
            if (publish_url.contains("ltscsy.qq.com")) {
                param.put("DomainName", "ltscsy.qq.com");
            }
            if (publish_url.contains("ugccsy.qq.com")) {
                param.put("DomainName", "ugccsy.qq.com");
            }
            if (publish_url.contains("lmcsy.qq.com")) {
                param.put("DomainName", "lmcsy.qq.com");
            }
        }
        String replace_url = null;
        if (publish_url.contains("vshywbhls.tc.qq.com")) {
            String replace = publish_url.replace(Constants.VSH, Constants.LTS);
            replace_url = replace.replace(" ", "");
            logger.info("replace_url2 = " + replace_url);
        } else if (publish_url.contains("videohywb.tc.qq.com")) {
            String replace2 = publish_url.replace(Constants.VIDEO, Constants.LTS);
            replace_url = replace2.replace(" ", "");
            logger.info("replace_url2 = " + replace_url);
        } else {
            replace_url = publish_url.replace(" ", "");
        }
        param.put("ObjectPath", replace_url);
        param.put("ObjectType", object_type);
        String url = BuildCdnUrlUtils.getFinalUrl(Constants.GET, BuildCdnUrlUtils.build20180510PublicParameters(), param);
        String body = HttpUtils.sendHttpGet(url);
        return body;
    }

    private static String getElementPath(Elements path, int i) {
        org.jsoup.nodes.Element element = path.get(i);
        String text = element.text();
        String replace = text.replace(" ", "");
        logger.info("replace = " + replace);
        return replace;
    }

    private String returnXml2(String state, String str) {
        org.dom4j.Document requestDoc = DocumentHelper.createDocument();
        org.dom4j.Element root = requestDoc.addElement("ccsc");
        root.addElement("result").addText(state);
        root.addElement("detail").addText(str);
        return root.asXML();
    }

}
