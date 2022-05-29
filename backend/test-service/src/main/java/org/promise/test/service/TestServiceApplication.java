package org.promise.test.service;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author moqi
 * @date 2022/2/27 23:22
 */
@SpringBootApplication
@MapperScan("org.promise.test.service.mapperService")
public class TestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }

    @PostConstruct
    public void setDruidProperties() {
        System.setProperty("druid.mysql.usePingMethod", "false");
    }
}
