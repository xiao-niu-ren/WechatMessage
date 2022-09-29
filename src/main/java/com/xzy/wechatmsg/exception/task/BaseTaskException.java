package com.xzy.wechatmsg.exception.task;

import com.xzy.wechatmsg.constant.ExceptionConstants;

/**
 * @description: BaseTaskException
 * @author: Xie zy
 * @create: 2022.09.02
 */
public class BaseTaskException extends RuntimeException {
    public BaseTaskException() {
        super(ExceptionConstants.BASE_TASK_EXCEPTION_MSG);
    }

    public BaseTaskException(String msg) {
        super(msg);
    }
}
