package com.xjr.mzmall.enums;

/**
 * 异常枚举类
 */
public enum ExceptionEnum {
    BAD_REQUEST(400,"请求数据格式不正确"),
    UNAUTHORIZED(401, "登录凭证过期!"),
    FORBIDDEN(403, "没有访问权限!"),
    NOT_FOUND(404, "请求的资源找不到!"),
    // 500
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVICE_UNAVAILABLE(503, "服务器正忙，请稍后再试!"),
    // 未知异常
    UNKNOWN(10000, "未知异常!"),
    // 自定义
    IS_NOT_NULL(10001,"%s不能为空");

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误描述
     */
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
