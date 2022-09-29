package com.xzy.wechatmsg;

import com.xzy.wechatmsg.bo.WrappedCronTask;
import com.xzy.wechatmsg.manager.task.AppTaskHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@MapperScan("com.xzy.wechatmsg.domain.task.mapper")
@SpringBootApplication
public class WechatMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMessageApplication.class, args);
        AppTaskHandler appTaskHandler = new AppTaskHandler();
        List<WrappedCronTask> wrappedCronTaskList = appTaskHandler.getWrappedCronTaskList();
        while (true) {
            try {
                System.out.println("------------------" + LocalDateTime.now() + "---------------------------");
                wrappedCronTaskList.forEach(System.out::println);
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
