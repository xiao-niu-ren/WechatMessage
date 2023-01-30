package com.xzy.wechatmsg.manager.robot;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.domain.robot.model.WechatMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvPicMsg;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvTxtMsg;
import org.springframework.stereotype.Component;

/**
 * @description: RobotMsgHandler
 * @author: Xie zy
 * @create: 2023.01.31
 */
@Component
public class RobotMsgHandler {

    public WechatMsgDTO buildWechatMsg(WechatMsgWithInfoAndType.WechatMsg msg) {
        return JSON.parseObject(JSON.toJSONString(msg), WechatMsgDTO.class);
    }

    public WechatRsvTxtMsg parseTxtMsg(WechatRsvMsgDTO msg) {
        return JSON.parseObject(JSON.toJSONString(msg), WechatRsvTxtMsg.class);
    }

    public WechatRsvPicMsg parsePicMsg(WechatRsvMsgDTO msg) {
        //TODO
        return null;
    }

    public String dealAtGroupTxtMsg(String txt) {
        //TODO
        return null;
    }

    public String dealNormalGroupTxtMsg(String txt) {
        //TODO
        return null;
    }

    public String dealPrivateTxtMsg(String txt) {
        //TODO
        return null;
    }
}
