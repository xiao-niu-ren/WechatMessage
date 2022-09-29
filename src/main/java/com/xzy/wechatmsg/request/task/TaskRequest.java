package com.xzy.wechatmsg.request.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: TaskRequest
 * @author: Xie zy
 * @create: 2022.09.01
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskRequest {
    Integer taskId;
    /**
     * bo.WechatMsgWithInfoAndType的JSON字符串
     */
    String msg;
    String cron;
    Integer status;
    /**
     * "db"-数据库 | "app"-SpringBoot应用
     */
    String type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;
}
