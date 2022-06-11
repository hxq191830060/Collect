package org.promise.publish.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.promise.common.util.JsonUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 13:29
 * @description: 用于对RPC接口进行日志打印——打印入参，出参，调用时间
 */
@Slf4j
@Component
@Order(1000)
@Aspect
public class RequestCostAspect {

    @Pointcut("execution(public * org.promise.publish.service.api.impl.*.*(..))||"+
            "@annotation(org.promise.common.annotation.RecordLog)")
    public void cost(){

    }

    @Around ("cost()")
    public Object execute( ProceedingJoinPoint joinPoint ) throws Throwable {

        String methodName=buildMethodName(joinPoint);
        String params=getParamsString(joinPoint.getArgs ());
        Object result;

        try{
            long startTime=System.currentTimeMillis ();
            result=joinPoint.proceed ();
            long endTime=System.currentTimeMillis ();
            log.info ("方法:{} 执行完成 ,入参: {}, 执行结果: {}, 耗时: {}毫秒",methodName,params,result,(endTime-startTime));
            return result;
        }catch (Throwable exception){
            log.error ("方法:{} 执行完成 ,入参: {}, 抛出异常: ",methodName,params,exception);
            throw exception;
        }

    }

    private String buildMethodName(ProceedingJoinPoint joinPoint){
        Object target=joinPoint.getTarget ();
        try{
            MethodSignature methodSignature=(MethodSignature) joinPoint.getSignature ();
            Method method=target.getClass ().getMethod (methodSignature.getName (),methodSignature.getParameterTypes ());
            return target.getClass ( ).getSimpleName ( ) +
                    "." +
                    methodSignature.getName ( )+"()";
        }catch (NoSuchMethodException e){
            return target.getClass ().getSimpleName ();
        }
    }


    private String getParamsString(Object[]args){
        if(args==null||args.length==0){
            return "";
        }
        StringBuilder builder=new StringBuilder(512);

        for(int i=0;i<args.length;i+=1){
            Object arg=args[i];
            if(arg==null){
                builder.append ("null");
            }else{
                builder.append (JsonUtil.toJson (arg));
            }
            if(i!=args.length-1){
                builder.append (" & ");
            }
        }
        return builder.toString ();
    }
}
