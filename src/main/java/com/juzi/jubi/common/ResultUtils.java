package com.juzi.jubi.common;

/**
 * 返回工具类
 *
 * @author codejuzi
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 需要传输的数据
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
