package com.xzy.wechatmsg.manager.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.ExceptionConst;
import com.xzy.wechatmsg.bo.WrappedCronTask;
import com.xzy.wechatmsg.domain.task.repository.TaskRepository;
import com.xzy.wechatmsg.enums.TaskTypeEnum;
import com.xzy.wechatmsg.exception.task.NoSuchTaskIdException;
import com.xzy.wechatmsg.client.WechatClient;
import com.xzy.wechatmsg.config.ScheduleConfig;
import com.xzy.wechatmsg.domain.task.model.Task;
import com.xzy.wechatmsg.enums.TaskStatusEnum;
import com.xzy.wechatmsg.mapper.TaskMapper;
import com.xzy.wechatmsg.request.task.TaskRequest;
import com.xzy.wechatmsg.vo.task.TaskResponseVO;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @description: AppTaskHandler
 * @author: Xie zy
 * @create: 2022.09.01
 */
@Component
public class AppTaskHandler extends AbstractTaskHandler {

    private static List<WrappedCronTask> wrappedCronTaskList = new CopyOnWriteArrayList<>();

    private static Map<String, ScheduledTask> scheduledCronTaskMap = new ConcurrentHashMap<>();

    @Autowired
    ScheduleConfig scheduleConfig;

    @Autowired
    WechatClient wechatClient;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public void createTasks(TaskRequest taskRequest) {
        //查db，查不到报错
        Task dbTask = taskRepository.checkTaskId(taskRequest.getTaskId());
        //查到去更新db状态 && 双写app
        if (TaskStatusEnum.TASK_STOP.getValue().equals(dbTask.getStatus())) {
            //获取db数据
            Integer taskId = dbTask.getId();
            String msg = dbTask.getMsg();
            String cron = dbTask.getCron();
            LocalDateTime createTime = dbTask.getCreateTime();
            //启动appTask需要更新updateTime
            LocalDateTime now = LocalDateTime.now();
            //1.更新数据库状态和updateTime
            taskMapper.updateStatusWithUpdateTime(dbTask.getId(), TaskStatusEnum.TASK_STOP.getValue(),TaskStatusEnum.TASK_RUNNING.getValue(),now);
            //2.创建app任务
            Runnable runnable = () -> wechatClient.sendToXiaoniuren(dbTask.getMsg());
            //双写app任务
            this.addWrappedCronTask(new WrappedCronTask(taskId, msg, cron, TaskStatusEnum.TASK_RUNNING.getValue(),createTime,now,runnable), false);
        }
    }

    @Override
    public List<TaskResponseVO.TaskVO> readTasks() {
        //useless method
        return null;
    }

    @Override
    public void updateTasks(TaskRequest taskRequest) {
        //找不到id上游已经判断了，所以直接删除appTask即可
        this.removeWrappedCronTask(taskRequest.getTaskId());
        //获取上下文
        Integer taskId = taskRequest.getTaskId();
        String msg = taskRequest.getMsg();
        String cron = taskRequest.getCron();
        LocalDateTime createTime = taskRequest.getCreateTime();
        //找不到id上游已经判断了，所以直接运行app即可
        LocalDateTime now = LocalDateTime.now();
        //1.更新数据库状态和updateTime
        taskMapper.updateStatusWithUpdateTime(taskId,TaskStatusEnum.TASK_STOP.getValue(),TaskStatusEnum.TASK_RUNNING.getValue(),now);
        //2.创建app任务
        Runnable runnable = () -> wechatClient.sendToXiaoniuren(msg);
        //双写app任务
        this.addWrappedCronTask(new WrappedCronTask(taskId, msg, cron, TaskStatusEnum.TASK_RUNNING.getValue(),createTime,now,runnable), false);
    }

    @Override
    public void deleteTasks(TaskRequest taskRequest) {
        //type = app需要查询数据库，并改状态
        if(TaskTypeEnum.TASK_TYPE_APP.getValue().equals(taskRequest.getType())){
            //查db，查不到报错
            taskRepository.checkTaskId(taskRequest.getTaskId());
            //查到更新状态为stop
            taskMapper.updateStatusWithOutUpdateTime(taskRequest.getTaskId(),TaskStatusEnum.TASK_RUNNING.getValue(), TaskStatusEnum.TASK_STOP.getValue());
        }
        //type = db，上游已经删除了，故肯定查不到
        //停止app任务
        this.removeWrappedCronTask(taskRequest.getTaskId());
    }

