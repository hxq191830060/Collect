package org.promise.common.constant;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:32
 * @description:
 */
public enum HttpMethodEnum {

    OPTIONS("OPTIONS"),
    GET("GET"),
    POST("POST"),
    PUT("PUT");


    private String name;

    HttpMethodEnum(String name){
        this.name=name;
    }

    public String getName () {
        return name;
    }
}
