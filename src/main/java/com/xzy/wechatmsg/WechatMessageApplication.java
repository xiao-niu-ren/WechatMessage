package com.xzy.wechatmsg;

import com.xzy.wechatmsg.bo.WrappedCronTask;
import com.xzy.wechatmsg.manager.task.AppTaskHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.List;
import java.util.Map;

@EnableScheduling
@MapperScan("com.xzy.wechatmsg.mapper")
@SpringBootApplication
public class WechatMessageApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WechatMessageApplication.class, args);
        AppTaskHandler appTaskHandler = new AppTaskHandler();
        List<WrappedCronTask> wrappedCronTaskList = appTaskHandler.getWrappedCronTaskList();
        Map<String, ScheduledTask> scheduledCronTaskMap = appTaskHandler.getScheduledCronTaskMap();
        while(true){
            try {
                wrappedCronTaskList.forEach(System.out::println);
                scheduledCronTaskMap.entrySet().forEach(System.out::println);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
