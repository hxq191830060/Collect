package org.promise.http.service.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

/**
 * @author moqi
 * @date 2022/5/19 21:12
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterVO {

    private String email;

    private String password;

    private List<String> role;

    private String nickname;

    private String verificationCode;

}
