package com.xzy.wechatmsg.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzy.wechatmsg.bo.WrappedCronTask;
import com.xzy.wechatmsg.domain.task.model.Task;
import com.xzy.wechatmsg.enums.TaskStatusEnum;
import com.xzy.wechatmsg.manager.task.AppTaskHandler;
import com.xzy.wechatmsg.domain.task.mapper.TaskMapper;
import com.xzy.wechatmsg.manager.task.RunnableWechatMsgFactory;
import com.xzy.wechatmsg.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
    RunnableWechatMsgFactory runnableWechatMsgFactory;

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
            //TODO : 把所有的sendToXiaoniuren换成变成通用接口，支持发群组定时消息等
            Runnable runnable = runnableWechatMsgFactory.createRunnable(msg);
            //Running且updateTime小于2小时的task，就说明这个这个task因为SpringBoot应用停止运行挂过一段时间（一般一个小时以上，特殊情况可能很短，但是也一定挂掉过）
            //那就不启动数据库中过时的Running状态的task，并将状态设置成stop，等待用户手动启动各个挂掉过的task
            //场景：挂掉以后，用户发现前端的updateTime还是很久之前的，于是重启SpringBoot应用，启动成功以后，给出用户反馈（立即将过期的设置为stop表示启动成功了）
            //原本意思是让另一个应用去监控这个应用= =
            if(TimeUtils.isUpdateTimeExpired(updateTime)){
                taskMapper.updateStatusWithOutUpdateTime(id,TaskStatusEnum.TASK_RUNNING.getValue(),TaskStatusEnum.TASK_STOP.getValue());
                return;
            }
            //appTask双写
            appTaskHandler.addWrappedCronTask(new WrappedCronTask(id,msg,cron,status,createTime,updateTime,runnable),true, taskRegistrar);
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
