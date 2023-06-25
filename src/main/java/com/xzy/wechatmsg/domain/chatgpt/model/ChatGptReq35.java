package com.xzy.wechatmsg.domain.chatgpt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @description: ChatGptReq35
 * @author: Xie zy
 * @create: 2023.05.19
 */
@Data
@NoArgsConstructor
public class ChatGptReq35 {
    String model;
    List<Message> messages;
//    Integer max_tokens;
    Float temperature;

    @Data
    @NoArgsConstructor
    public static class Message {
        String role;
        String content;
    }
}