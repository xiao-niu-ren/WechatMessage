package com.xzy.wechatmsg.service.impl;

import com.xzy.wechatmsg.enums.TaskTypeEnum;
import com.xzy.wechatmsg.manager.task.AbstractTaskHandler;
import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.service.ScheduleTaskService;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: ScheduleTaskServiceImpl
 * @author: Xie zy
 * @create: 2022.08.31
 */
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

    @Autowired
    Map<String, AbstractTaskHandler> abstractTaskHandlerMap;

    @Override
    public void createTasks(TaskRequest taskRequest) {
        AbstractTaskHandler dbTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_DB.getHandler());
        AbstractTaskHandler appTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_APP.getHandler());
        if(TaskTypeEnum.TASK_TYPE_DB.getValue().equals(taskRequest.getType())){
            //db创建任务 && app启动任务
            dbTaskHandler.createTasks(taskRequest);
            appTaskHandler.createTasks(taskRequest);
        } else if(TaskTypeEnum.TASK_TYPE_APP.getValue().equals(taskRequest.getType())){
            //app启动任务
            appTaskHandler.createTasks(taskRequest);
        }
    }

    @Override
    public List<TaskResponseVO.TaskVO> readTasks() {
        AbstractTaskHandler dbTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_DB.getHandler());
        //读db
        return dbTaskHandler.readTasks();
    }

    @Override
    public void updateTasks(TaskRequest taskRequest) {
        AbstractTaskHandler dbTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_DB.getHandler());
        AbstractTaskHandler appTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_APP.getHandler());
        //更新db && 更新app
        dbTaskHandler.updateTasks(taskRequest);
        appTaskHandler.updateTasks(taskRequest);
    }

    @Override
    public void deleteTasks(TaskRequest taskRequest) {
        AbstractTaskHandler dbTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_DB.getHandler());
        AbstractTaskHandler appTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_APP.getHandler());
        if(TaskTypeEnum.TASK_TYPE_DB.getValue().equals(taskRequest.getType())){
            //db删除任务 && app删除任务
            dbTaskHandler.deleteTasks(taskRequest);
            appTaskHandler.deleteTasks(taskRequest);
        } else if(TaskTypeEnum.TASK_TYPE_APP.getValue().equals(taskRequest.getType())){
            //app结束任务
            appTaskHandler.deleteTasks(taskRequest);
        }
    }

    @Override
    public void refreshApp() {
        AbstractTaskHandler appTaskHandler = abstractTaskHandlerMap.get(TaskTypeEnum.TASK_TYPE_APP.getHandler());
        appTaskHandler.refreshApp();
    }
}
