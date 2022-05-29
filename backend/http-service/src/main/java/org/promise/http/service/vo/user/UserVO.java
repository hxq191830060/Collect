package org.promise.http.service.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/3 19:44
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -3972945506896008359L;

    private Long userId;

    private String email;

    private String password;

    private List<String> role;

    private String nickname;

    private String avatar;

    private String introduction;

    private Integer publishTaskNum;

    private Integer acceptTaskNum;

    private List<String> skills;

    private List<String> taskPreference;

    private List<String> testDevs;
}
