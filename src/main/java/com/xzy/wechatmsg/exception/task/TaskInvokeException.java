package com.xzy.wechatmsg.exception.task;

import com.xzy.wechatmsg.constant.ExceptionConstants;

/**
 * @description: TaskInvokeException
 * @author: Xie zy
 * @create: 2022.09.29
 */
public class TaskInvokeException extends BaseTaskException{
    public TaskInvokeException(){
        super(ExceptionConstants.TASK_INVOKE_FAILED);
    }
}
