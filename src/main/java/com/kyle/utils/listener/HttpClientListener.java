package com.kyle.utils.listener;

import com.alibaba.fastjson.JSON;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 调用第三方接口监听接口
 *
 * @author: hehongbo
 */

public interface HttpClientListener {

    Logger log = LoggerFactory.getLogger(HttpClientListener.class);

    HashMap<Integer,String> RESPONSE_CODE_MAP = new HashMap<Integer,String>(){{
        put(200,"(成功)服务器已成功处理了请求");
    }};

    default void execute(Request request, String result, CallInfo info) {
        log.debug("url:{},method:{},body:{},headers:{},response:{},responseCode:{},responseCodeDesc:{}", request.url(),
                request.method(),
                JSON.toJSONString(request.body()),
                JSON.toJSONString(request.headers()), result, HttpClientHolder.getResponseCode(),RESPONSE_CODE_MAP.get(HttpClientHolder.getResponseCode()));

    }


    default Request pre(Request request) {
        return request;
    }
}
