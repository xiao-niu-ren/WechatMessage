package com.xzy.wechatmsg.client;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.domain.robot.model.WechatMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.exception.task.CustomMethodInvokeException;
import com.xzy.wechatmsg.exception.task.TaskInvokeException;
import com.xzy.wechatmsg.manager.robot.RobotMsgHandler;
import com.xzy.wechatmsg.manager.task.CustomMethodHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

/**
 * @description: WechatClient
 * @author: Xie zy
 * @create: 2022.08.31
 */
@Component
public class WechatRobotClient {

    @Value(value = "${wechat.url}")
    private String url;

    private final String SEND_PATH_XIAONIUREN_TEMPLATE = "/xiaoniuren?msg={msg}";

    private final String SEND_PATH_SEND_TEXT_MSG = "/sendTextMsg";

    private final String SEND_PATH_SEND_IMG_MSG = "/sendImgMsg";

    private final String SEND_PATH_SEND_AT_MSG = "/sendATMsg";

    private final String SEND_PATH_SEND_ANNEX = "/sendAnnex";

    private final String GET_PATH_WECHAT_USER_LIST = "/getWeChatUserList";

    private final String GET_PATH_GET_MEMBER_ID = "/getMemberId";

    private final String GET_PATH_GET_CHATROOM_MEMBER_NICK_TEMPLATE = "/getChatroomMemberNick";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    @Autowired
    RobotMsgHandler robotMsgHandler;

    @Autowired
    CustomMethodHandler customMethodHandler;

    public void sendToXiaoniuren(String msg) {
        String url = this.url + SEND_PATH_XIAONIUREN_TEMPLATE;
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, msg);
    }

    public void sendTextMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_TEXT_MSG;
        HttpEntity<WechatMsgWithInfoAndType.WechatMsg> request = new HttpEntity<>(msg, httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public void sendTextMsgWithCustomMethod(WechatMsgWithInfoAndType.WechatMsg msg) {
        String customMethodName = msg.getCustomMethodName();
        String customMethodParam = msg.getCustomMethodParam();
        try {
            Method method = customMethodHandler.getClass().getDeclaredMethod(customMethodName, String.class);
            method.setAccessible(true);
            method.invoke(customMethodHandler, customMethodParam);
        } catch (Exception e) {
            throw new CustomMethodInvokeException();
        }
    }

    public void sendImgMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_IMG_MSG;
        HttpEntity<WechatMsgDTO> request = new HttpEntity<>(robotMsgHandler.buildWechatMsg(msg), httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public void sendAtMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_AT_MSG;
        HttpEntity<WechatMsgDTO> request = new HttpEntity<>(robotMsgHandler.buildWechatMsg(msg), httpEntity.getHeaders());
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public void sendAnnex(WechatMsgWithInfoAndType.WechatMsg msg) {
        String url = this.url + SEND_PATH_SEND_ANNEX;
        HttpEntity<WechatMsgDTO> request = new HttpEntity<>(robotMsgHandler.buildWechatMsg(msg), httpEntity.getHeaders());
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

    public String getChatroomMemberNick(String roomId, String userId){
        String url = this.url + GET_PATH_GET_CHATROOM_MEMBER_NICK_TEMPLATE + "/" + roomId + "/" + userId;
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        WechatRsvMsgDTO wechatRsvMsgDTO = JSON.parseObject(res.getBody(), WechatRsvMsgDTO.class);
        String content = wechatRsvMsgDTO.getContent();
        WechatRsvMsgDTO.NickNameResp nickNameResp = JSON.parseObject(content, WechatRsvMsgDTO.NickNameResp.class);
        return nickNameResp.getNick();
    }

}
