package com.xzy.wechatmsg.service;

import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;

import java.util.List;
import java.util.Map;

/**
 * @description: ScheduleTaskService
 * @author: Xie zy
 * @create: 2022.08.31
 */
public interface ScheduleTaskService {

    public void createTasks(TaskRequest taskRequest);

    public List<TaskResponseVO.TaskVO> readTasks();

    public void updateTasks(TaskRequest taskRequest);

    public void deleteTasks(TaskRequest taskRequest);

    public void refreshApp();

}
