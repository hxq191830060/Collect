package org.promise.user.service.mapperService.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/3/3 23:41
 */
@Data
public class UserPO {


    private Long userId;

    private String userName;

    private String password;

    private String role;

    private String nickName;

    private String avatar;

    private String introduction;

    private String skills;

    private String taskPreference;

    private String testDevs;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
