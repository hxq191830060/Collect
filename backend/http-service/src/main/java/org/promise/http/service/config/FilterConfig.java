package org.promise.http.service.config;

import org.promise.http.service.filter.SecurityFilter;
import org.promise.http.service.health.HealthCheckController;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.annotation.WebServlet;

/**
 * @author: 黄相淇
 * @date: 2022年03月04日 20:11
 * @description:
 */
@Configuration
@WebServlet
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SecurityFilter> registerSecurityFilter(){
        FilterRegistrationBean<SecurityFilter> registration=new FilterRegistrationBean<> ();
        registration.setFilter (getSecurityFilter ());
        registration.addUrlPatterns ("/*");//所有的请求都要过滤
        registration.setName ("SecurityFilter");
        registration.setOrder (2);
        return registration;
    }


    private SecurityFilter getSecurityFilter(){
        return new SecurityFilter ();
    }
}
