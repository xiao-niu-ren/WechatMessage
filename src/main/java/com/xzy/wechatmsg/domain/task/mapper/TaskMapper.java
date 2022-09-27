package com.xzy.wechatmsg.domain.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzy.wechatmsg.domain.task.model.Task;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * @description: TaskMapper
 * @author: Xie zy
 * @create: 2022.09.02
 */
public interface TaskMapper extends BaseMapper<Task> {

    @Select("UPDATE task SET status = #{new_status},update_time = #{update_time} WHERE status = #{pre_status} AND id = #{id}")
    void updateStatusWithUpdateTime(@Param("id")Integer id, @Param("pre_status") Integer preStatus, @Param("new_status") Integer newStatus, @Param("update_time")LocalDateTime updateTime);

    @Select("UPDATE task SET status = #{new_status} WHERE status = #{pre_status} AND id = #{id}")
    void updateStatusWithOutUpdateTime(@Param("id")Integer id, @Param("pre_status") Integer preStatus, @Param("new_status") Integer newStatus);

}
