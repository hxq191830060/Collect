package org.promise.http.service.util;

import org.promise.http.service.info.ValidUserInfo;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 21:01
 * @description:
 */
public class ValidUserThreadLocalUtil {

    private static final ThreadLocal<ValidUserInfo> threadLocal=new ThreadLocal<> ();


    public static ValidUserInfo get(){
        return threadLocal.get ();
    }

    public static void remove(){
        threadLocal.remove ();
    }

    public static void set(ValidUserInfo userInfo){
        threadLocal.set (userInfo);
    }

}
