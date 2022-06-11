package org.promise.http.service.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:黄相淇
 * @date:2022年05月10日1:24
 * @description:用于健康检查
 */
@RestController
public class HealthCheckController {


    @GetMapping("/health")
    public String health(){
        return "health";
    }
}
