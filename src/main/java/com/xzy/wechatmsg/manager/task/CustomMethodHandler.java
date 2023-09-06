package com.xzy.wechatmsg.manager.task;

import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.domain.task.model.Task;
import com.xzy.wechatmsg.enums.TaskStatusEnum;
import com.xzy.wechatmsg.request.task.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * @description: CustomMethod
 * @author: Xie zy
 * @create: 2023.09.07
 */
@Component
public class CustomMethodHandler {
    @Value(value = "${wechat.url}")
    private String url;

    private final String SEND_PATH_XIAONIUREN_TEMPLATE = "/xiaoniuren?msg={msg}";

    private static final String SEND_PATH_SEND_TEXT_MSG = "/sendTextMsg";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    private void sendToXiaoniuren(String msg) {
        String url = this.url + SEND_PATH_XIAONIUREN_TEMPLATE;
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
    }

    private void sendTextMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_TEXT_MSG;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    //---------------------------------------------------------------------------------
    public void test(String param) {
        sendToXiaoniuren(param);
    }
}
