package com.xzy.wechatmsg.exception.task;

import com.xzy.wechatmsg.constant.ExceptionConstants;

/**
 * @description: NoSuchTaskIdException
 * @author: Xie zy
 * @create: 2022.09.02
 */
public class NoSuchTaskIdException extends BaseTaskException {
    public NoSuchTaskIdException() {
        super(ExceptionConstants.NO_SUCH_TASK_ID_EXCEPTION_MSG);
    }
}
