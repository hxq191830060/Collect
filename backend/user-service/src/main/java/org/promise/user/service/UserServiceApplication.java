package org.promise.user.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * @author: 黄相淇
 * @date: 2022年03月03日 21:43
 * @description:
 */
@SpringBootApplication
@MapperScan("org.promise.user.service.mapperService")
public class UserServiceApplication {
    public static void main(String[]args){
        SpringApplication.run (UserServiceApplication.class,args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void setDruidProperties(){
        System.setProperty("druid.mysql.usePingMethod","false");
    }
}
