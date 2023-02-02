package com.xzy.wechatmsg.manager.robot;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.client.WechatRobotClient;
import com.xzy.wechatmsg.domain.chatgpt.model.ChatGptReq;
import com.xzy.wechatmsg.domain.chatgpt.model.ChatGptResp;
import com.xzy.wechatmsg.domain.robot.model.WechatMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvPicMsg;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvTxtMsg;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: RobotMsgHandler
 * @author: Xie zy
 * @create: 2023.01.31
 */
@Component
public class RobotMsgHandler {


    private static Map<String, List<String>> autoRespMap = new ConcurrentHashMap<String, List<String>>(){{
        //k为roomId，v为wxId
    }};


    @Value(value = "${openai.token}")
    private String token;

    public WechatMsgDTO buildWechatMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        return JSON.parseObject(JSON.toJSONString(msg), WechatMsgDTO.class);
    }

    public WechatRsvTxtMsg parseTxtMsg(WechatRsvMsgDTO msg) {
        return JSON.parseObject(JSON.toJSONString(msg), WechatRsvTxtMsg.class);
    }

    public WechatRsvPicMsg parsePicMsg(WechatRsvMsgDTO msg) {
        //TODO
        return null;
    }

    public String dealAtGroupTxtMsg(WechatRobotClient wechatRobotClient, String roomId, String wxId, String txt) {
        //TODO 下面这行有 bug
//        String nickName = wechatRobotClient.getChatroomMemberNick(roomId,wxId);
        //1.如果是开启自动回复，那就加入map，保存当前群聊，并返回收到
        //2.如果是关闭自动回复，那就从map中移除，并返回已关闭自动回复，保留@回复
        if ("开启我的自动回复".equals(txt)) {
            List<String> list = autoRespMap.getOrDefault(roomId, new ArrayList<>());
            list.add(wxId);
            autoRespMap.put(roomId, list);
            return "已开启" + "nickName" + "的自动回复";
        } else if ("关闭我的自动回复".equals(txt)) {
//            List<String> list = autoRespMap.getOrDefault(roomId, new ArrayList<>());
//            autoRespMap.put(roomId, list);
//            autoRespMap.put(roomId, wxId);
            return "已关闭" + "nickName" + "的自动回复";
        } else if ("开启所有自动回复".equals(txt)) {
            return "已开启所有自动回复";
        } else if ("关闭所有自动回复".equals(txt)) {
            return "已关闭所有自动回复";
        } else {
//            //空一行，在发言，因为前面有个@
//            return System.lineSeparator() + chat(txt);
            return chat(txt);
        }
    }

    public String dealNormalGroupTxtMsg(String txt) {
        return chat(txt);
    }

    public String dealPrivateTxtMsg(String txt) {
        return chat(txt);
    }

    public String dealAtGroupPicMsg(WechatRsvPicMsg pic) {
        //TODO
        return null;
    }

    public String dealNormalGroupPicMsg(WechatRsvPicMsg pic) {
        //TODO
        return null;
    }

    public String dealPrivatePicMsg(WechatRsvPicMsg pic) {
        //TODO
        return null;
    }

    /**
     * chatGpt chat
     */
    public String chat(String input) {
        String completionsApi = "https://api.openai.com/v1/completions";

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer ".concat(token));

        //body
        ChatGptReq body = new ChatGptReq();
        body.setModel("text-davinci-003");
        body.setPrompt(input + "\n");
        //返回消息长度
        body.setMax_tokens(3000);
        //0每次回答一样-1每次回答不一样
        body.setTemperature(0.8f);

        HttpEntity<ChatGptReq> entity = new HttpEntity<>(body, headers);

        //post
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(completionsApi, entity, String.class);
        } catch (Exception e) {
            return "chatGpt调用出错，请更改你的问法或稍后重试";
        }
        ChatGptResp chatGptResp = JSON.parseObject(responseEntity.getBody(), ChatGptResp.class);

        if (chatGptResp == null || CollectionUtils.isEmpty(chatGptResp.getChoices())) {
            return "不好意思我没理解您的意思";
        } else {
            String text = chatGptResp.getChoices().get(0).getText();
            return text.replaceAll("^[" + System.lineSeparator() + "]", "").replaceAll("[" + System.lineSeparator() + "]$", "");
        }
    }

}
