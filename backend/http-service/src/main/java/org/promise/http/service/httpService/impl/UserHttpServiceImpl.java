package org.promise.http.service.httpService.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.http.service.convert.UserConvert;
import org.promise.http.service.httpService.UserHttpService;
import org.promise.http.service.util.JwtUtil;
import org.promise.http.service.vo.user.UserRegisterVO;
import org.promise.http.service.vo.user.UserVO;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.request.PublisherIdRequest;
import org.promise.publish.service.api.response.TaskCountResponse;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.request.TestQueryRequest;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.UserIdRequest;
import org.promise.user.service.api.request.UserInfoRequest;
import org.promise.user.service.api.response.UserInfoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Year;

/**
 * @author moqi
 * @date 2022/3/26 13:47
 */
@Slf4j
@Service
public class UserHttpServiceImpl implements UserHttpService {


    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private UserService userService;

    @Resource
    private UserConvert userConvert;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private PublishService publishService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private TestService testService;


    /**
     * 用户注册
     * @param userRegisterVO 用户信息
     * @return 登录结果
     */
    @Override
    public HttpResult<String> register(UserRegisterVO userRegisterVO) {
        if(userRegisterVO==null){
            return HttpResultUtil.fail("用户信息为空");
        }
        UserInfo userInfo= userConvert.convertVO2Info(userRegisterVO);

        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);
        userInfoRequest.setVerificationCode(userRegisterVO.getVerificationCode());

        RpcResult<UserInfoResponse> rpcResult = userService.register(userInfoRequest);
        if(rpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())){
            return HttpResultUtil.success();
        }else {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
    }

    /**
     * 用户登录
     * @param userVO 用户信息
     * @return 登录凭证token
     */
    @Override
    public HttpResult<String> login(UserVO userVO) {
        UserInfo userInfo= userConvert.convertVO2Info(userVO);

        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);

        RpcResult<UserInfoResponse> rpcResult = userService.login(userInfoRequest);
        if(rpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())){
            String token=JwtUtil.generateTokenByUserInfo(rpcResult.getData().getUserInfo());
            return HttpResultUtil.success(token);
        }else {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param userVO 待更新的用户信息，其中userId不为null
     * @return 更新结果
     */
    @Override
    public HttpResult<String> update(UserVO userVO) {
        UserInfo userInfo= userConvert.convertVO2Info(userVO);
        log.info("需要更新的用户信息:{}",userInfo);
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);

        RpcResult<UserInfoResponse> rpcResult = userService.updateUserInfo(userInfoRequest);

        if(rpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())){
            return HttpResultUtil.success();
        }else {
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public HttpResult<UserVO> getUser(Long userId) {
        UserIdRequest userIdRequest=new UserIdRequest();
        userIdRequest.setUserId(userId);
        RpcResult<UserInfoResponse> rpcResult = userService.getUserInfoByUserId(userIdRequest);
        if(rpcResult.getData()==null){
            return HttpResultUtil.fail(rpcResult.getMessage());
        }
        UserVO userVO=userConvert.convertInfo2VO(rpcResult.getData().getUserInfo());

        //添加用户发布任务相关信息
        PublisherIdRequest publisherIdRequest=new PublisherIdRequest();
        publisherIdRequest.setUserId(userId);
        RpcResult<TaskCountResponse> publishTaskCountRpcResult = publishService.getTaskCountByPublisherId(publisherIdRequest);
        if(publishTaskCountRpcResult.getData()==null){
            return HttpResultUtil.fail(publishTaskCountRpcResult.getMessage());
        }
        userVO.setPublishTaskNum(publishTaskCountRpcResult.getData().getTaskCount());

        //添加用户接受任务的相关信息
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setWorkerId(userId);
        RpcResult<Integer> workerAcceptedTaskNumber = testService.getWorkerAcceptedTaskNumber(testQueryRequest);
        if(workerAcceptedTaskNumber.getData()==null){
            return HttpResultUtil.fail(workerAcceptedTaskNumber.getMessage());
        }
        userVO.setAcceptTaskNum(workerAcceptedTaskNumber.getData());

        return HttpResultUtil.success(userVO);
    }

    @Override
    public HttpResult<UserVO> getVerificationCode(String username) {
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName(username);
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);
        userService.getVerificationCode(userInfoRequest);
        return HttpResultUtil.success();
    }


}
