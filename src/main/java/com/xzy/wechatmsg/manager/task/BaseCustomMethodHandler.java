package com.xzy.wechatmsg.manager.task;

import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @description: BaseCustomMethodHandler
 * @author: Xie zy
 * @create: 2023.09.07
 */
@Component
public class BaseCustomMethodHandler {
    @Value(value = "${wechat.url}")
    private String url;

    private final String SEND_PATH_XIAONIUREN_TEMPLATE = "/xiaoniuren?msg={msg}";

    private static final String SEND_PATH_SEND_TEXT_MSG = "/sendTextMsg";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    protected void sendToXiaoniuren(String msg) {
        String url = this.url + SEND_PATH_XIAONIUREN_TEMPLATE;
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
    }

    protected void sendTextMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_TEXT_MSG;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
