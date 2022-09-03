package com.xzy.wechatmsg.manager.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xzy.wechatmsg.domain.task.model.Task;
import com.xzy.wechatmsg.domain.task.repository.TaskRepository;
import com.xzy.wechatmsg.enums.TaskStatusEnum;
import com.xzy.wechatmsg.mapper.TaskMapper;
import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: DbTaskHandler
 * @author: Xie zy
 * @create: 2022.09.01
 */
@Component
public class DbTaskHandler extends AbstractTaskHandler{

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public void createTasks(TaskRequest taskRequest) {
        LocalDateTime now = LocalDateTime.now();
        Task task = Task.builder()
                .cron(taskRequest.getCron())
                .msg(taskRequest.getMsg())
                .status(TaskStatusEnum.TASK_STOP.getValue())
                .createTime(now)
                .updateTime(now)
                .build();
        taskMapper.insert(task);
        Integer insertId = task.getId();
        //把主键带出来给后续方法（App#createTasks）
        taskRequest.setTaskId(insertId);
    }

    @Override
    public List<TaskResponseVO.TaskVO> readTasks() {
        List<Task> tasks = taskMapper.selectList(new QueryWrapper<>());
        List<TaskResponseVO.TaskVO> res = new ArrayList<>();
        tasks.forEach( task -> {
            TaskResponseVO.TaskVO taskVO = JSON.parseObject(JSON.toJSONString(task), TaskResponseVO.TaskVO.class);
            taskVO.setTaskId(task.getId());
            res.add(taskVO);
        });
        return res;
    }

    @Override
    public void updateTasks(TaskRequest taskRequest) {
        //去db中查询有没有这个id，没有就报错
        Task dbTask = taskRepository.checkTaskId(taskRequest.getTaskId());
        //转换前端参数
        //对前端传进来的进行属性填充，前端一般不会传""过来，空的msg也没意义，不如直接shutdown任务
        if(!StringUtils.isEmpty(taskRequest.getMsg())) dbTask.setMsg(taskRequest.getMsg());
        if(!StringUtils.isEmpty(taskRequest.getCron())) dbTask.setCron(taskRequest.getCron());
        //更新的设为stop
        dbTask.setStatus(TaskStatusEnum.TASK_STOP.getValue());
        LambdaUpdateWrapper<Task> updateWrapper = new UpdateWrapper<Task>().lambda();
        updateWrapper.eq(Task::getId,dbTask.getId());
        taskMapper.update(dbTask, updateWrapper);
        //传给下游方法
        taskRequest.setMsg(dbTask.getMsg());
        taskRequest.setCron(dbTask.getCron());
        taskRequest.setCreateTime(dbTask.getCreateTime());
    }

    @Override
    public void deleteTasks(TaskRequest taskRequest) {
        //去db中查询有没有这个id，没有就报错
        taskRepository.checkTaskId(taskRequest.getTaskId());
        //有的话就删除
        LambdaQueryWrapper<Task> queryWrapper = new QueryWrapper<Task>().lambda();
        queryWrapper.eq(Task::getId,taskRequest.getTaskId());
        taskMapper.delete(queryWrapper);
    }
}
