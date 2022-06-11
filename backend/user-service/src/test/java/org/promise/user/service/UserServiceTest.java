package org.promise.user.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.JsonUtil;
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
import org.promise.user.service.mapperService.po.UserPO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 16:13
 * @description:
 */
@SpringBootTest
@Slf4j
@ExtendWith(SpringExtension.class)
@Transactional
public class UserServiceTest {

    private static final String SUCCESS="success";

    @Resource
    private UserConvert userConvert;

    @Resource
    private UserService userService;

    @Resource
    private UserManager userManager;


    @Test
    public void testUserConvert1(){
        UserInfo userInfo=new UserInfo ();
        userInfo.setUserId (1L);
        userInfo.setUserName ("hxq");
        userInfo.setPassword ("hxq200001028");
        ArrayList<String> role=new ArrayList<>();
        role.add("publisher");
        userInfo.setRole (role);
        userInfo.setNickName ("琪琪");
        userInfo.setAvatar ("http:hxq.world.com");
        userInfo.setIntroduction ("帅气男孩");
        log.info ("将UserInfo转换为UserDAO: {}",userConvert.convertInfo2DAO (userInfo));
    }

    @Test
    public void testUserConvert2(){
        UserDAO userDAO=new UserDAO ();
        userDAO.setUserId (3L);
        userDAO.setUserName ("毛梦娟");
        userDAO.setPassword ("mmjsadasdsad");
        ArrayList<String> role=new ArrayList<>();
        role.add("publisher");
        userDAO.setRole (role);
        userDAO.setNickName ("毛菜菜");
        userDAO.setAvatar ("傻子");
        userDAO.setIntroduction ("傻狗毛菜菜");
        log.info ("将UserDAO转换为UserInfo: {}",userConvert.convertDAO2Info (userDAO));

        log.info ("将UserDAO转换为UserPO: {}",userConvert.convertDAO2PO (userDAO));
    }

    @Test
    public void testUserConvert3(){
        UserPO userPO=new UserPO ();
        userPO.setUserId (5L);
        userPO.setUserName ("zly");
        userPO.setPassword ("zlywad");
        ArrayList<String> role=new ArrayList<>();
        role.add("publisher");
        userPO.setRole(JsonUtil.toJson(role));
        userPO.setNickName ("洋洋");
        userPO.setAvatar ("http://dasd");
        userPO.setIntroduction ("无敌的洋洋哥哥");
        userPO.setCreateTime (LocalDateTime.now ());
        userPO.setUpdateTime ( LocalDateTime.now ());
        log.info ("将UserPO转换为UserDAO: {}",userConvert.convertPO2DAO (userPO));
    }


    @Test
    public void testRegisterSuccess(){
        UserInfoRequest request=new UserInfoRequest ();
        UserInfo userInfo=new UserInfo ();
        userInfo.setUserName ("黄相淇");
        userInfo.setPassword ("hxq20001028");
        userInfo.setNickName ("琪琪");
        ArrayList<String> role=new ArrayList<>();
        role.add("publisher");
        userInfo.setRole (role);
        request.setUserInfo (userInfo);
        request.setVerificationCode("******");
        RpcResult<UserInfoResponse> register = userService.register(request);
        Assertions.assertEquals(register.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    public void testBatchGetUserInfoByUserId(){
        BatchGetUserInfoRequest request=new BatchGetUserInfoRequest ();
        Set<Long> userIdSet=new HashSet<> ();
        userIdSet.add (1L);
        userIdSet.add (242L);
        userIdSet.add (243L);
        userIdSet.add (244L);
        request.setUserIdSet (userIdSet);
        RpcResult<BatchGetUserInfoResponse> batchGetUserInfoResponseRpcResult = userService.batchGetUserInfoByUserId(request);
        Assertions.assertEquals(batchGetUserInfoResponseRpcResult.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    public void testLogAnnotation(){
        log.info ("{}",userManager.getUserInfoListByUserIdList (Lists.list (1L,2L,3L )));
    }

    @Test
    void testRegisterMultiRole(){
        UserInfoRequest request=new UserInfoRequest ();
        UserInfo userInfo=new UserInfo ();
        userInfo.setUserName ("test@test.com");
        userInfo.setPassword ("123456");
        userInfo.setNickName ("mm");
        ArrayList<String> role=new ArrayList<>();
        role.add("publisher");
        userInfo.setRole (role);
        request.setUserInfo(userInfo);
        request.setVerificationCode("******");
        RpcResult<UserInfoResponse> rpcResult = userService.register(request);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);

        role=new ArrayList<>();
        role.add("worker");
        userInfo.setRole (role);
        rpcResult = userService.register(request);
        Assertions.assertEquals(rpcResult.getMessage(),SUCCESS);

        rpcResult = userService.register(request);
        Assertions.assertEquals(rpcResult.getMessage(),"当前用户已经存在");
    }

    @Test
    void loginTest(){
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName("1402290478@qq.com");
        userInfo.setPassword("123456");
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);
        RpcResult<UserInfoResponse> login = userService.login(userInfoRequest);
        Assertions.assertEquals(login.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void updateUserInfoTest(){
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName("1402290478@qq.com");
        userInfo.setPassword("123456");
        userInfo.setUserId(375L);
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);
        RpcResult<UserInfoResponse> login = userService.updateUserInfo(userInfoRequest);
        Assertions.assertEquals(login.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getUserInfoByUserIdTest(){
        UserIdRequest userIdRequest=new UserIdRequest(375L);
        RpcResult<UserInfoResponse> userInfoByUserId = userService.getUserInfoByUserId(userIdRequest);
        Assertions.assertEquals(userInfoByUserId.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getVerificationCodeTest(){
        UserInfo userInfo=new UserInfo();
        userInfo.setUserName("1402290478@qq.com");
        UserInfoRequest userInfoRequest=new UserInfoRequest();
        userInfoRequest.setUserInfo(userInfo);
        RpcResult<UserInfoResponse> verificationCode = userService.getVerificationCode(userInfoRequest);
        Assertions.assertEquals(verificationCode.getCode(),RpcCodes.SUCCESS.getCode());
    }
}
