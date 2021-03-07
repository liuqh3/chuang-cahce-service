package com.chuang.shi.yun.chuangservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chuang.shi.yun.chuangservice.common.AliCommonOpenApiUtil;
import com.chuang.shi.yun.chuangservice.common.Constants;
import com.chuang.shi.yun.chuangservice.common.exception.BusinessException;
import com.chuang.shi.yun.chuangservice.dto.AliBaBaOpenApiCommon;
import com.chuang.shi.yun.chuangservice.enums.RetCode;
import com.chuang.shi.yun.chuangservice.service.ChuangCahceService;

import com.chuang.shi.yun.chuangservice.util.MD5;
import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ChuangCacheServiceImpl implements ChuangCahceService {
    /**
     * publish,update,delete 请求信息 key:uid value map-->list_task_id,item_id,publish_path,op,list_value,object_type
     */
    public static Map<String, Map<String, Object>> allRequestInfoMap = new ConcurrentHashMap<>(256);
    private static final Logger logger = LoggerFactory.getLogger(ChuangCacheServiceImpl.class);

    @Override
    public Object chuangCachePublishRefresh(String publish) {
        logger.info("进入 chuangCachePublishRefresh 方法" + publish);
        if (null == checkParamerter(publish, "passwd")) {
            return returnXml(Constants.FAIURE, Constants.PASSWORD);
        }
        org.jsoup.nodes.Document document = Jsoup.parse(publish);
        Elements ele_password = document.getElementsByTag("passwd");
        org.jsoup.nodes.Element eles_password = ele_password.get(0);
        String password_text = eles_password.text();
        logger.info("password_text:" + password_text);

        Elements item_value2 = document.getElementsByAttribute("value");
        String value_item = item_value2.attr("value");
        String secret = null;
        try {
            secret = MD5.GetMD5Code(value_item + "678000" + "chuangshiyun" + "c2s0y1QVideo");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Objects.equals(secret, password_text)) {
            return returnXml(Constants.FAIURE, Constants.AUTHORIZATION);
        }

        if (null == checkParamerter(publish, "ccsc")) {
            return returnXml(Constants.FAIURE, Constants.PUBLISH_XML);
        }

        if (null == checkParamerter(publish, "cust_id")) {
            return returnXml(Constants.FAIURE, Constants.CUST_ID);
        }

        if ((null == checkParamerter(publish, "item_id")) || (checkParamerter(publish, "item_id").length() > 500)) {
            return returnXml(Constants.FAIURE, Constants.CUST_ID);
        }

        if (null == checkParamerter(publish, "source_path")) {
            return returnXml(Constants.FAIURE, Constants.SOURCE_PATH);
        }

        if (null == checkParamerter(publish, "publish_path")) {
            return returnXml(Constants.FAIURE, Constants.PUBLISH_PATH);
        }

        List<String> list_value = new ArrayList<>();
        Elements eles_item_id = document.getElementsByTag("item_id");
        for (int i = 0; i < eles_item_id.size(); i++) {
            Element value = document.getElementsByAttribute("value").get(i);
            String value_text = value.attr("value");
            System.out.println("value_text = " + value_text);
            list_value.add(value_text);
        }
        Elements op = document.getElementsByTag("op");
        Element op_type = op.get(0);
        logger.info("op&&&:" + op_type);

        Elements source_path = document.getElementsByTag("source_path");
        String source_text = source_path.text();
        if (source_text.contains("https")) {
            return returnXml(Constants.FAIURE, "not support https");
        }
        logger.info("source_path:" + source_path.size());
        Elements publish_path = document.getElementsByTag("publish_path");
        String publish_text = publish_path.text();
        if (publish_text.contains("https")) {
            return returnXml(Constants.FAIURE, "not support https");
        }
        logger.info("publish_path.size" + publish_path.size());

        //不需要上报的情况
        String commonString ;
        if (null == checkParamerter(publish, "publish_report")) {
            commonString = getElements(eles_item_id, source_path, publish_path, op_type);
            logger.info("commonString = " + commonString);
            return commonString;
        }
        Elements eles_publish_report = document.getElementsByTag("publish_report");
        Element ele_publish_report = eles_publish_report.get(0);
        String publish_report_text = ele_publish_report.text();
        logger.info("publish_report_text = " + publish_report_text);

        //todo 需要上报的情况
        if (Objects.equals(op_type.toString(), "publish")) {
            String publishElements = getPublishElements(eles_item_id, source_path, publish_path, op_type, list_value, publish_report_text);
            logger.info("publishElements = "+publishElements);
            getStringElements(publishElements,"PushTaskId");
        }

        if (Objects.equals(op_type.toString(), "delete")) {
            String deleteElements = getPublishElements(eles_item_id, source_path, publish_path, op_type, list_value, publish_report_text);
            logger.info("publishElements = "+deleteElements);
            getStringElements(deleteElements,"RefreshTaskId");
        }

        if (Objects.equals(op_type.toString(), "update")) {
            String updateElements = getPublishElements(eles_item_id, source_path, publish_path, op_type, list_value, publish_report_text);
            logger.info("publishElements = "+updateElements);
            getStringElements(updateElements,"PushTaskId");
        }
        return returnXml(Constants.FAIURE, Constants.OTHERS);
    }

    /**
     *StringElements 封装
     */
    private  String getStringElements(String element,String taskId){
        if(element.isEmpty()){
            return returnXml(Constants.FAIURE,Constants.FAIURE);
        }else {
            if(element.contains(taskId)){
                return returnXml(Constants.SUCCESS,Constants.SUCCESS_MESSAGE);
            }else {
                return returnXml(Constants.FAIURE,"call openApi failed");
            }
        }
    }

    private String getPublishElements(Elements item_id, Elements source_path, Elements publish_path, Element op_type, List<String> list_value, String publish_report) {
        //todo
        logger.info("进入service层getPublishElements方法" + item_id + "op_type" + op_type);
        List<String> publish_list = new ArrayList<>();
        List<String> delete_list = new ArrayList<>();
        List<String> update_list = new ArrayList<>();
        List<String> list_task_id = new ArrayList<>();
        int size = item_id.size();
        for (int i = 0; i < size; i++) {
            String source_url = getElementPath(source_path, i);
            logger.info("source_url" + source_url);

            if (Objects.equals(op_type.toString(), "publish")) {
                String publish2 = commonObjectCacheOpenApi(Constants.PUSH_ACTION, Constants.AK, Constants.SK, source_url);
                publish_list.add(publish2);
                logger.info("publish2 = " + publish2);
                if (publish2 != null) {
                    String pushTaskId = getStringToJson(publish2, "PushTaskId");
                    logger.info("pushTaskId = " + pushTaskId);
                    list_task_id.add(pushTaskId);
                }
            } else if (Objects.equals(op_type.toString(), "delete")) {
                String delete = commonObjectCacheOpenApi(Constants.REFRESH_ACTION, Constants.AK, Constants.SK, source_url);
                delete_list.add(delete);
                logger.info("publish2 = " + delete);
                if (delete != null) {
                    String refreshTaskId = getStringToJson(delete, "RefreshTaskId");
                    logger.info("refreshTaskId = " + refreshTaskId);
                    list_task_id.add(refreshTaskId);
                }
            } else if (Objects.equals(op_type.toString(), "update")) {
                String update = commonObjectCacheOpenApi(Constants.REFRESH_ACTION, Constants.AK, Constants.SK, source_url);
                if (update != null) {
                    String update2 = commonObjectCacheOpenApi(Constants.PUSH_ACTION, Constants.AK, Constants.SK, source_url);
                    update_list.add(update);
                    if (update2 != null) {
                        String refreshTaskId = getStringToJson(update, "RefreshTaskId");
                        logger.info("refreshTaskId = " + refreshTaskId);
                        list_task_id.add(refreshTaskId);
                    }
                }
            } else {
                logger.info("您输入的不合法 请输入 publish | delete | update这三种中的其中一个");
                return returnXml(Constants.FAIURE, Constants.OTHERS);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list_task_id", list_task_id);
        map.put("eles_item_id", item_id);
        map.put("publish_path", publish_path);
        map.put("op_type", op_type);
        map.put("list_value", list_value);
        String object_type = "";
        if (Objects.equals(op_type.toString(), "publish")) {
            object_type = "preload";
        } else if (Objects.equals(op_type.toString(), "delete")) {
            object_type = "file";
        } else if (Objects.equals(op_type.toString(), "update")) {
            object_type = "preload";
        }
        map.put("object_type", object_type);
        map.put("publish_report", publish_report);
        ChuangCacheServiceImpl.allRequestInfoMap.put(UUID.randomUUID().toString(),map);
        if (Objects.equals(op_type.toString(),"publish")){
            return String.join("",publish_list);
        } else if (Objects.equals(op_type.toString(),"delete")){
            return String.join("",delete_list);
        } else if (Objects.equals(op_type.toString(),"update")){
            return String.join("",update_list);
        }else {
            return returnXml(Constants.FAIURE,Constants.OTHERS);
        }

    }

    private String getStringToJson(String str, String param) {
        String data2 = JSON.toJSONString(str);
        String data3 = data2.replace("\\", "");
        String data4 = data3.substring(1, data3.length() - 1);
        String data5 = data4.substring(1, data4.length() - 1);
        logger.info("data5=" + data5);
        JSONObject jsonObject = JSON.parseObject(data4);
        String taskId = (String) jsonObject.get(param);
        return taskId;
    }

    private String getElements(Elements item_id, Elements source_path, Elements publish_path, Element op_type) {
        logger.info("进入service层getElements 方法" + "source_path" + source_path + "op_type:" + op_type);
        String publish2;
        String delete;
        String update;
        String update2;

        List<String> publish_list = new ArrayList<>();
        List<String> delete_list = new ArrayList<>();
        List<String> update_list = new ArrayList<>();
        List<String> update_list2 = new ArrayList<>();
        for (int i = 0; i < item_id.size(); i++) {
            String source_url = getElementPath(source_path, i);
            logger.info("source_url = " + source_url);
            String publish_url = getElementPath(publish_path, i);
            logger.info("publish_url = " + publish_url);
            if (Objects.equals(op_type.toString(), "publish")) {
                publish2 = commonObjectCacheOpenApi(Constants.PUSH_ACTION, Constants.AK, Constants.SK, source_url);
                publish_list.add(publish2);
                logger.info("publish2 = " + publish2);
            } else if (Objects.equals(op_type.toString(), "delete")) {
                delete = commonObjectCacheOpenApi(Constants.REFRESH_ACTION, Constants.AK, Constants.SK, source_url);
                delete_list.add(delete);
                logger.info("delete = " + delete);
            } else if (Objects.equals(op_type.toString(), "update")) {
                update = commonObjectCacheOpenApi(Constants.REFRESH_ACTION, Constants.AK, Constants.SK, source_url);
                update2 = commonObjectCacheOpenApi(Constants.PUSH_ACTION, Constants.AK, Constants.SK, source_url);
                update_list.add(update2);
                logger.info("update2 = " + update2);
            } else {
                logger.info("您输入的不合法 请输入 publish | delete | update这三种中的其中一个");
                return returnXml(Constants.FAIURE, Constants.OTHERS);
            }
        }
        if (!publish_list.isEmpty()) {
            return returnXml(Constants.SUCCESS, Constants.PUBLISH);
        }
        if (!delete_list.isEmpty()) {
            return returnXml(Constants.SUCCESS, Constants.DELETE);
        }
        if (!update_list.isEmpty()) {
            return returnXml(Constants.SUCCESS, Constants.UPDATE);
        }

        return returnXml(Constants.FAIURE, Constants.FAIURE);
    }

    private String commonObjectCacheOpenApi(String action, String ak, String sk, String path) {
        AliBaBaOpenApiCommon api = new AliBaBaOpenApiCommon();
        api.setArgs("ObjectPath");
        api.setAction(action);

        if (path.contains("vshywbhls.tc.qq.com")) {
            String vs_lts = path.replace("http://vshywbhls.tc.qq.com", "ltscsy.qq.com");
            logger.info("vs_lts = " + vs_lts);
            api.setArgs_uri(vs_lts);
        } else if (path.contains("videohywb.tc.qq.com")) {
            String vs_lts2 = path.replace("http://videohywb.tc.qq.com", "ltscsy.qq.com");
            logger.info("vs_lts = " + vs_lts2);
            api.setArgs_uri(vs_lts2);
        } else {
            String replace = path.replace(" ", "");
            logger.info("replace = " + replace);
            if (path.contains("ltscsy.qq.com")) {
                api.setArgs_uri(replace);
            }
            if (path.contains("ugccsy.qq.com")) {
                api.setArgs_uri(replace);
            }
            if (path.contains("lmcsy.qq.com")) {
                api.setArgs_uri(replace);
            }
        }
        String commonObject = AliCommonOpenApiUtil.aliCommonOpenApi(api);
        logger.info("类型" + action + "信息刷新成功" + commonObject);
        return commonObject;
    }

    private String getElementPath(Elements path, int i) {
        Element element = path.get(i);
        String path_text = element.text();
        String replace_path_text = path_text.replace(" ", "");
        logger.info("replace_path_text = " + replace_path_text);
        return replace_path_text;
    }


    private String checkParamerter(String xml, String str) {
        String str_text = null;
        if (str != null) {
            org.jsoup.nodes.Document document = Jsoup.parse(xml);
            Elements str2 = document.getElementsByTag(str);
            logger.info("source_path" + str2.size());
            for (int i = 0; i < str2.size(); i++) {
            Element element_str =  str2.get(i);
                str_text = element_str.text();
                if (str_text.contains("https")) {
                    return returnXml(Constants.FAIURE, "not support https");
                }
                logger.info("str_text" + str_text);
                if (str_text == null) {
                    throw new BusinessException(RetCode.ERR_PARAM_NULL + str);
                }
            }
        } else {
            return null;
        }
        return str_text;
    }

    private String returnXml(String state, String str) {
        org.dom4j.Document requestDoc = DocumentHelper.createDocument();
        org.dom4j.Element root = requestDoc.addElement("ccsc");
        root.addElement("result").addText(state);
        root.addElement("detail").addText(str);
        return root.asXML();
    }
}
