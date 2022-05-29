package org.promise.common.constant;


public enum HttpCodes {

    SUCCESS(200,"success"),
    ERROR(500,"error"),
    REDIRECT(300,"redirect"),
    UNAUTHORIZED(401,"暂未登录或token已经过期"),
    FORBIDDEN(403,"没有相关权限");

    private final Integer code;
    private final String message;

    HttpCodes ( Integer code, String message ){
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
