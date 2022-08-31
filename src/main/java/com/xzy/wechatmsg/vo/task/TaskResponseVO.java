package com.xzy.wechatmsg.vo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzy.wechatmsg.vo.BaseResponseVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @description: TaskResponseVO
 * @author: Xie zy
 * @create: 2022.09.01
 */
@AllArgsConstructor
@Data
public class TaskResponseVO extends BaseResponseVO<TaskResponseVO.TaskVO> {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class TaskVO{
        Integer taskId;
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

}
