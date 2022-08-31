package com.xzy.wechatmsg.enums;

/**
 * @description: TaskTypeEnum
 * @author: Xie zy
 * @create: 2022.09.01
 */
public enum TaskTypeEnum {
    /**
     * db
     */
    TASK_TYPE_DB("db","dbTaskHandler"),
    /**
     * app
     */
    TASK_TYPE_APP("app","appTaskHandler"),
    ;

    String value;
    String handler;

    TaskTypeEnum(String value,String handler) {
        this.value = value;
        this.handler = handler;
    }

    public String getValue() {
        return this.value;
    }

    public String getHandler() {
        return this.handler;
    }

    public static TaskTypeEnum findByValue(String value) {
        for (TaskTypeEnum taskTypeEnum : TaskTypeEnum.values()) {
            if (taskTypeEnum.value.equals(value)) {
                return taskTypeEnum;
            }
        }
        return null;
    }

    public static TaskTypeEnum findByHandler(String handler) {
        for (TaskTypeEnum taskTypeEnum : TaskTypeEnum.values()) {
            if (taskTypeEnum.handler.equals(handler)) {
                return taskTypeEnum;
            }
        }
        return null;
    }

}
