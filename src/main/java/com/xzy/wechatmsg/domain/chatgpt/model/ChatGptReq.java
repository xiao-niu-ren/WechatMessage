package com.xzy.wechatmsg.domain.chatgpt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: Body
 * @author: Xie zy
 * @create: 2023.02.02
 */
@Data
@NoArgsConstructor
public class ChatGptReq {
    String model;
    String prompt;
    Integer max_tokens;
    Float temperature;
}
