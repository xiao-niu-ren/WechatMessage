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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
            //等configureTasks方法中放入的注册器
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.taskRegistrar;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //注册器中配置定时任务的执行期，SpringBoot对@Configuration的类会特殊处理，所以加入的是本类中的执行期bean
        taskRegistrar.setTaskScheduler(taskScheduler());

        //查询数据库中Running状态任务
        LambdaQueryWrapper<Task> wrapper = new QueryWrapper<Task>().lambda();
        wrapper.eq(Task::getStatus, TaskStatusEnum.TASK_RUNNING.getValue());
        List<Task> dbRunningTasks = taskMapper.selectList(wrapper);
        //初始化
        AppTaskHandler appTaskHandler = new AppTaskHandler();
        dbRunningTasks.forEach(dbRunningTask -> {
            //取出数据库的值
            Integer id = dbRunningTask.getId();
            String msg = dbRunningTask.getMsg();
            String cron = dbRunningTask.getCron();
            Integer status = dbRunningTask.getStatus();
            LocalDateTime createTime = dbRunningTask.getCreateTime();
            LocalDateTime updateTime = dbRunningTask.getUpdateTime();
            //构造runnable
            Runnable runnable = () -> wechatClient.sendToXiaoniuren(msg);
            //把updateTime小于2小时的过滤掉，并更新成stop
            if(TimeUtils.isUpdateTimeExpired(updateTime)){
                taskMapper.updateStatusWithOutUpdateTime(id,TaskStatusEnum.TASK_RUNNING.getValue(),TaskStatusEnum.TASK_STOP.getValue());
                return;
            }
            //AppTask双写
            //1.只写入WrappedCronTaskList，单纯填充信息，不启动任务
            appTaskHandler.addWrappedCronTask(new WrappedCronTask(id,msg,cron,status,createTime,updateTime,runnable),true);
            //启动任务，获取信息
            ScheduledTask scheduledTask = taskRegistrar.scheduleCronTask(new CronTask(runnable,cron));
            //2.交给scheduledCronTaskMap管理，后续可以cancel
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

        //保留注册器
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
