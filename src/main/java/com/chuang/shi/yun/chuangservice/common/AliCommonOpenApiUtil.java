package com.chuang.shi.yun.chuangservice.common;

import com.chuang.shi.yun.chuangservice.dto.AliBaBaOpenApiCommon;
import com.chuang.shi.yun.chuangservice.util.BuildCdnUrlUtils;

import java.util.HashMap;
import java.util.Map;

public class AliCommonOpenApiUtil {
    /**
     * 阿里openApi封装
     */
    public static String aliCommonOpenApi(AliBaBaOpenApiCommon api){
        Map<String,String> param=new HashMap<>();
        //请求参数：非公共部分
        param.put(api.getArgs(),api.getArgs_uri());
        param.put("Action",api.getAction());

        String url= BuildCdnUrlUtils.getFinalUrl(Constants.GET,BuildCdnUrlUtils.build20180510PublicParameters(),param);
        String body= HttpUtils.sendHttpGet(url);
        return body;
    }
}
