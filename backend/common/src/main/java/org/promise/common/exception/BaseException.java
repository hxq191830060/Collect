package org.promise.common.exception;


import java.io.Serializable;

public class BaseException extends RuntimeException  {

    private Integer code;

    private String errorMessage;

    public BaseException(Integer code,String errorMessage){
        super(errorMessage);
        this.code=code;
        this.errorMessage=errorMessage;
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
}
