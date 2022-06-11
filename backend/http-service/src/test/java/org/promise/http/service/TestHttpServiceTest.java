package org.promise.http.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.LocalDateTimeUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.http.service.httpService.TestHttpService;
import org.promise.http.service.vo.test.HighEvaluationTestVO;
import org.promise.http.service.vo.test.TestCollaborationRecordVO;
import org.promise.http.service.vo.test.TestVO;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.request.TaskIdRequest;
import org.promise.publish.service.api.response.TaskMaxWorkerCountResponse;
import org.promise.publish.service.api.response.TaskTimeResponse;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.CollaborationInfo;
import org.promise.test.service.api.info.FinishedTestInfo;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.api.request.*;
import org.promise.test.service.api.response.*;
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
 * @date 2022/4/1 18:08
 */
@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
public class TestHttpServiceTest {

    @Resource
    private TestHttpService testHttpService;


    @Test
    void createTestTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = testHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(testHttpService, publishService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TaskIdRequest taskIdRequest = new TaskIdRequest(1L);
        TaskMaxWorkerCountResponse taskMaxWorkerCountResponse = new TaskMaxWorkerCountResponse(10);
        when(publishService.getMaxWorkerCountByTaskId(taskIdRequest)).thenReturn(RpcResultUtil.success(taskMaxWorkerCountResponse));

        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setTaskId(1L);
        createTestRequest.setWorkerId(1L);
        createTestRequest.setAllowedMaxWorkerCount(10);
        when(testService.createTest(createTestRequest)).thenReturn(RpcResultUtil.success());

        TestVO testVO = new TestVO();
        testVO.setTestId(1L);
        testVO.setWorkerId(1L);
        testVO.setTaskId(1L);
        Assertions.assertEquals(testHttpService.createTest(testVO).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void whetherWorkerHasAcceptTaskTest() throws NoSuchFieldException, IllegalAccessException {
        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        WhetherWorkerHasAcceptTaskRequest whetherWorkerHasAcceptTaskRequest =
                new WhetherWorkerHasAcceptTaskRequest();
        whetherWorkerHasAcceptTaskRequest.setTaskId(1L);
        whetherWorkerHasAcceptTaskRequest.setWorkerId(1L);
        when(testService.whetherWorkerHasAcceptTask(whetherWorkerHasAcceptTaskRequest)).thenReturn(RpcResultUtil.success());

        Assertions.assertEquals(testHttpService.whetherWorkerHasAcceptTask(1L, 1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void cancelTestTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = testHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(testHttpService, publishService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(1L);
        TestInfoResponse testInfoResponse = new TestInfoResponse();
        List<TestInfo> testInfoList = new ArrayList<>();
        TestInfo testInfo = new TestInfo();
        testInfo.setTestId(1L);
        testInfo.setWorkerId(1L);
        testInfo.setTaskId(1L);
        testInfoList.add(testInfo);
        testInfoResponse.setTestInfoList(testInfoList);
        when(testService.getSimpleTestInfoByTestId(testQueryRequest)).thenReturn(RpcResultUtil.success(testInfoResponse));

        TaskIdRequest taskIdRequest = new TaskIdRequest(1L);
        TaskTimeResponse taskTimeResponse = new TaskTimeResponse();
        String allowCancelTime = LocalDateTimeUtil.asString(LocalDateTime.now());
        taskTimeResponse.setAllowCancelTime(allowCancelTime);
        when(publishService.getAllowCancelTimeByTaskId(taskIdRequest)).thenReturn(RpcResultUtil.success(taskTimeResponse));

        CancelTestRequest cancelTestRequest = new CancelTestRequest(1L, 1L, allowCancelTime);
        when(testService.cancelTest(cancelTestRequest)).thenReturn(RpcResultUtil.success());

        Assertions.assertEquals(testHttpService.cancelTest(1L, 1L).getCode(), HttpCodes.SUCCESS.getCode());
    }


    @Test
    void getTestByTestIdTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = testHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(testHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(1L);
        List<TestInfo> testInfoList = new ArrayList<>();
        TestInfo testInfo = new TestInfo();
        testInfo.setTestId(1L);
        testInfo.setTaskId(1L);
        testInfo.setWorkerId(1L);
        List<TestCollaborationRecordInfo> testCollaborationRecordInfoList = new ArrayList<>();
        TestCollaborationRecordInfo testCollaborationRecordInfo = new TestCollaborationRecordInfo();
        testCollaborationRecordInfo.setTestId(1L);
        testCollaborationRecordInfo.setWorkerId(2L);
        testInfo.setTestCollaborationRecordList(testCollaborationRecordInfoList);
        testInfoList.add(testInfo);
        TestInfoResponse testInfoResponse = new TestInfoResponse();
        testInfoResponse.setTestInfoList(testInfoList);
        when(testService.getTestInfoByTestId(testQueryRequest)).thenReturn(RpcResultUtil.success(testInfoResponse));

        List<CollaborationInfo> collaborationInfoList=new ArrayList<>();
        CollaborationInfo collaborationInfo=new CollaborationInfo();
        collaborationInfo.setId(1L);
        collaborationInfo.setTestId(1L);
        collaborationInfo.setWorkerId(2L);
        collaborationInfoList.add(collaborationInfo);
        when(testService.getSimpleCollaborationListByTestId(testQueryRequest))
                .thenReturn(RpcResultUtil.success(new CollaborationInfoListResponse(collaborationInfoList)));

        Set<Long> workerIdSet = new HashSet<>();
        workerIdSet.add(2L);
        workerIdSet.add(1L);
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(workerIdSet);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(2L, new UserInfo());
        userInfoMap.put(1L,new UserInfo());
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(batchGetUserInfoRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(testHttpService.getTestByTestId(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }


    @Test
    void submitTestTest() throws NoSuchFieldException, IllegalAccessException {
        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = testHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(testHttpService, publishService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(1L);
        TestInfoResponse testInfoResponse = new TestInfoResponse();
        List<TestInfo> testInfoList = new ArrayList<>();
        TestInfo testInfo = new TestInfo();
        testInfo.setTestId(1L);
        testInfo.setWorkerId(1L);
        testInfo.setTaskId(1L);
        testInfoList.add(testInfo);
        testInfoResponse.setTestInfoList(testInfoList);
        when(testService.getSimpleTestInfoByTestId(testQueryRequest)).thenReturn(RpcResultUtil.success(testInfoResponse));

        TaskIdRequest taskIdRequest = new TaskIdRequest(1L);
        TaskTimeResponse taskTimeResponse = new TaskTimeResponse();
        String time = LocalDateTimeUtil.asString(LocalDateTime.now());
        taskTimeResponse.setTestEndTime(time);
        when(publishService.getTaskEndTimeByTaskId(taskIdRequest)).thenReturn(RpcResultUtil.success(taskTimeResponse));

        SubmitTestRequest submitTestRequest = new SubmitTestRequest();
        submitTestRequest.setTestId(1L);
        submitTestRequest.setWorkerId(1L);
        submitTestRequest.setLastSubmitTime(time);
        submitTestRequest.setTestTools(new ArrayList<>());
        submitTestRequest.setBugImgList(new ArrayList<>());
        submitTestRequest.setTestCaseList(new ArrayList<>());
        when(testService.submitTest(submitTestRequest)).thenReturn(RpcResultUtil.success());

        TestVO testVO = new TestVO();
        testVO.setTestId(1L);
        testVO.setWorkerId(1L);
        testVO.setTaskId(1L);
        Assertions.assertEquals(testHttpService.submitTest(testVO).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void submitCollaborationRecordTest() throws NoSuchFieldException, IllegalAccessException {
        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        SubmitCollaborationRecordRequest submitCollaborationRecordRequest =
                new SubmitCollaborationRecordRequest(new TestCollaborationRecordInfo());
        when(testService.submitCollaborationRecord(submitCollaborationRecordRequest)).thenReturn(RpcResultUtil.success());

        Assertions.assertEquals(testHttpService.submitCollaborationRecord(new TestCollaborationRecordVO()).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getTestInfoWithHighEvaluationByTaskIdTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = testHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(testHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        FinishedTestInfoResponse finishedTestInfoResponse=new FinishedTestInfoResponse();
        List<FinishedTestInfo> finishedTestInfolist =new ArrayList<>();
        FinishedTestInfo finishedTestInfo=new FinishedTestInfo();
        finishedTestInfo.setTestId(1L);
        finishedTestInfo.setWorkerId(1L);
        finishedTestInfolist.add(finishedTestInfo);
        finishedTestInfoResponse.setHighFinishedTestInfoList(finishedTestInfolist);
        when(testService.getTestInfoWithHighEvaluationByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(finishedTestInfoResponse));

        Set<Long> workerIdSet = new HashSet<>();
        workerIdSet.add(1L);
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(workerIdSet);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(1L, new UserInfo());
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(batchGetUserInfoRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(testHttpService.getTestInfoWithHighEvaluationByTaskId(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getSimpleTestByTaskIdTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = testHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(testHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        FinishedTestInfoResponse finishedTestInfoResponse=new FinishedTestInfoResponse();
        List<FinishedTestInfo> finishedTestInfolist =new ArrayList<>();
        FinishedTestInfo finishedTestInfo=new FinishedTestInfo();
        finishedTestInfo.setTestId(1L);
        finishedTestInfo.setWorkerId(1L);
        finishedTestInfolist.add(finishedTestInfo);
        finishedTestInfoResponse.setHighFinishedTestInfoList(finishedTestInfolist);
        when(testService.getSimpleFinishedTestByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(finishedTestInfoResponse));

        Set<Long> workerIdSet = new HashSet<>();
        workerIdSet.add(1L);
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(workerIdSet);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(1L, new UserInfo());
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(batchGetUserInfoRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(testHttpService.getSimpleTestByTaskId(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void submitCollaborationTest() throws NoSuchFieldException, IllegalAccessException {
        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        SubmitCollaborationRequest submitCollaborationRequest=new SubmitCollaborationRequest();
        when(testService.submitCollaboration(submitCollaborationRequest)).thenReturn(RpcResultUtil.success());

        Assertions.assertEquals(testHttpService.submitCollaboration(null).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getCollaborationListByTestIdTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = testHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(testHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(1L);
        List<CollaborationInfo> collaborationInfoList = new ArrayList<>();
        CollaborationInfo collaborationInfo = new CollaborationInfo();
        collaborationInfo.setTestId(1L);
        collaborationInfo.setId(1L);
        collaborationInfo.setWorkerId(1L);
        collaborationInfoList.add(collaborationInfo);
        CollaborationInfoListResponse collaborationInfoListResponse = new CollaborationInfoListResponse();
        collaborationInfoListResponse.setCollaborationInfoList(collaborationInfoList);
        when(testService.getSimpleCollaborationListByTestId(testQueryRequest)).thenReturn(RpcResultUtil.success(collaborationInfoListResponse));

        Set<Long> workerIdSet = new HashSet<>();
        workerIdSet.add(1L);
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(workerIdSet);
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(1L, new UserInfo());
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(batchGetUserInfoRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(testHttpService.getCollaborationListByTestId(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getCollaborationByIdTest() throws NoSuchFieldException, IllegalAccessException {
        UserService userService = mock(UserService.class);
        Field userServiceField = testHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(testHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = testHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(testHttpService, testService);

        CollaborationIdRequest collaborationIdRequest=new CollaborationIdRequest(1L);
        CollaborationInfo collaborationInfo = new CollaborationInfo();
        collaborationInfo.setTestId(1L);
        collaborationInfo.setId(1L);
        collaborationInfo.setWorkerId(1L);
        CollaborationInfoResponse collaborationInfoResponse = new CollaborationInfoResponse();
        collaborationInfoResponse.setCollaborationInfo(collaborationInfo);
        when(testService.getCollaborationById(collaborationIdRequest)).thenReturn(RpcResultUtil.success(collaborationInfoResponse));

        UserIdRequest userIdRequest = new UserIdRequest(1L);
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1L);
        userInfo.setAvatar("avatar");
        userInfo.setNickName("nickname");
        userInfoResponse.setUserInfo(userInfo);
        when(userService.getUserInfoByUserId(userIdRequest)).thenReturn(RpcResultUtil.success(userInfoResponse));

        Assertions.assertEquals(testHttpService.getCollaborationById(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }

}
