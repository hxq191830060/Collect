package org.promise.http.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.promise.http.service.constant.AccessMethodWhiteList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:30
 * @description:
 */
@Slf4j
public class MethodCertificationStrategy implements CertificationStrategy{

    @Override
    public boolean certificate ( HttpServletRequest request , HttpServletResponse response ) {
        String method=request.getMethod ();
        return AccessMethodWhiteList.allowMethod (method);
    }

    @Override
    public boolean processCertificateFailed ( HttpServletRequest request, HttpServletResponse response ) {
        return true;
    }

    @Override
    public void afterProcess () {
    }
}
