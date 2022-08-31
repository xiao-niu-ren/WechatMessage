package com.xzy.wechatmsg.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * @description: TimeUtils
 * @author: Xie zy
 * @create: 2022.09.03
 */
public class TimeUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isUpdateTimeExpired(LocalDateTime updateTime){
        LocalDateTime now = LocalDateTime.now();
        return updateTime.plusHours(2).isBefore(now);
    }

}
