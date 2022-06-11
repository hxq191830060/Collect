package org.promise.http.service.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.promise.http.service.strategy.CertificationStrategy;
import org.promise.http.service.strategy.MethodCertificationStrategy;
import org.promise.http.service.strategy.TokenCertificationStrategy;
import org.promise.http.service.strategy.URLCertificationStrategy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:09
 * @description:
 */
@Slf4j
public class SecurityFilter implements Filter {

    private List<CertificationStrategy> certificationStrategyList;

    private Set<String> excludeUrlSet;

    @Override
    public void init ( FilterConfig filterConfig ) throws ServletException {
        Filter.super.init (filterConfig);

        //初始化策略列表
        certificationStrategyList=new ArrayList<> ();
        certificationStrategyList.add (new MethodCertificationStrategy ());
        certificationStrategyList.add (new URLCertificationStrategy ());
        certificationStrategyList.add (new TokenCertificationStrategy ());

    }

    @Override
    public void doFilter ( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException {

        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        HttpServletResponse httpServletResponse=(HttpServletResponse) response;

        String url=((HttpServletRequest) request).getRequestURI ();

        boolean success=false;
        for(CertificationStrategy strategy:certificationStrategyList){
            if(strategy.certificate (httpServletRequest,httpServletResponse)){
                //认证成功
                success=true;
                chain.doFilter (httpServletRequest,httpServletResponse);
                strategy.afterProcess ();
                break;
            }else{
                //认证失败，调用其失败处理逻辑
                if(!strategy.processCertificateFailed (httpServletRequest,httpServletResponse)){
                    break;
                }
            }
        }

        if(!success){
            log.info ("请求非法, 请求信息——URL: {}, method: {}, token: {}, cookies: {}, Protocol: {}"
                    ,httpServletRequest.getRequestURL ()
                    ,httpServletRequest.getMethod ()
                    ,httpServletRequest.getHeader (TokenCertificationStrategy.TOKEN_HEADER)
                    ,httpServletRequest.getCookies ()
                    ,httpServletRequest.getProtocol ());
        }

    }
}
