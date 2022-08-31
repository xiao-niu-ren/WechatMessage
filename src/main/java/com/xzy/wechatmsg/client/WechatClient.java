package com.xzy.wechatmsg.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: WechatClient
 * @author: Xie zy
 * @create: 2022.08.31
 */
@Component
public class WechatClient {

    @Value(value = "${wechat.url}")
    private String url;

    private final String SEND_PATH_XIAONIUREN_TEMPLATE = "/xiaoniuren?msg={msg}";

    private final String SEND_PATH_SEND_TEXT_MSG = "/sendTextMsg";

    private final String SEND_PATH_SEND_IMG_MSG = "/sendImgMsg";

    private final String SEND_PATH_SEND_AT_MSG = "/sendATMsg";

    private final String SEND_PATH_SEND_ANNEX = "/sendAnnex";

    private final String GET_PATH_SEND_ANNEX = "/getWeChatUserList";

    private final String GET_PATH_GET_MEMBER_ID = "/getMemberId";

    private final String GET_PATH_GET_CHATROOM_MEMBER_NICK_TEMPLATE = "/getChatroomMemberNick/{roomid}/{wxid}";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    public void sendToXiaoniuren(String msg){
        String url = this.url + SEND_PATH_XIAONIUREN_TEMPLATE;
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
    }

}
