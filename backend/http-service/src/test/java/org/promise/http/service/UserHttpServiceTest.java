package org.promise.http.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.RpcResultUtil;
import org.promise.http.service.httpService.UserHttpService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @author moqi
 * @date 2022/4/1 20:45
 */
@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
public class UserHttpServiceTest {

    @Resource
    private UserHttpService userHttpService;

    @Test
    void registerTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = userHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userHttpService, userService);

        UserInfoRequest userInfoRequest1=new UserInfoRequest();
        userInfoRequest1.setUserInfo(new UserInfo());
        UserInfoRequest userInfoRequest2=new UserInfoRequest();
        when(userService.register(userInfoRequest1)).thenReturn(RpcResultUtil.success());
        when(userService.register(userInfoRequest2)).thenReturn(RpcResultUtil.fail(""));

        Assertions.assertEquals(userHttpService.register(new UserRegisterVO()).getCode(), HttpCodes.SUCCESS.getCode());
        Assertions.assertNotEquals(userHttpService.register(null).getCode(),HttpCodes.SUCCESS.getCode());

    }

    @Test
    void login() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = userHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userHttpService, userService);

        UserInfoRequest userInfoRequest1=new UserInfoRequest();
        userInfoRequest1.setUserInfo(new UserInfo());
        UserInfoRequest userInfoRequest2=new UserInfoRequest();
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(1L);
        userInfo.setRole(new ArrayList<>());
        UserInfoResponse userInfoResponse=new UserInfoResponse();
        userInfoResponse.setUserInfo(userInfo);
        when(userService.login(userInfoRequest1)).thenReturn(RpcResultUtil.success(userInfoResponse));
        when(userService.login(userInfoRequest2)).thenReturn(RpcResultUtil.fail(""));

        Assertions.assertEquals(userHttpService.login(new UserVO()).getCode(), HttpCodes.SUCCESS.getCode());
        Assertions.assertNotEquals(userHttpService.login(null).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void updateTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = userHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userHttpService, userService);

        UserInfoRequest userInfoRequest1=new UserInfoRequest();
        userInfoRequest1.setUserInfo(new UserInfo());
        UserInfoRequest userInfoRequest2=new UserInfoRequest();
        when(userService.updateUserInfo(userInfoRequest1)).thenReturn(RpcResultUtil.success());
        when(userService.updateUserInfo(userInfoRequest2)).thenReturn(RpcResultUtil.fail(""));

        Assertions.assertEquals(userHttpService.update(new UserVO()).getCode(), HttpCodes.SUCCESS.getCode());
        Assertions.assertNotEquals(userHttpService.update(null).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getUserTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService=mock(PublishService.class);
        Field publishServiceField = userHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(userHttpService,publishService);

        UserService userService=mock(UserService.class);
        Field userServiceField = userHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userHttpService,userService);

        TestService testService=mock(TestService.class);
        Field testServiceField = userHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(userHttpService,testService);

        UserIdRequest userIdRequest=new UserIdRequest();
        userIdRequest.setUserId(1L);
        UserInfo userInfo =new UserInfo();
        userInfo.setUserId(1L);
        UserInfoResponse userInfoResponse=new UserInfoResponse();
        userInfoResponse.setUserInfo(userInfo);
        when(userService.getUserInfoByUserId(userIdRequest)).thenReturn(RpcResultUtil.success(userInfoResponse));

        PublisherIdRequest publisherIdRequest=new PublisherIdRequest();
        publisherIdRequest.setUserId(1L);
        TaskCountResponse taskCountResponse=new TaskCountResponse();
        taskCountResponse.setTaskCount(1);
        when(publishService.getTaskCountByPublisherId(publisherIdRequest)).thenReturn(RpcResultUtil.success(taskCountResponse));

        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setWorkerId(1L);
        when(testService.getWorkerAcceptedTaskNumber(testQueryRequest)).thenReturn(RpcResultUtil.success(1));

        Assertions.assertEquals(userHttpService.getUser(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }
}
