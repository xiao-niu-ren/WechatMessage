package com.xzy.wechatmsg.manager.task;

import com.alibaba.fastjson.JSON;
import com.xzy.wechatmsg.exception.task.NoSuchMsgTypeException;
import com.xzy.wechatmsg.bo.WechatMsgWithInfoAndType;
import com.xzy.wechatmsg.client.WechatRobotClient;
import com.xzy.wechatmsg.enums.WechatMsgTypeEnum;
import com.xzy.wechatmsg.exception.task.TaskInvokeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    WechatRobotClient wechatClient;

    private static final Map<WechatMsgTypeEnum, String> msgTypeMethodNameMap = new HashMap<WechatMsgTypeEnum, String>() {{
        put(WechatMsgTypeEnum.TEXT_MSG, "sendTextMsg");
        put(WechatMsgTypeEnum.IMG_MSG, "sendImgMsg");
        put(WechatMsgTypeEnum.AT_MSG, "sendAtMsg");
        put(WechatMsgTypeEnum.ANNEX_MSG, "sendAnnex");
        put(WechatMsgTypeEnum.CUSTOM_METHOD_TXT_MSG, "sendTextMsgWithCustomMethod");
    }};

    /**
     * @param msg WechatMsgWithType的JSON字符串
     * @return 可运行的app任务
     */
    public Runnable createRunnable(String msg) {
        //获取不同type的method
        WechatMsgWithInfoAndType wechatMsgWithInfoAndType = JSON.parseObject(msg, WechatMsgWithInfoAndType.class);
        String methodName = msgTypeMethodNameMap.get(wechatMsgWithInfoAndType.getMsgType());
        //反射获取Runnable
        Runnable res = () -> {};
        try {
            Method method = wechatClient.getClass().getDeclaredMethod(methodName, WechatMsgWithInfoAndType.WechatMsg.class);
            method.setAccessible(true);
            WechatMsgWithInfoAndType.WechatMsg wechatMsg = wechatMsgWithInfoAndType.getMsgInfo();
            res = () -> {
                try {
                    method.invoke(wechatClient, JSON.parseObject(JSON.toJSONString(wechatMsg), WechatMsgWithInfoAndType.WechatMsg.class));
                } catch (Exception e) {
                    throw new TaskInvokeException();
                }
            };
        } catch (Exception e) {
            throw new NoSuchMsgTypeException();
        }
        return res;
    }
}
