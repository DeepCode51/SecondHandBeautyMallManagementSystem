package com.xjr.mzmall.exception;


import com.xjr.mzmall.enums.ExceptionEnum;

/**
 * 后台业务异常
 */
public class BackendBusinessException extends RuntimeException{
    private ExceptionEnum exceptionEnum;
    private Integer code;
    private String errorMsg;

    public BackendBusinessException() {
        super();
    }

    public BackendBusinessException(ExceptionEnum exceptionEnum) {
        super("{code:" + exceptionEnum.getCode() + ",errorMsg:" + exceptionEnum.getMsg() + "}");
        this.exceptionEnum = exceptionEnum;
        this.code = exceptionEnum.getCode();
        this.errorMsg = exceptionEnum.getMsg();
    }

    public BackendBusinessException(Integer code, String errorMsg) {
        super("{code:" + code + ",errorMsg:" + errorMsg + "}");
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public BackendBusinessException(Integer code, String errorMsg, Object... args) {
        super("{code:" + code + ",errorMsg:" + String.format(errorMsg, args) + "}");
        this.code = code;
        this.errorMsg = String.format(errorMsg, args);
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
