package org.promise.http.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.util.JsonUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.http.service.httpService.RecommendHttpService;
import org.promise.http.service.httpService.dto.TaskSimilarityDTO;
import org.promise.http.service.httpService.dto.TestSimilarityDTO;
import org.promise.http.service.util.HttpRequestUtil;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.TaskIdsRequest;
import org.promise.publish.service.api.response.TaskMapResponse;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.FinishedTestInfo;
import org.promise.test.service.api.request.TestBatchQueryRequest;
import org.promise.test.service.api.response.FinishedTestInfoMapResponse;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author moqi
 * @date 2022/4/1 21:07
 */

@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
public class RecommendHttpServiceTest {

    @Resource
    private RecommendHttpService recommendHttpService;

    @Resource
    private HttpRequestUtil httpRequestUtil;

    @Test
    void recommendTestTest() throws Exception {
        UserService userService = mock(UserService.class);
        Field userServiceField = recommendHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(recommendHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = recommendHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(recommendHttpService, testService);

        HttpRequestUtil httpRequestUtil=mock(HttpRequestUtil.class);
        Field httpRequestUtilField=recommendHttpService.getClass().getDeclaredField("httpRequestUtil");
        httpRequestUtilField.setAccessible(true);
        httpRequestUtilField.set(recommendHttpService,httpRequestUtil);

        List<TestSimilarityDTO> testSimilarityDTOList=new ArrayList<>();
        testSimilarityDTOList.add(new TestSimilarityDTO(1L,0.3));
        testSimilarityDTOList.add(new TestSimilarityDTO(2L,0.4));
        testSimilarityDTOList.add(new TestSimilarityDTO(3L,0.6));
        testSimilarityDTOList.add(new TestSimilarityDTO(7L,0.7));
        when(httpRequestUtil.requestTest(1L,1L)).thenReturn(JsonUtil.toJson(testSimilarityDTOList));

        Set<Long> testIds=new HashSet<>();
        testIds.add(2L);
        testIds.add(3L);
        TestBatchQueryRequest testBatchQueryRequest=new TestBatchQueryRequest(testIds);
        FinishedTestInfoMapResponse finishedTestInfoMapResponse=new FinishedTestInfoMapResponse();
        Map<Long, FinishedTestInfo> finishedTestInfoMap=new HashMap<>();
        FinishedTestInfo finishedTestInfo1=new FinishedTestInfo();
        finishedTestInfo1.setWorkerId(2L);
        finishedTestInfoMap.put(2L,finishedTestInfo1);
        FinishedTestInfo finishedTestInfo2=new FinishedTestInfo();
        finishedTestInfo2.setWorkerId(3L);
        finishedTestInfoMap.put(3L,finishedTestInfo2);
        finishedTestInfoMapResponse.setMap(finishedTestInfoMap);
        when(testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest)).thenReturn(RpcResultUtil.success(finishedTestInfoMapResponse));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(3L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse=new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap=new HashMap<>();
        userInfoMap.put(2L,new UserInfo());
        userInfoMap.put(3L,new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(recommendHttpService.recommendTest(1L,1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void recommendTaskTest() throws Exception{
        UserService userService = mock(UserService.class);
        Field userServiceField = recommendHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(recommendHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = recommendHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(recommendHttpService, testService);

        PublishService publishService=mock(PublishService.class);
        Field publishServiceField = recommendHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(recommendHttpService,publishService);

        HttpRequestUtil httpRequestUtil=mock(HttpRequestUtil.class);
        Field httpRequestUtilField=recommendHttpService.getClass().getDeclaredField("httpRequestUtil");
        httpRequestUtilField.setAccessible(true);
        httpRequestUtilField.set(recommendHttpService,httpRequestUtil);

        List<TaskSimilarityDTO> taskSimilarityDTOList = new ArrayList<>();
        taskSimilarityDTOList.add(new TaskSimilarityDTO(77L, 0.5));
        taskSimilarityDTOList.add(new TaskSimilarityDTO(78L, 0.66));
        taskSimilarityDTOList.add(new TaskSimilarityDTO(79L,0.3));
        when(httpRequestUtil.requestTask(1L)).thenReturn(JsonUtil.toJson(taskSimilarityDTOList));

        List<Long> taskIds=new ArrayList<>();
        taskIds.add(78L);
        taskIds.add(77L);
        TaskIdsRequest taskIdsRequest=new TaskIdsRequest(taskIds,0);
        Map<Long, TaskInfo> taskInfoMap=new HashMap<>();
        TaskInfo taskInfo1=new TaskInfo();
        taskInfo1.setTaskId(77L);
        taskInfo1.setPublisherId(1L);
        TaskInfo taskInfo2=new TaskInfo();
        taskInfo2.setTaskId(78L);
        taskInfo2.setPublisherId(2L);
        taskInfoMap.put(77L,taskInfo1);
        taskInfoMap.put(78L,taskInfo2);
        TaskMapResponse taskMapResponse=new TaskMapResponse();
        taskMapResponse.setTaskInfoMap(taskInfoMap);
        when(publishService.getTaskMapByTaskIds(taskIdsRequest)).thenReturn(RpcResultUtil.success(taskMapResponse));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(1L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse=new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap=new HashMap<>();
        userInfoMap.put(2L,new UserInfo());
        userInfoMap.put(1L,new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(recommendHttpService.recommendTask(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }

}
