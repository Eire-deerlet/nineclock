package com.nineclock.common.exception;

import com.nineclock.common.entity.Result;
import com.nineclock.common.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//统一异常处理的类
@ControllerAdvice
@ResponseBody
@Slf4j
public class BasicExceptionHandler {

    /**
     * 系统的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class) //指定捕获的异常类型
    public Result otherException(Exception e){
        log.error("系统的异常：",e);
        // SERVER_ERROR(500,"对不起,系统繁忙,请稍后重试"),
        return Result.errorCodeMessage(ResponseEnum.SERVER_ERROR.getCode(),
                ResponseEnum.SERVER_ERROR.getMessage());
    }
    /**
     * 业务的异常
     */
    @ExceptionHandler(NcException.class)
    public Result resolveException(NcException ncException){
        log.error("业务的异常",ncException);

        return Result.errorCodeMessage(ncException.getCode(),ncException.getMessage());
    }
}
