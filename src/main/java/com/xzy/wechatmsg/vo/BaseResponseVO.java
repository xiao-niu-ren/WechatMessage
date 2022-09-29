package com.xzy.wechatmsg.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: BaseResponseVO
 * @author: Xie zy
 * @create: 2022.09.01
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BaseResponseVO<T> {
    int code;
    Boolean success;
    T data;

    public static BaseResponseVO success() {
        return BaseResponseVO.builder()
                .code(0)
                .success(true)
                .data("success")
                .build();
    }

    public static <T> BaseResponseVO success(T data) {
        return BaseResponseVO.builder()
                .code(0)
                .success(true)
                .data(data)
                .build();
    }

    public static BaseResponseVO error() {
        return BaseResponseVO.builder()
                .code(-1)
                .success(false)
                .data("error")
                .build();
    }

    public static BaseResponseVO error(String errMsg) {
        return BaseResponseVO.builder()
                .code(-1)
                .success(false)
                .data(errMsg)
                .build();
    }

}
