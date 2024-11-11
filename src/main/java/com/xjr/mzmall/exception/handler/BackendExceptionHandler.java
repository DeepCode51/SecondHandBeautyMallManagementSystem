package com.xjr.mzmall.exception.handler;

import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.enums.ResultEnum;
import com.xjr.mzmall.exception.BackendBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class BackendExceptionHandler {

    /**
     * 后台业务异常处理
     *
     * @param e 业务异常
     * @return
     */
    @ExceptionHandler(value = BackendBusinessException.class)
    @ResponseBody
    public Result backendBusinessExceptionHandler(HttpServletRequest request, BackendBusinessException e) {
        return Result.fail(e.getCode(), e.getErrorMsg());
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public Result exceptionHandler(HttpServletRequest request, Exception e) {
//        log.info(e.getMessage());
//        return Result.fail(ResultEnum.FAIL.getCode(), "程序发生未知异常，请联系运维人员排查");
//    }
}
