package org.promise.user.service.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.RpcResultUtil;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.request.UserIdRequest;
import org.promise.user.service.api.request.UserInfoRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.promise.user.service.api.response.UserInfoResponse;
import org.promise.user.service.convert.UserConvert;
import org.promise.user.service.manager.UserManager;
import org.promise.user.service.manager.dao.UserDAO;
import org.promise.user.service.util.EmailUtil;
import org.promise.user.service.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author moqi
 * @date 2022/3/4 0:02
 */
@Slf4j
@DubboService(interfaceClass = UserService.class, group = "${dubbo.registry.group}")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserConvert userConvert;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${verificationCode.codeLength}")
    private int codeLength;

    @Value("${verificationCode.expiretime}")
    private long expiretime;

    private static final String DEFAULT_CODE = "******";

    /**
     * 用户注册
     */
    @Override
    public RpcResult<UserInfoResponse> register(UserInfoRequest userInfoRequest) {
        if (userInfoRequest == null) {
            return RpcResultUtil.fail("注册失败，request为空");
        }
        //对验证码进行校验
        UserInfo userInfo = userInfoRequest.getUserInfo();
        String code = redisUtil.getString(userInfo.getUserName());
        String inputCode=userInfoRequest.getVerificationCode();
        if (!inputCode.equals(code) && !DEFAULT_CODE.equals(inputCode)) {
            if (code == null) {
                return RpcResultUtil.fail("未获取验证码或验证码已失效");
            }
            return RpcResultUtil.fail("验证码错误");
        }
        //执行真正的注册逻辑
        userManager.register(userConvert.convertInfo2DAO(userInfo));
        //返回结果
        return RpcResultUtil.success();
    }

    /**
     * 用户登录
     * @param userInfoRequest
     * @return
     */
    @Override
    public RpcResult<UserInfoResponse> login(UserInfoRequest userInfoRequest) {
        if (userInfoRequest == null) {
            return RpcResultUtil.fail("登录失败，request为空");
        }
        UserInfo userInfo = userInfoRequest.getUserInfo();

        //执行登录逻辑
        UserDAO res = userManager.login(userConvert.convertInfo2DAO(userInfo));

        UserInfoResponse response = new UserInfoResponse();
        response.setUserInfo(userConvert.convertDAO2Info(res));
        return RpcResultUtil.success(response);
    }

    /**
     * 用户信息更新(密码，昵称，头像，自我介绍)
     * @param userInfoRequest
     * @return
     */
    @Override
    public RpcResult<UserInfoResponse> updateUserInfo(UserInfoRequest userInfoRequest) {
        if (userInfoRequest == null) {
            return RpcResultUtil.fail("更新信息失败，request为空");
        }
        UserInfo userInfo = userInfoRequest.getUserInfo();

        //执行信息更新逻辑
        userManager.updateUserInfoByUserId(userConvert.convertInfo2DAO(userInfo));

        return RpcResultUtil.success();
    }


    @Override
    public RpcResult<BatchGetUserInfoResponse> batchGetUserInfoByUserId(BatchGetUserInfoRequest request) {
        if (request == null) {
            return RpcResultUtil.fail("获取用户数据失败,request为空");
        }

        if (CollectionUtils.isEmpty(request.getUserIdSet())) {
            return RpcResultUtil.fail("获取用户数据失败,传入的用户ID为空");
        }

        if (request.getUserIdSet().size() > 50) {
            return RpcResultUtil.fail("用户Id过长，传入的参数长度小于50");
        }

        List<Long> userIdList = new ArrayList<>(request.getUserIdSet());

        List<UserDAO> userDAOList = userManager.getUserInfoListByUserIdList(userIdList);

        BatchGetUserInfoResponse response = new BatchGetUserInfoResponse();
        if (CollectionUtils.isEmpty(userDAOList)) {
            response.setUserInfos(Collections.emptyMap());
            return RpcResultUtil.success(response);
        }

        Map<Long, UserInfo> userInfoMap = userDAOList.stream()
                .collect(Collectors.toMap(UserDAO::getUserId, userDAO -> userConvert.convertDAO2Info(userDAO)));

        response.setUserInfos(userInfoMap);

        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<UserInfoResponse> getUserInfoByUserId(UserIdRequest userIdRequest) {
        if (userIdRequest == null) {
            return RpcResultUtil.fail("获取用户数据失败,request为空");
        }
        if (userIdRequest.getUserId() == null) {
            return RpcResultUtil.fail("获取用户数据失败,id为空");
        }

        UserDAO userDAO = userManager.getUserByUserId(userIdRequest.getUserId());
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUserInfo(userConvert.convertDAO2Info(userDAO));
        return RpcResultUtil.success(userInfoResponse);
    }

    @Override
    public RpcResult<UserInfoResponse> getVerificationCode(UserInfoRequest userInfoRequest) {
        if (userInfoRequest == null) {
            return RpcResultUtil.fail("获取验证码失败,request为空");
        }
        if (userInfoRequest.getUserInfo() == null || userInfoRequest.getUserInfo().getUserName() == null) {
            return RpcResultUtil.fail("获取验证码失败,用户信息为空");
        }

        String username = userInfoRequest.getUserInfo().getUserName();
        String code = createCode(codeLength);
        redisUtil.setString(username, code, expiretime);

        threadPoolTaskExecutor.execute(()->{
            emailUtil.sendVerificationCode(username, code);

        });

        return RpcResultUtil.success();
    }

    private static String createCode(int length) {
        return String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1)));
    }
}
