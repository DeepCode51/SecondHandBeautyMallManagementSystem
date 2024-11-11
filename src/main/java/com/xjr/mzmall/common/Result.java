package com.xjr.mzmall.common;

import com.xjr.mzmall.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;   //0成功 1失败
    private String msg;
    private Long count;
    private T data;

    public static Result<Object> success(){
        return new Result<>(ResultEnum.SUCCESS.getCode(),
                ResultEnum.SUCCESS.getMessage(),null,null);
    }

    public static Result<Object> success(String msg){
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg, null, null);
    }

    public static Result<Object> success(Integer code, String msg){
        return new Result<>(code, msg, null, null);
    }

    public static Result<Object> success(String msg, Object data, Long count){
        return new Result<>(ResultEnum.SUCCESS.getCode(), msg, count, data);
    }

    public static Result<Object> success(Object data){
        return new Result<>(ResultEnum.SUCCESS.getCode(),
                ResultEnum.SUCCESS.getMessage(), null, data);
    }

    public static Result<Object> fail(){
        return new Result<>(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMessage(),null,null);
    }

    public static Result<Object> fail(String msg){
        return new Result<>(ResultEnum.FAIL.getCode(), msg, null, null);
    }

    public static Result<Object> fail(Integer code, String msg){
        return new Result<>(code,msg,null,null);
    }
}
