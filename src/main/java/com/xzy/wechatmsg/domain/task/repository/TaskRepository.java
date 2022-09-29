package com.xzy.wechatmsg.domain.task.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzy.wechatmsg.domain.task.model.Task;
import com.xzy.wechatmsg.exception.task.NoSuchTaskIdException;
import com.xzy.wechatmsg.domain.task.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description: TaskRepository
 * @author: Xie zy
 * @create: 2022.09.03
 */
@Repository
public class TaskRepository {

    @Autowired
    TaskMapper taskMapper;

    public Task checkTaskId(Integer taskId) {
        //去db中查询有没有这个id，没有就报错，有就传来db中的task
        LambdaQueryWrapper<Task> queryWrapper = new QueryWrapper<Task>().lambda();
        queryWrapper.eq(Task::getId, taskId);
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tasks)) {
            throw new NoSuchTaskIdException();
        }
        return tasks.get(0);
    }
}
