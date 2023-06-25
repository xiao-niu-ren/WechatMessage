package com.xzy.wechatmsg.domain.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: ChatGptResp
 * @author: Xie zy
 * @create: 2023.02.02
 */
@Data
@NoArgsConstructor
public class ChatGptResp30 {
    String id;
    String object;
    Integer created;
    String model;
    List<Choice> choices;
    Usage usage;

    @Data
    @NoArgsConstructor
    public static class Choice {
        String text;
        Integer index;
        List<Double> logprobs;
        @JsonProperty("finish_reason")
        String finishReason;
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
