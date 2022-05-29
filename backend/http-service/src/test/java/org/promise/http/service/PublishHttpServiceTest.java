package org.promise.http.service;

import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.result.page.Page;
import org.promise.common.util.HttpResultUtil;
import org.promise.common.util.LocalDateTimeUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.http.service.controller.PublishController;
import org.promise.http.service.httpService.PublishHttpService;
import org.promise.http.service.vo.publish.TaskFileVO;
import org.promise.http.service.vo.publish.TaskVO;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskFileInfo;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.*;
import org.promise.publish.service.api.response.TaskListResponse;
import org.promise.publish.service.api.response.TaskMapResponse;
import org.promise.publish.service.api.response.TaskResponse;
import org.promise.publish.service.api.response.TaskStateResponse;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.api.request.GetWorkerTestInfoRequest;
import org.promise.test.service.api.response.TestInfoResponse;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.request.UserIdRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.promise.user.service.api.response.UserInfoResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * @author moqi
 * @date 2022/4/1 14:51
 */
@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
public class PublishHttpServiceTest {


    @Resource
    private PublishHttpService publishHttpService;

    private PublishController publishController;

    @Test
    void publishTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        TaskVO taskVO=new TaskVO();
        TaskInfo taskInfo=new TaskInfo();
        PublishRequest publishRequest=new PublishRequest();
        publishRequest.setTaskInfo(taskInfo);
        when(publishService.publish(publishRequest)).thenReturn(RpcResultUtil.success());

