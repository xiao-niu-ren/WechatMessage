package com.xzy.wechatmsg.domain.task.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @description: Task
 * @author: Xie zy
 * @create: 2022.09.02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "task", autoResultMap = true)
public class Task {
    @TableId(type = IdType.AUTO,value = "id")
    Integer id;
    String msg;
    String cron;
    /**
     * 0-运行结束 ｜ 1-正在运行
     */
    Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;
}
