package com.itheima.reggie.common;

import com.itheima.reggie.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public R handlerBusinessException(BusinessException ex){
        log.warn(ex.getMessage());
        return R.error(ex.getMessage());
    }


    @ExceptionHandler
    public R handlerException(Exception ex){
        log.error("发生了异常:"+ex.getMessage());
        ex.printStackTrace();
        return R.error("不好意思,我们正在升级~~");
    }
}
