package com.xzy.wechatmsg.manager.robot;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.client.WechatRobotClient;
import com.xzy.wechatmsg.domain.chatgpt.model.ChatGptReq30;
import com.xzy.wechatmsg.domain.chatgpt.model.ChatGptReq35;
import com.xzy.wechatmsg.domain.chatgpt.model.ChatGptResp30;
import com.xzy.wechatmsg.domain.chatgpt.model.ChatGptResp35;
import com.xzy.wechatmsg.domain.robot.model.WechatMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvPicMsg;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvTxtMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: RobotMsgHandler
 * @author: Xie zy
 * @create: 2023.01.31
 */
@Component
public class RobotMsgHandler {


    public static Map<String, Set<String>> autoRespMap = new ConcurrentHashMap<String, Set<String>>(){{
        //k为roomId，v为wxId
    }};

    public static Set<String> autoRespRoomSet = new HashSet<String>(){{
        //元素为roomId
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

    public String dealAtGroupTxtMsg(WechatRobotClient wechatRobotClient, String roomId, String userId, String txt) {
        txt = txt.trim();
        String nickName = wechatRobotClient.getChatroomMemberNick(roomId, userId);
        //1.如果是开启自动回复，那就加入map，保存当前群聊，并返回收到
        //2.如果是关闭自动回复，那就从map中移除，并返回已关闭自动回复，保留@回复
        if ("开启我的自动回复".equals(txt)) {
            Set<String> set = autoRespMap.getOrDefault(roomId, new HashSet<>());
            set.add(userId);
            autoRespMap.put(roomId, set);
            return "已开启" + nickName + "的自动回复";
        } else if ("关闭我的自动回复".equals(txt)) {
            Set<String> set = autoRespMap.getOrDefault(roomId, new HashSet<>());
            if (set.contains(userId)) {
                set.remove(userId);
            }
            if (set.isEmpty() && autoRespMap.containsKey(roomId)) {
                autoRespMap.remove(roomId);
            }
            return "已关闭" + nickName + "的自动回复";
        } else if ("开启所有自动回复".equals(txt)) {
            autoRespRoomSet.add(roomId);
            return "已开启所有自动回复";
        } else if ("关闭所有自动回复".equals(txt)) {
            if (autoRespRoomSet.contains(roomId)) {
                autoRespRoomSet.remove(roomId);
            }
            return "已关闭所有自动回复";
        } else {
            return "To " + nickName + ":" + System.lineSeparator() + chat(txt, true);
        }
    }

    public String dealNormalGroupTxtMsg(String txt) {
        txt = txt.trim();
        return chat(txt, false);
    }

    public String dealPrivateTxtMsg(String txt) {
        txt = txt.trim();
        return chat(txt, false);
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
    public String chat(String input, boolean openTips) {
        if (openTips) {
            if (input == null || input.equals("")) {
                return "您好，我是Robot-Niu"
                        + System.lineSeparator()
                        + "可以艾特我提问哦～"
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "更改触发方式可艾特我输入如下文字，两次输入请间隔一段时间"
                        + System.lineSeparator()
                        + "1.开启我的自动回复（不用艾特我就自动回复您的消息）"
                        + System.lineSeparator()
                        + "2.关闭我的自动回复（关闭自动回复您的消息）"
                        + System.lineSeparator()
                        + "3.开启所有自动回复（开启所有群消息自动回复，目前只有小牛人可以）"
                        + System.lineSeparator()
                        + "4.关闭所有自动回复（关闭所有群消息自动回复）";
            }
        } else {
            if (input == null || input.equals("")) {
                return "您好，我是Robot-Niu"
                        + System.lineSeparator()
                        + "可以提问我哦～";
            }
        }

//        // 以下是gpt3
//        String completionsApi = "https://api.openai.com/v1/completions";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        //header
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer ".concat(token));
//
//        //body
//        ChatGptReq30 body = new ChatGptReq30();
//        body.setModel("text-davinci-003");
//        body.setPrompt(input + "\n");
//        //返回消息长度
//        body.setMax_tokens(3000);
//        //0每次回答一样~1每次回答不一样
//        body.setTemperature(0.8f);
//
//        HttpEntity<ChatGptReq30> entity = new HttpEntity<>(body, headers);

        // 以下是gpt3.5
        ////////////////////////////////////////////////////////////
        String completionsApi = "https://api.openai.com/v1/chat/completions";

        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer ".concat(token));

        //body
        ChatGptReq35 body = new ChatGptReq35();
        body.setModel("gpt-3.5-turbo-0301");
        ChatGptReq35.Message message = new ChatGptReq35.Message();
        message.setRole("user");
        message.setContent(input);
        body.setMessages(new ArrayList<ChatGptReq35.Message>() {{
            add(message);
        }});
        //0每次回答一样~1每次回答不一样
        body.setTemperature(0.7f);

        HttpEntity<ChatGptReq35> entity = new HttpEntity<>(body, headers);
        ////////////////////////////////////////////////////////////

        //post
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(completionsApi, entity, String.class);
        } catch (Exception e) {
            return "GPT的api调用出错，请稍后重试~";
        }
        // 以下是gpt3
//        ChatGptResp30 chatGptResp = JSON.parseObject(responseEntity.getBody(), ChatGptResp30.class);
        // 以下是gpt3.5
        ChatGptResp35 chatGptResp = JSON.parseObject(responseEntity.getBody(), ChatGptResp35.class);

        if (chatGptResp == null || CollectionUtils.isEmpty(chatGptResp.getChoices())) {
            return "不好意思我没理解您的意思";
        } else {
            // 以下是gpt3
//            String text = chatGptResp.getChoices().get(0).getText();
            // 以下是gpt3.5
            String text = chatGptResp.getChoices().get(0).getMessage().getContent();
            return text.replaceAll("^[" + System.lineSeparator() + "]", "").replaceAll("[" + System.lineSeparator() + "]$", "");
        }
    }

}
