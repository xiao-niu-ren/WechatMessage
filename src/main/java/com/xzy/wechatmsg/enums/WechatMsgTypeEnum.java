package com.xzy.wechatmsg.enums;

/**
 * @description: WechatMsgTypeEnum
 * @author: Xie zy
 * @create: 2022.09.29
 */
public enum WechatMsgTypeEnum {
    /**
     * text
     */
    TEXT_MSG("text_msg"),
    /**
     * img
     */
    IMG_MSG("img_msg"),
    /**
     * at
     */
    AT_MSG("at_msg"),
    /**
     * annex
     */
    ANNEX_MSG("annex_msg"),
    ;

    String value;

    WechatMsgTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static WechatMsgTypeEnum findByValue(String value) {
        for (WechatMsgTypeEnum wechatMsgTypeEnum : WechatMsgTypeEnum.values()) {
            if (wechatMsgTypeEnum.value.equals(value)) {
                return wechatMsgTypeEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "WechatMsgTypeEnum{" +
                "value='" + value + '\'' +
                '}';
    }
}
