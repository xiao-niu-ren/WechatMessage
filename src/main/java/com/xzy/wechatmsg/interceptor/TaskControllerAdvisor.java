package com.xzy.wechatmsg.interceptor;

import com.xzy.wechatmsg.constant.ExceptionConstants;
import com.xzy.wechatmsg.exception.task.NoSuchMsgTypeException;
import com.xzy.wechatmsg.exception.task.NoSuchTaskIdException;
import com.xzy.wechatmsg.vo.BaseResponseVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: TaskControllerAdvisor
 * @author: Xie zy
 * @create: 2022.09.29
 */
@RestControllerAdvice
public class TaskControllerAdvisor {

    @ExceptionHandler(Exception.class)
    public BaseResponseVO error(Exception e) {
        return BaseResponseVO.error();
    }

    @ExceptionHandler(NoSuchTaskIdException.class)
    public BaseResponseVO noSuchTaskIdException(NoSuchTaskIdException e) {
        return BaseResponseVO.error(ExceptionConstants.NO_SUCH_TASK_ID_EXCEPTION_MSG);
    }

    @ExceptionHandler(NoSuchMsgTypeException.class)
    public BaseResponseVO noSuchMsgTypeException(NoSuchMsgTypeException e) {
        return BaseResponseVO.error(ExceptionConstants.NO_SUCH_MSG_TYPE);
    }

}
