package org.promise.common.result.http;

import lombok.Data;
import org.promise.common.constant.HttpCodes;

import java.io.Serializable;


@Data
public class HttpResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    public HttpResult( HttpCodes httpCodes,T data){
        this.data=data;
        this.code=httpCodes.getCode ();
        this.message=httpCodes.getMessage ();
    }


    public HttpResult(HttpCodes httpCodes){
        this.code=httpCodes.getCode ();
        this.message=httpCodes.getMessage ();
    }

    //不允许直接创建HttpResult
    private HttpResult(){

    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
