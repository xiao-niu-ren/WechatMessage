package com.xzy.wechatmsg.service;

import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;

import java.util.List;

/**
 * @description: ScheduleTaskService
 * @author: Xie zy
 * @create: 2022.08.31
 */
public interface ScheduleTaskService {

    void createTasks(TaskRequest taskRequest);

    List<TaskResponseVO.TaskVO> readTasks();

    void updateTasks(TaskRequest taskRequest);

    void deleteTasks(TaskRequest taskRequest);

    void refreshApp();

}
