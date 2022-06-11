package org.promise.user.service.api.api;

import com.sun.org.apache.bcel.internal.classfile.Code;
import org.promise.common.result.rpc.RpcResult;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.request.UserIdRequest;
import org.promise.user.service.api.request.UserInfoRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.promise.user.service.api.response.UserInfoResponse;

/**
 * @author moqi
 * @date 2022/3/3 23:38
 */
public interface UserService {

    /**
     * 用户注册
     * @param userInfoRequest
     * @return
     */
    RpcResult<UserInfoResponse> register(UserInfoRequest userInfoRequest);

    /**
     * 用户登录
     * @param userInfoRequest
     * @return
     */
    RpcResult<UserInfoResponse> login(UserInfoRequest userInfoRequest);

    /**
     * 用户信息更新(密码，昵称，头像，自我介绍)
     * @param userInfoRequest
     * @return
     */
    RpcResult<UserInfoResponse> updateUserInfo(UserInfoRequest userInfoRequest);

    /**
     * 批量查询——批量根据用户的Id获取用户数据
     * @param
     * @return
     */
    RpcResult<BatchGetUserInfoResponse> batchGetUserInfoByUserId( BatchGetUserInfoRequest request);

    RpcResult<UserInfoResponse> getUserInfoByUserId(UserIdRequest userIdRequest);

    RpcResult<UserInfoResponse> getVerificationCode(UserInfoRequest userInfoRequest);
}
