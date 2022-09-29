package com.xzy.wechatmsg.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xzy.wechatmsg.enums.WechatMsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: WechatMsgWithType
 * @author: Xie zy
 * @create: 2022.09.29
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WechatMsgWithType {
    @JsonProperty("msg_info")
    String msgInfo;
    @JsonProperty("msg_type")
    WechatMsgTypeEnum msgType;
}
