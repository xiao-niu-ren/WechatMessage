package com.xzy.wechatmsg.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

/**
 * @description: WrappedCronTask
 * @author: Xie zy
 * @create: 2022.09.02
 */
public class WrappedCronTask extends CronTask {

    private Integer taskId;
    private String msg;
    private String cron;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;

    public WrappedCronTask(Integer taskId,
                           String msg,
                           String cron,
                           Integer status,
                           LocalDateTime createTime,
                           LocalDateTime updateTime,
                           Runnable runnable) {
        super(runnable,cron);
        this.taskId = taskId;
        this.msg = msg;
        this.cron = cron;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "WrappedCronTask{" +
                "taskId=" + taskId +
                ", msg='" + msg + '\'' +
                ", cron='" + cron + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
