package org.promise.http.service.constant;

import org.promise.common.constant.HttpMethodEnum;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:31
 * @description:
 */
public class AccessMethodWhiteList {
    private static Set<String> methodSet;

    static {
        methodSet=new HashSet<> ();
        methodSet.add (HttpMethodEnum.OPTIONS.getName ());
    }

    public static boolean allowMethod(String method){
        return methodSet.contains (method);
    }
}