    public List<WrappedCronTask> getWrappedCronTaskList() {
        return AppTaskHandler.wrappedCronTaskList;
    }

    public Map<String, ScheduledTask> getScheduledCronTaskMap() {
        return AppTaskHandler.scheduledCronTaskMap;
    }

    public void shutdownAllAppTask() {
        //拉闸App，双拉
        //1.拉闸wrappedList
        this.getWrappedCronTaskList().forEach(wrappedCronTask -> {
            taskMapper.updateStatusWithOutUpdateTime(wrappedCronTask.getTaskId(), TaskStatusEnum.TASK_RUNNING.getValue(),TaskStatusEnum.TASK_STOP.getValue());
        });
        //清空数据
        this.getWrappedCronTaskList().clear();
        //2.拉闸cronMap
        shutdownAllCronTask();
    }

    @Override
    public void refreshApp() {
        //刷新App，使App的运行状态和数据库一致
        //拉闸App
        shutdownAllAppTask();
        //对于数据库中Running的，重新注册运行appTask
        LambdaQueryWrapper<Task> wrapper = new QueryWrapper<Task>().lambda();
        wrapper.eq(Task::getStatus, TaskStatusEnum.TASK_RUNNING.getValue());
        List<Task> dbRunningTasks = taskMapper.selectList(wrapper);
        dbRunningTasks.forEach(dbRunningTask -> {
            Integer taskId = dbRunningTask.getId();
            String msg = dbRunningTask.getMsg();
            String cron = dbRunningTask.getCron();
            Integer status = dbRunningTask.getStatus();
            LocalDateTime createTime = dbRunningTask.getCreateTime();
            LocalDateTime updateTime = dbRunningTask.getUpdateTime();
            Runnable runnable = () -> wechatClient.sendToXiaoniuren(msg);
            this.addWrappedCronTask(new WrappedCronTask(taskId, msg, cron, status,createTime,updateTime,runnable), false);
        });
    }

    @PreDestroy
    public void shutdownAppAfterSpringBoot(){
        //基本不用的方法，SpringBoot容器关闭后执行
        shutdownAllAppTask();
    }

    public void addWrappedCronTask(WrappedCronTask wrappedCronTask, boolean onlyWrapped) {
        //双加
        //1.加wrappedList
        this.getWrappedCronTaskList().add(wrappedCronTask);
        if (!onlyWrapped) {
            //2.启动并加入cronMap
            Runnable runnable = wrappedCronTask.getRunnable();
            String cron = wrappedCronTask.getCron();
            ScheduledTask scheduledCronTask = this.addCronTask(new CronTask(runnable, cron));
            scheduledCronTaskMap.put(wrappedCronTask.getTaskId().toString(), scheduledCronTask);
        }
    }

    public void removeWrappedCronTask(Integer taskId) {
        //如果有这个taskId就删除这个App Task
        //双删
        //1.删除wrappedTask
        List<WrappedCronTask> wrappedCronTaskList = getWrappedCronTaskList().stream()
                .filter(wrappedCronTask -> wrappedCronTask.getTaskId().equals(taskId))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(wrappedCronTaskList)) return;
        getWrappedCronTaskList().remove(wrappedCronTaskList.get(0));
        //2.删除Task
        this.removeCronTask(taskId);
    }

    private ScheduledTask addCronTask(CronTask cronTask) {
        ScheduledTask scheduledCronTask = scheduleConfig.getScheduledTaskRegistrar().scheduleCronTask(cronTask);
        return scheduledCronTask;
    }

    private void removeCronTask(Integer taskId) {
        //cancel && remove
        this.getScheduledCronTaskMap().get(taskId.toString()).cancel();
        this.getScheduledCronTaskMap().remove(taskId.toString());
    }

    private void shutdownAllCronTask() {
        //cancel && clear
        this.getScheduledCronTaskMap().values().forEach(ScheduledTask::cancel);
        this.getScheduledCronTaskMap().clear();
    }

}
