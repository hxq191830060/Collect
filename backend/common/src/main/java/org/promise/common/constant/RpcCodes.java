package org.promise.common.constant;


public enum RpcCodes {

    SUCCESS(50000,"success"),
    ERROR(60000,"error");

    private final Integer code;

    private final String message;

    RpcCodes(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public Integer getCode () {
        return code;
    }

    public String getMessage () {
        return message;
    }
}
