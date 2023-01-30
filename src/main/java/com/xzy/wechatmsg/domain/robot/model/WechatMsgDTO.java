package com.xzy.wechatmsg.domain.robot.model;

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
    private String wxid;
    /** 消息内容 */
    private String content;
    /** 群组id 群组内发送@消息时使用 */
    private String roomid;
    /** 发送消息类型 */
    private Integer type;
    /** 昵称 */
    private String nickname;
    /** 图片消息的图片地址(绝对路径 D:/xxx.jpg) */
    private String path;
    private String ext;
}
