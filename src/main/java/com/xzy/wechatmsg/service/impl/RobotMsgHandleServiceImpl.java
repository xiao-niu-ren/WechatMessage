package com.xzy.wechatmsg.service.impl;

import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.client.WechatRobotClient;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvPicMsg;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvTxtMsg;
import com.xzy.wechatmsg.manager.robot.RobotMsgHandler;
import com.xzy.wechatmsg.service.RobotMsgHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
        CompletableFuture.runAsync(() -> {
            WechatRsvTxtMsg wechatRsvTxtMsg = robotMsgHandler.parseTxtMsg(rsvMsg);
            String wxid = wechatRsvTxtMsg.getWxid();
            if (wxid.endsWith("@chatroom")) {
                if (wechatRsvTxtMsg.getContent().contains("@NiuBot")) {
                    //1.群聊@消息
                    String roomId = wxid;
                    String userId = wechatRsvTxtMsg.getId1();
                    WechatMsgWithInfoAndType.WechatMsg wechatMsg = new WechatMsgWithInfoAndType.WechatMsg();
                    String content = robotMsgHandler.dealAtGroupTxtMsg(wechatRobotClient, roomId, userId, wechatRsvTxtMsg.getContent().replace("@NiuBot", ""));
                    wechatMsg.setContent(content);
                    wechatMsg.setWxId(roomId);
                    wechatRobotClient.sendTextMsg(wechatMsg);
                } else {
                //2.普通群聊消息
                //默认关闭，当@机器人开启自动回复以后，@机器人关闭自动回复，保留@回复
                String roomId = wxid;
                String wxId = wechatRsvTxtMsg.getId1();
                if (!RobotMsgHandler.autoRespRoomSet.contains(roomId) || !RobotMsgHandler.autoRespMap.containsKey(roomId) || !RobotMsgHandler.autoRespMap.get(roomId).contains(wxId)) {
                    return;
                }
                WechatMsgWithInfoAndType.WechatMsg wechatMsg = new WechatMsgWithInfoAndType.WechatMsg();
                wechatMsg.setContent(robotMsgHandler.dealNormalGroupTxtMsg(wechatRsvTxtMsg.getContent()));
                wechatMsg.setWxId(roomId);
                wechatRobotClient.sendTextMsg(wechatMsg);
                }
            } else {
                //3.私聊消息
                WechatMsgWithInfoAndType.WechatMsg wechatMsg = new WechatMsgWithInfoAndType.WechatMsg();
                String content = robotMsgHandler.dealPrivateTxtMsg(wechatRsvTxtMsg.getContent());
                wechatMsg.setContent(content);
                wechatMsg.setWxId(wxid);
                wechatRobotClient.sendTextMsg(wechatMsg);
            }
        });
    }

    @Override
    public void handlePicMsg(WechatRsvMsgDTO rsvMsg) {
        CompletableFuture.runAsync(() -> {
            WechatRsvPicMsg wechatRsvPicMsg = robotMsgHandler.parsePicMsg(rsvMsg);
            //TODO 分私聊/群聊/艾特
        });
    }
}
