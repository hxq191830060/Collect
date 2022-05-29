package org.promise.http.service.vo.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author moqi
 * @date 2022/3/2 18:55
 */
@Data
@AllArgsConstructor
public class OssTokenVO {

    private String accessKeyId;

    private String accessKeySecret;

    private String stsToken;

    private String regionId;

    private String bucket;
}
