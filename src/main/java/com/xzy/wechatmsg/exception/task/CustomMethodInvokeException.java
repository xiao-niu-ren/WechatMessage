package com.xzy.wechatmsg.exception.task;

import com.xzy.wechatmsg.constant.ExceptionConstants;

/**
 * @description: CustomMethodInvokeException
 * @author: Xie zy
 * @create: 2023.09.07
 */
public class CustomMethodInvokeException extends BaseTaskException {
    public CustomMethodInvokeException() {
        super(ExceptionConstants.CUSTOM_METHOD_INVOKE_EXCEPTION);
    }
}
