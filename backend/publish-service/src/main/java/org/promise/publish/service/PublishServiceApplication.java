package org.promise.publish.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
@MapperScan("org.promise.publish.service.mapperService")
public class PublishServiceApplication {
    public static void main(String[]args){
        SpringApplication.run (PublishServiceApplication.class,args);
    }

    @PostConstruct
    public void setDruidProperties(){
        System.setProperty("druid.mysql.usePingMethod","false");
    }
}
