package org.promise.http.service.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import org.promise.http.service.vo.oss.OssTokenVO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author moqi
 * @date 2022/3/2 18:55
 */
@ConfigurationProperties(prefix = "alioss")
@Component
@Data
public class OssUtil {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String roleArn;

    private String regionId;

    private String bucket;

    public OssTokenVO getToken() throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient defaultAcsClient=new DefaultAcsClient(profile);
        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest();
        assumeRoleRequest.setRoleSessionName("lyy");
        assumeRoleRequest.setRoleArn(roleArn);
        assumeRoleRequest.setDurationSeconds(1000L);
        AssumeRoleResponse acsResponse = defaultAcsClient.getAcsResponse(assumeRoleRequest);
        AssumeRoleResponse.Credentials credentials=acsResponse.getCredentials();
        return new OssTokenVO(
                credentials.getAccessKeyId(),
                credentials.getAccessKeySecret(),
                credentials.getSecurityToken(),
                regionId,
                bucket
        );
    }
}
