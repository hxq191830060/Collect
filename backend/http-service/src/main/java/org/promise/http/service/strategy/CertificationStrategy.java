package org.promise.http.service.strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:29
 * @description: 认证策略
 */
public interface CertificationStrategy {

    //是否认证成功
    boolean certificate( HttpServletRequest request, HttpServletResponse response );

    //如果认证失败，其处理逻辑
    //如果返回true，那么继续认证
    //如果返回false，停止认证
    boolean processCertificateFailed(HttpServletRequest request,HttpServletResponse response) throws IOException;


    void afterProcess();
}
