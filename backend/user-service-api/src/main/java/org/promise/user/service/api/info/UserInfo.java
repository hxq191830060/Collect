package org.promise.user.service.api.info;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 23:37
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -3402505271732920471L;

    private Long userId;

    private String userName;

    private String password;

    //用户的角色，目前仅支持admin,publisher,worker 三种角色，每个用户可以拥有多种角色——"admin,publisher"
    private List<String> role;

    private String nickName;

    private String avatar;

    private String introduction;

    private List<String> skills;

    private List<String> taskPreference;

    private List<String> testDevs;
}
