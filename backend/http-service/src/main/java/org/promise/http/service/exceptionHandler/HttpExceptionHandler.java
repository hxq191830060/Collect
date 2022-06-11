package org.promise.http.service.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.promise.common.exception.BaseException;
import org.promise.common.result.http.HttpResult;
import org.promise.common.util.HttpResultUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: 黄相淇
 * @date: 2022年03月05日 1:35
 * @description: 全局异常处理器
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class HttpExceptionHandler {


    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<HttpResult> handlerException( BaseException exception, ServletWebRequest request ){
        log.error ("对url:{}的访问过程中出现了异常",request.getRequest ().getRequestURI (),exception);
        HttpResult result= HttpResultUtil.failFromException (exception);
        return new ResponseEntity<> (result, HttpStatus.OK);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<HttpResult> handlerException( Exception exception, ServletWebRequest request ){
        log.error ("对url:{}的访问过程中出现了异常",request.getRequest ().getRequestURI (),exception);
        HttpResult result= HttpResultUtil.fail (exception.getMessage ());
        return new ResponseEntity<> (result, HttpStatus.OK);
    }


}
