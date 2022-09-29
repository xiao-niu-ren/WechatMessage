package com.xzy.wechatmsg.client;

import com.xzy.wechatmsg.request.task.WechatMsgWithInfoAndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    private final String GET_PATH_WECHAT_USER_LIST = "/getWeChatUserList";

    private final String GET_PATH_GET_MEMBER_ID = "/getMemberId";

    private final String GET_PATH_GET_CHATROOM_MEMBER_NICK_TEMPLATE = "/getChatroomMemberNick/{roomid}/{wxid}";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    public void sendToXiaoniuren(String msg) {
        String url = this.url + SEND_PATH_XIAONIUREN_TEMPLATE;
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
    }

    public void sendTextMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_TEXT_MSG;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public void sendImgMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_IMG_MSG;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public void sendAtMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_AT_MSG;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public void sendAnnex(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_ANNEX;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

//    public void getWeChatUserList(WechatMsgWithInfoAndType.WechatMsg msg){
//        String url = this.url + GET_PATH_WECHAT_USER_LIST;
//        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
//    }
//
//    public void getMemberId(WechatMsgWithInfoAndType.WechatMsg msg){
//        String url = this.url + GET_PATH_GET_MEMBER_ID;
//        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
//    }
//
//    public void getChatroomMemberNick(WechatMsgWithInfoAndType.WechatMsg msg){
//        String url = this.url + GET_PATH_GET_CHATROOM_MEMBER_NICK_TEMPLATE;
//        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
//    }

}
