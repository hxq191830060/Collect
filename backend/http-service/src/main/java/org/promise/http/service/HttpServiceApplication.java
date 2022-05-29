package org.promise.http.service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class HttpServiceApplication {
    public static void main(String[]args){
        SpringApplication.run(HttpServiceApplication.class, args);
    }

}
