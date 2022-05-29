package org.promise.user.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.user.service.api.info.UserInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/4 0:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest implements Serializable {

    private static final long serialVersionUID = 2987908514311905949L;

    private UserInfo userInfo;

    private String verificationCode;
}
