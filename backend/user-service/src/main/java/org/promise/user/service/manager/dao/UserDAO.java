package org.promise.user.service.manager.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 23:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO{

    private Long userId;

    private String userName;

    private String password;

    private List<String> role;

    private String nickName;

    private String avatar;

    private String introduction;

    private List<String> skills;

    private List<String> taskPreference;

    private List<String> testDevs;
}
