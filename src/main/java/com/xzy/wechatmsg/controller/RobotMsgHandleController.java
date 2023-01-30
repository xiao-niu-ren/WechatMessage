package com.xzy.wechatmsg.controller;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.client.WechatRobotClient;
import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.service.ScheduleTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: RobotMsgHandleController
 * @author: Xie zy
 * @create: 2023.01.30
 */
@RequestMapping("/wechat-rsv-msg")
@RestController
public class RobotMsgHandleController {

    @Autowired
    ScheduleTaskService scheduleTaskService;

    @Autowired
    WechatRobotClient wechatClient;

    @RequestMapping("/txt")
    public void handleTxt(@RequestBody WechatRsvMsgDTO rsvMsg) {
        System.out.println("收到txt");
        wechatClient.sendToXiaoniuren(JSON.toJSONString(rsvMsg));
    }

    @RequestMapping("/pic")
    public void handlePic(@RequestBody WechatRsvMsgDTO rsvMsg) {
        System.out.println("收到pic");
        wechatClient.sendToXiaoniuren(JSON.toJSONString(rsvMsg));
    }

}
