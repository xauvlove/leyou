package com.leyou.common.advice;

import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyMarketException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(LyMarketException.class) //如果不加注解 那么拦截所有异常
    public ResponseEntity<ExceptionResult> handlerException(LyMarketException e) {
        LyMarketExceptionEnum lyMarketExceptionEnum = e.getLyMarketExceptionEnum();
        return ResponseEntity.status(lyMarketExceptionEnum.getCode()).body(new ExceptionResult(lyMarketExceptionEnum));
    }
}
