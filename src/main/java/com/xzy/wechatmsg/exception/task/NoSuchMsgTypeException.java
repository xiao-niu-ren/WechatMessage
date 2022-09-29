package com.xzy.wechatmsg.exception.task;

import com.xzy.wechatmsg.constant.ExceptionConstants;

/**
 * @description: NoSuchMsgTypeException
 * @author: Xie zy
 * @create: 2022.09.29
 */
public class NoSuchMsgTypeException extends BaseTaskException {
    public NoSuchMsgTypeException() {
        super(ExceptionConstants.NO_SUCH_MSG_TYPE);
    }
}
