package com.xzy.wechatmsg.controller;

import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.service.ScheduleTaskService;
import com.xzy.wechatmsg.vo.BaseResponseVO;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: ScheduleTaskController
 * @author: Xie zy
 * @create: 2022.08.31
 */
@RequestMapping("/schedule-task")
@RestController
public class ScheduleTaskController {

    @Autowired
    ScheduleTaskService scheduleTaskService;

    @RequestMapping("/create")
    public BaseResponseVO createTasks(@RequestBody TaskRequest taskRequest) {
        scheduleTaskService.createTasks(taskRequest);
        return BaseResponseVO.success();
    }

    @RequestMapping("/read")
    public BaseResponseVO readTasks() {
        List<TaskResponseVO.TaskVO> taskVOS = scheduleTaskService.readTasks();
        return TaskResponseVO.success(taskVOS);
    }

    @RequestMapping("/update")
    public BaseResponseVO updateTasks(@RequestBody TaskRequest taskRequest) {
        scheduleTaskService.updateTasks(taskRequest);
        return BaseResponseVO.success();
    }

    @RequestMapping("/delete")
    public BaseResponseVO deleteTasks(@RequestBody TaskRequest taskRequest) {
        scheduleTaskService.deleteTasks(taskRequest);
        return BaseResponseVO.success();
    }

    /**让容器中的任务和数据库中保持一致
     *
     * @return vo
     */
    @RequestMapping("/refreshApp")
    public BaseResponseVO refreshApp() {
        scheduleTaskService.refreshApp();
        return TaskResponseVO.success();
    }

}
