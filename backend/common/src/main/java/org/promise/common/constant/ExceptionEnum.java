package org.promise.common.constant;

import org.promise.common.exception.BaseException;

import javax.swing.*;


public enum ExceptionEnum {

    NullException(50001,"数据为null"),
    WorkerFullException(50002,"当前任务的工人已满"),
    TimeWrongException(50003,"当前时间错误"),
    UserHasExistException(50004,"当前用户已经存在"),
    UserDoesNotExistException(50005,"用户不存在"),
    IncorrectUserException(50006,"账号或密码错误"),
    TaskDoesNotExistException(50007,"任务不存在"),
    TestCreateFailedException(50008,"测试创建失败"),
    TestDoesNotExistException(50009,"测试不存在"),
    UserPasswordWrongException(50010,"当前账号已存在其他身份,注册新身份但密码错误"),
    DataDuplicateException(50011,"数据重复"),
    CancelTimeWrongException(50012,"可取消时间应早于结束时间")
    ;

    private Integer code;
    private String errorMessage;
    private BaseException exception;

    ExceptionEnum(Integer code,String errorMessage){
        this.code=code;
        this.errorMessage=errorMessage;
        this.exception=createException(code,errorMessage);
    }

    public void maybeThrow() throws BaseException{
        this.maybeThrow ( null );
    }

    public void maybeThrow(String message) throws BaseException {
        if(exception!=null){
            if(message!=null){
                throw createException (code,message);
            }else{
                throw exception;
            }
        }
    }

    private BaseException createException( Integer code, String errorMessage){
        return new BaseException (code,errorMessage);
    }

    public Integer getCode () {
        return code;
    }

    public void setCode ( Integer code ) {
        this.code = code;
    }

    public String getErrorMessage () {
        return errorMessage;
    }

    public void setErrorMessage ( String errorMessage ) {
        this.errorMessage = errorMessage;
    }

    public BaseException getException () {
        return exception;
    }

    public void setException ( BaseException exception ) {
        this.exception = exception;
    }
}
