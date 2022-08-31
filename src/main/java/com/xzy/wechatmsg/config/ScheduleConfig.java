package com.xzy.wechatmsg.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzy.wechatmsg.bo.WrappedCronTask;
import com.xzy.wechatmsg.client.WechatClient;
import com.xzy.wechatmsg.domain.task.model.Task;
import com.xzy.wechatmsg.enums.TaskStatusEnum;
import com.xzy.wechatmsg.manager.task.AppTaskHandler;
import com.xzy.wechatmsg.mapper.TaskMapper;
import com.xzy.wechatmsg.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: ScheduleConfig
 * @author: Xie zy
 * @create: 2022.08.31
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Autowired
    WechatClient wechatClient;

    @Autowired
    TaskMapper taskMapper;

    private volatile ScheduledTaskRegistrar taskRegistrar;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ScheduledTaskRegistrar getScheduledTaskRegistrar(){
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.taskRegistrar;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());

        //查询数据库中Running状态任务
        LambdaQueryWrapper<Task> wrapper = new QueryWrapper<Task>().lambda();
        wrapper.eq(Task::getStatus, TaskStatusEnum.TASK_RUNNING.getValue());
        List<Task> dbRunningTasks = taskMapper.selectList(wrapper);
        AppTaskHandler appTaskHandler = new AppTaskHandler();
        //初始化
        dbRunningTasks.forEach(dbRunningTask -> {
            Integer id = dbRunningTask.getId();
            String msg = dbRunningTask.getMsg();
            String cron = dbRunningTask.getCron();
            Integer status = dbRunningTask.getStatus();
            LocalDateTime createTime = dbRunningTask.getCreateTime();
            LocalDateTime updateTime = dbRunningTask.getUpdateTime();
            Runnable runnable = () -> wechatClient.sendToXiaoniuren(msg);
            //把updateTime小于2小时的过滤掉，并更新成stop
            if(TimeUtils.isUpdateTimeExpired(updateTime)){
                taskMapper.updateStatusWithOutUpdateTime(id,TaskStatusEnum.TASK_RUNNING.getValue(),TaskStatusEnum.TASK_STOP.getValue());
                return;
            }
            //AppTask双写
            LocalDateTime now = LocalDateTime.now();
            appTaskHandler.addWrappedCronTask(new WrappedCronTask(id,msg,cron,status,createTime,updateTime,runnable),true);
            //把数据库中的Running的交给scheduledCronTaskMap管理，后续可以cancel
            ScheduledTask scheduledTask = taskRegistrar.scheduleCronTask(new CronTask(runnable,cron));
            appTaskHandler.getScheduledCronTaskMap().put(id.toString(), scheduledTask);
        });
        //应用启动时增加刷新updateTime的cronTask，每小时刷一次，这个不用双写，因为不需要wrapped管理
        HashMap<Runnable, String> cronTasks = new HashMap<>();
         cronTasks.put(() -> {
            appTaskHandler.getWrappedCronTaskList().forEach((wrappedCronTask) -> {
                taskMapper.updateStatusWithUpdateTime(wrappedCronTask.getTaskId(),TaskStatusEnum.TASK_RUNNING.getValue(),TaskStatusEnum.TASK_RUNNING.getValue(),LocalDateTime.now());
            });
        },"0 0 * * * ?");
        taskRegistrar.setCronTasks(cronTasks);

        this.taskRegistrar = taskRegistrar;
        countDownLatch.countDown();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setThreadNamePrefix("spring-task-scheduler-thread-");
        taskScheduler.setAwaitTerminationSeconds(60);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return taskScheduler;
    }

}
