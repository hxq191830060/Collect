package org.promise.common.util;

import org.promise.common.constant.HttpCodes;
import org.promise.common.exception.BaseException;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.rpc.RpcResult;


public class HttpResultUtil {

    public static <T>HttpResult<T> success(T data){
        return new HttpResult<> (HttpCodes.SUCCESS,data);
    }

    public static <T>HttpResult<T> success(){
        return new HttpResult<> (HttpCodes.SUCCESS);
    }

    public static <T>HttpResult<T> fail(String errorMessage){
        HttpResult<T> result=new HttpResult<T> (HttpCodes.ERROR);
        result.setMessage (errorMessage);
        return result;
    }

    public static <T>HttpResult<T> failFromException( BaseException exception){
        HttpResult<T> result=new HttpResult<T> (HttpCodes.ERROR);
        result.setMessage (exception.getErrorMessage ());
        return result;
    }

    public static <String> HttpResult<String> redirect(String redirectUrl){
        return new HttpResult<> (HttpCodes.REDIRECT,redirectUrl);
    }

    public static <T> HttpResult<T> unauthorized(){
        return new HttpResult<> (HttpCodes.UNAUTHORIZED);
    }

    public static <T> HttpResult<T> forbidden(){
        return new HttpResult<> (HttpCodes.FORBIDDEN);
    }


}
