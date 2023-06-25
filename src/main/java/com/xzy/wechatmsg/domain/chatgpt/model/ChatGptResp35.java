package com.xzy.wechatmsg.domain.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: ChatGptResp35
 * @author: Xie zy
 * @create: 2023.05.19
 */
@Data
@NoArgsConstructor
public class ChatGptResp35 {
    String id;
    String object;
    Integer created;
    String model;
    List<Choice> choices;
    Usage usage;

    @Data
    @NoArgsConstructor
    public static class Choice {
        Integer index;
        Message message;
        @JsonProperty("finish_reason")
        String finishReason;
    }

    @Data
    @NoArgsConstructor
    public static class Message {
        String role;
        String content;
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        @JsonProperty("prompt_tokens")
        Integer promptTokens;
        @JsonProperty("completion_tokens")
        Integer completionTokens;
        @JsonProperty("total_tokens")
        Integer totalTokens;
    }
}
