package org.promise.http.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.promise.common.util.JsonUtil;
import org.promise.user.service.api.info.UserInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 黄相淇
 * @date: 2022年03月03日 22:14
 * @description:
 */
@Slf4j
public class JwtUtil {

    private static final String key="xqggyyds";

    //过期时间，单位ms
    private static final Long expiration=3600000L;

    public static final String USER_ID="userId";

    public static final String ROLE="role";

    /**
     * claims里面只会有2个键值对——userId:value,role:value
     */
    public static String generateToken( Map<String,Object> claims){
        return Jwts.builder ().setClaims (claims).setExpiration (generateExpirationDate()).signWith (SignatureAlgorithm.HS512,key).compact ();
    }


    public static Date generateExpirationDate(){
        return new Date (System.currentTimeMillis ()+expiration);
    }

    public static Claims getClaimsFromToken(String token){
        Claims claims=null;
        try{
            claims=Jwts.parser ().setSigningKey (key).parseClaimsJws (token).getBody ();
        }catch (Exception e){
            log.warn ("JWT格式校验失败,token为: {}, 失败信息: {}",token,e.getMessage ());
        }
        return claims;
    }

    public static Long getUserIdFromClaims(Claims claims){
        return Long.parseLong((String) claims.get(USER_ID));
    }


    public static List<String> getRoleFromClaims(Claims claims){
        return JsonUtil.fromJsonToList((String) claims.get(ROLE),String.class);
    }

    public static Long getUserIdFromToken(String token){
        return getUserIdFromClaims (getClaimsFromToken (token));
    }

    public static List<String> getRoleFromToken(String token){
        return getRoleFromClaims (getClaimsFromToken (token));
    }

    public static Date getExpiredDateFromClaims(Claims claims){
        return claims.getExpiration ();
    }

    public static Date getExpiredDateFromToken(String token){
        return getExpiredDateFromClaims (getClaimsFromToken (token));
    }

    public static boolean isTokenExpired(String token){
        return getExpiredDateFromToken (token).before (new Date ());
    }

    public static String generateTokenByUserInfo( UserInfo userInfo ){
        Map<String, Object> map=new HashMap<>();
        map.put (USER_ID,String.valueOf(userInfo.getUserId()));
        map.put (ROLE, JsonUtil.toJson(userInfo.getRole()));
        return generateToken (map);
    }

}
