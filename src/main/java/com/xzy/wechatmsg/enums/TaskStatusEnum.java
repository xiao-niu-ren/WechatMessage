package com.xzy.wechatmsg.enums;

/**
 * @description: TaskStatusEnum
 * @author: Xie zy
 * @create: 2022.09.02
 */
public enum TaskStatusEnum {
    /**
     * stop
     */
    TASK_STOP(0),
    /**
     * running
     */
    TASK_RUNNING(1),
    ;

    Integer value;

    TaskStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static TaskStatusEnum findByValue(Integer value) {
        for (TaskStatusEnum taskStatusEnum : TaskStatusEnum.values()) {
            if (taskStatusEnum.value.equals(value)) {
                return taskStatusEnum;
            }
        }
        return null;
    }

}
