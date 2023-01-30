package com.xzy.wechatmsg.service;

import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;

/**
 * @description: RobotMsgHandleService
 * @author: Xie zy
 * @create: 2023.01.30
 */
public interface RobotMsgHandleService {

    void handleTxtMsg(WechatRsvMsgDTO rsvMsg);

    void handlePicMsg(WechatRsvMsgDTO rsvMsg);

}
