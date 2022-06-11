package org.promise.http.service.controller;

import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.promise.http.service.util.OssUtil;
import org.promise.http.service.vo.oss.OssTokenVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author moqi
 * @date 2022/3/2 18:55
 */
@RestController
@Slf4j
@RequestMapping("/oss")
public class OssController {
    @Resource
    private OssUtil ossUtil;

    @GetMapping("getToken")
    public OssTokenVO getToken() throws ClientException {
        log.info("用户尝试获取token");
        return ossUtil.getToken();
    }
}
