package org.promise.http.service.strategy;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.promise.common.util.HttpResultUtil;
import org.promise.common.util.JsonUtil;
import org.promise.http.service.info.ValidUserInfo;
import org.promise.http.service.util.JwtUtil;
import org.promise.http.service.util.ValidUserThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:44
 * @description:
 */

@Slf4j
public class TokenCertificationStrategy implements CertificationStrategy {

    public static final String TOKEN_HEADER="Authorization";


    @Override
    public boolean certificate ( HttpServletRequest request, HttpServletResponse response ) {
        String token=request.getHeader (TOKEN_HEADER);

        if(StringUtils.isNotEmpty (token)){
            Claims claims=JwtUtil.getClaimsFromToken (token);
            if(claims==null){
                //token解析失败
                return false;
            }
            ValidUserInfo userInfo=new ValidUserInfo (JwtUtil.getUserIdFromClaims (claims),JwtUtil.getRoleFromClaims (claims) );
            ValidUserThreadLocalUtil.set (userInfo);

            log.info ("请求合法,token有效, 请求信息——URL: {}, method: {}, token: {}, cookies: {},  Protocol: {}"
                    ,request.getRequestURL ()
                    ,request.getMethod ()
                    ,request.getHeader (TokenCertificationStrategy.TOKEN_HEADER)
                    ,request.getCookies ()
                    ,request.getProtocol ());
            return true;
        }

        return false;
    }

    @Override
    public boolean processCertificateFailed ( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        response.setCharacterEncoding ("UTF-8");
        response.setContentType("application/json");
        response.getWriter ().println (JsonUtil.toJson(HttpResultUtil.unauthorized ()));
        response.getWriter ().flush ();
        return false;
    }

    @Override
    public void afterProcess () {
        ValidUserThreadLocalUtil.remove ();
    }
}
