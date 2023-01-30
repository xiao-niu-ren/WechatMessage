package com.xzy.wechatmsg.domain.robot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @description: WechatMsg
 * @author: Xie zy
 * @create: 2023.01.30
 */
@Data
public class WechatMsgDTO {
    /** 消息id */
    private String id;
    /** 接收消息人的 微信原始id */
    @JsonProperty("wxid")
    private String wxId;
    /** 消息内容 */
    private String content;
    /** 群组id 群组内发送@消息时使用 */
    @JsonProperty("roomid")
    private String roomId;
    /** 发送消息类型 */
    private Integer type;
    /** 昵称 */
    @JsonProperty("nickname")
    private String nickName;
    /** 图片消息的图片地址(绝对路径 D:/xxx.jpg) */
    private String path;
    private String ext;
}
