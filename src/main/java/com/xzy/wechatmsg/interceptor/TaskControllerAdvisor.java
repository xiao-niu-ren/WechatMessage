package com.xzy.wechatmsg.interceptor;

import com.xzy.wechatmsg.exception.task.NoSuchMsgTypeException;
import com.xzy.wechatmsg.exception.task.NoSuchTaskIdException;
import com.xzy.wechatmsg.vo.BaseResponseVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description: TaskControllerAdvisor
 * @author: Xie zy
 * @create: 2022.09.29
 */
@ControllerAdvice
public class TaskControllerAdvisor {

    @ExceptionHandler(NoSuchTaskIdException.class)
    public BaseResponseVO noSuchTaskIdException(NoSuchTaskIdException e) {
        return BaseResponseVO.error();
    }

    @ExceptionHandler(NoSuchMsgTypeException.class)
    public BaseResponseVO noSuchMsgTypeException(NoSuchMsgTypeException e) {
        return BaseResponseVO.error();
    }

}
