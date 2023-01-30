package com.xzy.wechatmsg.service.impl;

import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.client.WechatRobotClient;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvTxtMsg;
import com.xzy.wechatmsg.manager.robot.RobotMsgHandler;
import com.xzy.wechatmsg.service.RobotMsgHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: RobotMsgHandleServiceImpl
 * @author: Xie zy
 * @create: 2023.01.30
 */
@Service
public class RobotMsgHandleServiceImpl implements RobotMsgHandleService {

    @Autowired
    RobotMsgHandler robotMsgHandler;

    @Autowired
    WechatRobotClient wechatRobotClient;

    @Override
    public void handleTxtMsg(WechatRsvMsgDTO rsvMsg) {
        WechatRsvTxtMsg wechatRsvTxtMsg = robotMsgHandler.parseTxtMsg(rsvMsg);
        String wxid = wechatRsvTxtMsg.getWxid();
        if (wxid.endsWith("@chatroom")) {
            if (wechatRsvTxtMsg.getContent().contains("@NiuBot")) {
                //1.群聊@消息
                WechatMsgWithInfoAndType.WechatMsg wechatMsg = new WechatMsgWithInfoAndType.WechatMsg();
                wechatMsg.setContent(robotMsgHandler.dealAtGroupTxtMsg(wechatRsvTxtMsg.getContent().replace("@NiuBot", "")));
                wechatMsg.setRoomId(wxid);
                wechatMsg.setWxId(wechatRsvTxtMsg.getId1());
                wechatRobotClient.sendAtMsg(wechatMsg);
            } else {
                //2.普通群聊消息
                WechatMsgWithInfoAndType.WechatMsg wechatMsg = new WechatMsgWithInfoAndType.WechatMsg();
                wechatMsg.setContent(robotMsgHandler.dealNormalGroupTxtMsg(wechatRsvTxtMsg.getContent()));
                wechatMsg.setRoomId(wxid);
                wechatMsg.setWxId(wechatRsvTxtMsg.getId1());
                wechatRobotClient.sendTextMsg(wechatMsg);
            }
        } else {
            //3.私聊消息
            WechatMsgWithInfoAndType.WechatMsg wechatMsg = new WechatMsgWithInfoAndType.WechatMsg();
            wechatMsg.setContent(robotMsgHandler.dealPrivateTxtMsg(wechatRsvTxtMsg.getContent()));
            wechatMsg.setWxId(wxid);
            wechatRobotClient.sendTextMsg(wechatMsg);
        }
    }

    @Override
    public void handlePicMsg(WechatRsvMsgDTO rsvMsg) {
        //TODO
    }
}
