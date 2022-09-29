package com.xzy.wechatmsg.manager.task;

import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: AbstractTaskHandler
 * @author: Xie zy
 * @create: 2022.09.01
 */
@Component
public abstract class AbstractTaskHandler {

    public abstract void createTasks(TaskRequest taskRequest);

    public abstract List<TaskResponseVO.TaskVO> readTasks();

    public abstract void updateTasks(TaskRequest taskRequest);

    public abstract void deleteTasks(TaskRequest taskRequest);

    public void refreshApp() {}

}
