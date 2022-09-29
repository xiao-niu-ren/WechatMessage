package com.xzy.wechatmsg.manager.task;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.bo.WechatMsg;
import com.xzy.wechatmsg.bo.WechatMsgWithType;
import com.xzy.wechatmsg.client.WechatClient;
import com.xzy.wechatmsg.constant.ExceptionConstants;
import com.xzy.wechatmsg.enums.WechatMsgTypeEnum;
import com.xzy.wechatmsg.exception.task.TaskInvokeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: RunnableWechatMsgFactory
 * @author: Xie zy
 * @create: 2022.09.29
 */
@Component
public class RunnableWechatMsgFactory {

    @Autowired
    WechatClient wechatClient;

    private static final Map<WechatMsgTypeEnum, String> msgTypeMethodNameMap = new HashMap<WechatMsgTypeEnum, String>(){{
        put(WechatMsgTypeEnum.TEXT_MSG,"sendTextMsg");
        put(WechatMsgTypeEnum.IMG_MSG,"sendImgMsg");
        put(WechatMsgTypeEnum.AT_MSG,"sendAtMsg");
        put(WechatMsgTypeEnum.ANNEX_MSG,"sendAnnex");
    }};

    public Runnable createRunnable(String msg) {
        WechatMsgWithType wechatMsgWithType = JSON.parseObject(msg, WechatMsgWithType.class);
        String methodName = msgTypeMethodNameMap.get(wechatMsgWithType.getMsgType());
        Runnable res = () -> {};
        try {
            Method method = wechatClient.getClass().getDeclaredMethod(methodName,WechatMsg.class);
            method.setAccessible(true);
            String wechatMsg = wechatMsgWithType.getMsgInfo();
            res = () -> {
                try {
                    method.invoke(wechatClient,JSON.parseObject(wechatMsg,WechatMsg.class));
                } catch (Exception e) {
                    throw new TaskInvokeException();
                }
            };
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return res;
    }
}
