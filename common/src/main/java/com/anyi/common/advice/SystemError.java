package com.anyi.common.advice;

public enum SystemError implements IError {
    SYSTEM_INTERNAL_ERROR(500, "网络异常，请稍后重试"),
    INVALID_PARAMETER(400, "请输入合法的参数"),
    PAGE_NOT_FOUND(404, "请求的地址不存在"),
    TOKEN_INVALID(10000, "登录状态异常，请重新登录"),
    TOKEN_EXPIRE(10001, "登录已过期，请重新登录")
    ;

    int code;
    String message;

    SystemError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
