package org.promise.http.service.httpService;

import org.promise.common.result.http.HttpResult;
import org.promise.http.service.vo.user.UserRegisterVO;
import org.promise.http.service.vo.user.UserVO;

/**
 * @author moqi
 * @date 2022/3/26 13:47
 */
public interface UserHttpService {

    /**
     * 用户注册
     * @param userRegisterVO 用户信息
     * @return 登录结果
     */
    HttpResult<String> register(UserRegisterVO userRegisterVO);

    /**
     * 用户登录
     * @param userVO 用户信息
     * @return 登录凭证token
     */
    HttpResult<String> login(UserVO userVO);

    /**
     * 更新用户信息
     * @param userVO 待更新的用户信息，其中userId不为null
     * @return 更新结果
     */
    HttpResult<String> update(UserVO userVO);

    /**
     * 获取当前用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    HttpResult<UserVO> getUser(Long userId);

    HttpResult<UserVO> getVerificationCode(String username);
}
