package org.promise.publish.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.promise.common.util.RpcResultUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author: 黄相淇
 * @date: 2022年02月28日 17:00
 * @description:
 */

@Aspect
@Component
@Slf4j
@Order(2000)
public class ExceptionHandleAspect {

    @Pointcut("execution(public org.promise.common.result.rpc.RpcResult org.promise.publish.service.api.impl.*.*(..))||"+
            "execution(public * org.promise.publish.service.http.*.*(..))")
    public void fail(){

    }

    @Around("fail()")
    public Object exceptionHandler( ProceedingJoinPoint joinPoint){
        Object result;
        try{
            result=joinPoint.proceed ();
        }catch (Throwable e){
            Signature signature=joinPoint.getSignature ();
            log.error ("execute {}.{}() failed, throw exception: ",joinPoint.getTarget ().getClass ().getSimpleName (),signature.getName (),e);
            result= RpcResultUtil.fail (e.getMessage ());
        }
        return result;
    }
}