        Assertions.assertEquals(publishHttpService.publish(taskVO),HttpResultUtil.success());
    }

    @Test
    void updateTaskTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        TaskVO taskVO = new TaskVO();
        TaskInfo taskInfo = new TaskInfo();
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setTaskInfo(taskInfo);
        when(publishService.updateTaskInfo(publishRequest)).thenReturn(RpcResultUtil.success());

        Assertions.assertEquals(publishHttpService.updateTask(taskVO), HttpResultUtil.success());
    }

    @Test
    void publishFileTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        TaskFileInfo taskFileInfo = new TaskFileInfo();
        TaskFileVO taskFileVO = new TaskFileVO();
        ArrayList<TaskFileInfo> taskFileInfoArrayList = new ArrayList<>();
        taskFileInfoArrayList.add(taskFileInfo);
        PublishFileRequest publishFileRequest = new PublishFileRequest();
        publishFileRequest.setTaskFileInfos(taskFileInfoArrayList);
        when(publishService.publishFile(publishFileRequest)).thenReturn(RpcResultUtil.success());
        Assertions.assertEquals(publishHttpService.publishFile(taskFileVO), HttpResultUtil.success());
    }

    @Test
    void getTaskListByPublisherIdTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        UserService userService = mock(UserService.class);
        Field userServiceField = publishHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(publishHttpService, userService);

        UserIdAndPageRequest userIdAndPageRequest = new UserIdAndPageRequest();
        userIdAndPageRequest.setUserId(1L);
        userIdAndPageRequest.setJudgeType(0);
        userIdAndPageRequest.setPage(new Page());
        TaskListResponse taskListResponse = new TaskListResponse();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(1L);
        taskInfo.setTaskName("name");
        taskInfo.setPublisherId(1L);
        taskInfoList.add(taskInfo);
        Page page = new Page();
        page.setTotal(1);
        taskListResponse.setTaskInfoList(taskInfoList);
        taskListResponse.setPage(page);
        when(publishService.getTasksByPublisherId(userIdAndPageRequest)).thenReturn(RpcResultUtil.success(taskListResponse));


        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userInfosRequest.setUserIdSet(userIds);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("avatar");
        userInfo.setNickName("nickname");
        userInfoMap.put(1L, userInfo);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(publishHttpService.getTaskListByPublisherId(1L, 0, new Page()).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getTaskByIdTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        UserService userService = mock(UserService.class);
        Field userServiceField = publishHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(publishHttpService, userService);

        TaskIdRequest taskIdRequest = new TaskIdRequest();
        taskIdRequest.setTaskId(1L);
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(1L);
        taskInfo.setTaskName("name");
        taskInfo.setPublisherId(1L);
        TaskResponse taskResponse = new TaskResponse(taskInfo);
        when(publishService.getTaskById(taskIdRequest)).thenReturn(RpcResultUtil.success(taskResponse));

        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(1L);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1L);
        userInfo.setNickName("nickname");
        userInfo.setAvatar("avatar");
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUserInfo(userInfo);
        when(userService.getUserInfoByUserId(userIdRequest)).thenReturn(RpcResultUtil.success(userInfoResponse));

        Assertions.assertEquals(publishHttpService.getTaskById(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getRunningTasksTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        UserService userService = mock(UserService.class);
        Field userServiceField = publishHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(publishHttpService, userService);

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(new Page());
        TaskListResponse taskListResponse = new TaskListResponse();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(1L);
        taskInfo.setTaskName("name");
        taskInfo.setPublisherId(1L);
        taskInfoList.add(taskInfo);
        Page page = new Page();
        page.setTotal(1);
        taskListResponse.setTaskInfoList(taskInfoList);
        taskListResponse.setPage(page);
        when(publishService.getTasksRunning(pageRequest)).thenReturn(RpcResultUtil.success(taskListResponse));


        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userInfosRequest.setUserIdSet(userIds);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("avatar");
        userInfo.setNickName("nickname");
        userInfoMap.put(1L, userInfo);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(publishHttpService.getRunningTasks(new Page()).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getAllTasksTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        UserService userService = mock(UserService.class);
        Field userServiceField = publishHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(publishHttpService, userService);

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(new Page());
        TaskListResponse taskListResponse = new TaskListResponse();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(1L);
        taskInfo.setTaskName("name");
        taskInfo.setPublisherId(1L);
        taskInfoList.add(taskInfo);
        Page page = new Page();
        page.setTotal(1);
        taskListResponse.setTaskInfoList(taskInfoList);
        taskListResponse.setPage(page);
        when(publishService.getAllTasks(pageRequest)).thenReturn(RpcResultUtil.success(taskListResponse));


        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userInfosRequest.setUserIdSet(userIds);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("avatar");
        userInfo.setNickName("nickname");
        userInfoMap.put(1L, userInfo);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(publishHttpService.getAllTasks(new Page()).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getTaskListByWorkerIdTest() throws IllegalAccessException, NoSuchFieldException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        UserService userService = mock(UserService.class);
        Field userServiceField = publishHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(publishHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = publishHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(publishHttpService, testService);

        GetWorkerTestInfoRequest workerTestInfoRequest = new GetWorkerTestInfoRequest();
        workerTestInfoRequest.setWorkerId(1L);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("running");
        arrayList.add("finish");
        workerTestInfoRequest.setStateList(arrayList);
        workerTestInfoRequest.setPage(new Page(1, Integer.MAX_VALUE));
        TestInfoResponse testInfoResponse = new TestInfoResponse();
        List<TestInfo> testInfoList = new ArrayList<>();
        TestInfo testInfo = new TestInfo();
        testInfo.setTestId(1L);
        testInfo.setTaskId(1L);
        testInfo.setWorkerId(1L);
        testInfoList.add(testInfo);
        testInfoResponse.setTestInfoList(testInfoList);
        Page page = new Page();
        page.setTotal(1);
        testInfoResponse.setPage(page);
        when(testService.getWorkerTestInfo(workerTestInfoRequest)).thenReturn(RpcResultUtil.success(testInfoResponse));

        TaskIdsRequest taskIdsRequest = new TaskIdsRequest();
        List<Long> taskIdList = new ArrayList<>();
        taskIdList.add(1L);
        taskIdsRequest.setTaskIds(taskIdList);
        taskIdsRequest.setType(0);
        TaskMapResponse taskMapResponse = new TaskMapResponse();
        Map<Long, TaskInfo> taskInfoMap = new HashMap<>();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(1L);
        taskInfo.setPublisherId(1L);
        taskInfoMap.put(1L, taskInfo);
        taskMapResponse.setTaskInfoMap(taskInfoMap);
        when(publishService.getTaskMapByTaskIds(taskIdsRequest)).thenReturn(RpcResultUtil.success(taskMapResponse));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userInfosRequest.setUserIdSet(userIds);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("avatar");
        userInfo.setNickName("nickname");
        userInfoMap.put(1L, userInfo);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(publishHttpService.getTaskListByWorkerId(1L, 0, new Page(1, 8)).getCode(),
                HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getTaskStateByTaskIdTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = publishHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(publishHttpService, publishService);

        TaskIdRequest taskIdRequest = new TaskIdRequest();
        taskIdRequest.setTaskId(1L);
        TaskStateResponse taskStateResponse = new TaskStateResponse();
        taskStateResponse.setSubmitted(true);
        when(publishService.getTaskStateByTaskId(taskIdRequest)).thenReturn(RpcResultUtil.success(taskStateResponse));

        Assertions.assertEquals(publishHttpService.getTaskStateByTaskId(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }
}
