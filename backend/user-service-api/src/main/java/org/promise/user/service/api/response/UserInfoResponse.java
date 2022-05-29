package org.promise.user.service.api.response;

import lombok.Data;
import org.promise.user.service.api.info.UserInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/4 0:04
 */
@Data
public class UserInfoResponse implements Serializable {

    private static final long serialVersionUID = 6466534666611208280L;

    private UserInfo userInfo;
}
