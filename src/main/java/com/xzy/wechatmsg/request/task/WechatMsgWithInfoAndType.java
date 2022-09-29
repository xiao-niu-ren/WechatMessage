package com.xzy.wechatmsg.request.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xzy.wechatmsg.enums.WechatMsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: WechatMsgWithInfoAndType
 * @author: Xie zy
 * @create: 2022.09.29
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WechatMsgWithInfoAndType {
    @JsonProperty("msg_info")
    WechatMsg msgInfo;
    @JsonProperty("msg_type")
    WechatMsgTypeEnum msgType;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class WechatMsg {
        @JsonProperty("wxid")
        String wxId;
        @JsonProperty("content")
        String content;
        @JsonProperty("roomid")
        String roomId;
        @JsonProperty("nickname")
        String nickName;
    }
}
