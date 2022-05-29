package org.promise.http.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.promise.http.service.constant.AccessUrlWhiteList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:43
 * @description:
 */
@Slf4j
public class URLCertificationStrategy implements CertificationStrategy{

    @Override
    public boolean certificate ( HttpServletRequest request, HttpServletResponse response ) {
        return AccessUrlWhiteList.allowUrl(request.getRequestURI ());
    }

    @Override
    public boolean processCertificateFailed ( HttpServletRequest request, HttpServletResponse response ) {
        return true;
    }

    @Override
    public void afterProcess () {
    }
}
