package com.xzy.wechatmsg.controller;

import com.xzy.wechatmsg.domain.robot.model.WechatRsvMsgDTO;
import com.xzy.wechatmsg.manager.robot.RobotMsgHandler;
import com.xzy.wechatmsg.service.RobotMsgHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    RobotMsgHandleService robotMsgHandleService;

    @Autowired
    RobotMsgHandler robotMsgHandler;

    @RequestMapping("/txt")
    public void handleTxt(@RequestBody WechatRsvMsgDTO rsvMsg) {
        robotMsgHandleService.handleTxtMsg(rsvMsg);
    }

    @RequestMapping("/pic")
    public void handlePic(@RequestBody WechatRsvMsgDTO rsvMsg) {
        robotMsgHandleService.handlePicMsg(rsvMsg);
    }

    @RequestMapping("/testOpenai")
    public String handlePic(@RequestParam String input) {
        String chat = robotMsgHandler.chat(input, false);
        return chat;
    }

}
