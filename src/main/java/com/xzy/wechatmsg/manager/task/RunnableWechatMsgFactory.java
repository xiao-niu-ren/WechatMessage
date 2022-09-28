package com.xzy.wechatmsg.manager.task;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.bo.WechatMsg;
import com.xzy.wechatmsg.client.WechatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: RunnableWechatMsgFactory
 * @author: Xie zy
 * @create: 2022.09.29
 */
@Component
public class RunnableWechatMsgFactory {

    @Autowired
    WechatClient wechatClient;

    public Runnable createRunnable(String msg) {
        return () -> wechatClient.sendToXiaoniuren(msg);
    }
}
