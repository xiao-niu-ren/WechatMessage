package com.xzy.wechatmsg.manager.task;

import com.xzy.wechatmsg.annotation.CronMethod;
import org.springframework.stereotype.Component;

/**
 * @description: CustomMethod
 * @author: Xie zy
 * @create: 2023.09.07
 */
@Component
public class CustomMethodHandler extends BaseCustomMethodHandler {

    @CronMethod
    public void test(String param) {
        sendToXiaoniuren(param);
    }

}
