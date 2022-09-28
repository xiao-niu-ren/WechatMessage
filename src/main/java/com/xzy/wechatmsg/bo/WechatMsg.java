package com.xzy.wechatmsg.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @description: WechatMsg
 * @author: Xie zy
 * @create: 2022.09.29
 */
@Data
public class WechatMsg {
    @JsonProperty("wxid")
    String wxId;
    @JsonProperty("content")
    String content;
    @JsonProperty("roomid")
    String roomId;
    @JsonProperty("nickname")
    String nickName;
}
