package org.promise.common.util;

import org.promise.common.constant.RpcCodes;
import org.promise.common.exception.BaseException;
import org.promise.common.result.rpc.RpcResult;


public class RpcResultUtil {


    public static <T> RpcResult<T> success(T data){
        return new RpcResult<> (RpcCodes.SUCCESS,data);
    }

    public static<T> RpcResult<T> success(){
        return new RpcResult<> (RpcCodes.SUCCESS);
    }

    public static <T> RpcResult<T> failFromException( BaseException e ){
        RpcResult<T> result=new RpcResult<> (RpcCodes.ERROR);
        result.setMessage (e.getErrorMessage ());
        return result;
    }


    public static<T> RpcResult<T> fail(String errorMessage){
        RpcResult<T> result=new RpcResult<> (RpcCodes.ERROR);
        result.setMessage (errorMessage);
        return result;
    }


}
