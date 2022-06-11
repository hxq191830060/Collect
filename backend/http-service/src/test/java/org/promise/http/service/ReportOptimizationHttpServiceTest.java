package org.promise.http.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.JsonUtil;
import org.promise.common.util.LocalDateTimeUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.http.service.httpService.RecommendHttpService;
import org.promise.http.service.httpService.ReportOptimizationHttpService;
import org.promise.http.service.httpService.dto.ClassifySimDTO;
import org.promise.http.service.httpService.dto.ReportClassifyDTO;
import org.promise.http.service.httpService.dto.ReportClusteringDTO;
import org.promise.http.service.httpService.dto.WordCloudDTO;
import org.promise.http.service.util.HttpRequestUtil;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.publish.service.api.request.TaskIdRequest;
import org.promise.publish.service.api.response.TaskResponse;
import org.promise.test.service.api.api.ReportOptimizationService;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.FinishedTestInfo;
import org.promise.test.service.api.info.ReportIntegrationInfo;
import org.promise.test.service.api.info.TestSummaryInfo;
import org.promise.test.service.api.request.TestBatchQueryRequest;
import org.promise.test.service.api.request.TestQueryRequest;
import org.promise.test.service.api.response.*;
import org.promise.user.service.api.api.UserService;
import org.promise.user.service.api.info.UserInfo;
import org.promise.user.service.api.request.BatchGetUserInfoRequest;
import org.promise.user.service.api.response.BatchGetUserInfoResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author moqi
 * @date 2022/5/27 15:14
 */
@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
public class ReportOptimizationHttpServiceTest {

    @Resource
    private ReportOptimizationHttpService reportOptimizationHttpService;

    @Resource
    private HttpRequestUtil httpRequestUtil;

    @Test
    void clusteringReportTest() throws Exception {
        UserService userService = mock(UserService.class);
        Field userServiceField = reportOptimizationHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(reportOptimizationHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = reportOptimizationHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(reportOptimizationHttpService, testService);

        HttpRequestUtil httpRequestUtil = mock(HttpRequestUtil.class);
        Field httpRequestUtilField = reportOptimizationHttpService.getClass().getDeclaredField("httpRequestUtil");
        httpRequestUtilField.setAccessible(true);
        httpRequestUtilField.set(reportOptimizationHttpService, httpRequestUtil);


        List<ReportClusteringDTO> reportClusteringDTOList = new ArrayList<>();
        List<Double> feature = new ArrayList<>();
        feature.add(0.1);
        feature.add(0.2);
        reportClusteringDTOList.add(new ReportClusteringDTO(feature, "1", 2L));
        reportClusteringDTOList.add(new ReportClusteringDTO(feature, "2", 3L));
        when(httpRequestUtil.clusteringReport(1L, 2)).thenReturn(JsonUtil.toJson(reportClusteringDTOList));

        Set<Long> testIds = new HashSet<>();
        testIds.add(2L);
        testIds.add(3L);
        TestBatchQueryRequest testBatchQueryRequest = new TestBatchQueryRequest(testIds);
        FinishedTestInfoMapResponse finishedTestInfoMapResponse = new FinishedTestInfoMapResponse();
        Map<Long, FinishedTestInfo> finishedTestInfoMap = new HashMap<>();
        FinishedTestInfo finishedTestInfo1 = new FinishedTestInfo();
        finishedTestInfo1.setWorkerId(2L);
        finishedTestInfoMap.put(2L, finishedTestInfo1);
        FinishedTestInfo finishedTestInfo2 = new FinishedTestInfo();
        finishedTestInfo2.setWorkerId(3L);
        finishedTestInfoMap.put(3L, finishedTestInfo2);
        finishedTestInfoMapResponse.setMap(finishedTestInfoMap);
        when(testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest)).thenReturn(RpcResultUtil.success(finishedTestInfoMapResponse));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(3L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(2L, new UserInfo());
        userInfoMap.put(3L, new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(reportOptimizationHttpService.clusteringReport(1L, 2).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void classifyReportTest() throws Exception {
        UserService userService = mock(UserService.class);
        Field userServiceField = reportOptimizationHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(reportOptimizationHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = reportOptimizationHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(reportOptimizationHttpService, testService);

        HttpRequestUtil httpRequestUtil = mock(HttpRequestUtil.class);
        Field httpRequestUtilField = reportOptimizationHttpService.getClass().getDeclaredField("httpRequestUtil");
        httpRequestUtilField.setAccessible(true);
        httpRequestUtilField.set(reportOptimizationHttpService, httpRequestUtil);

        List<ReportClassifyDTO> reportClassifyDTOList = new ArrayList<>();
        List<Double> feature = new ArrayList<>();
        feature.add(0.1);
        feature.add(0.2);
        reportClassifyDTOList.add(new ReportClassifyDTO("11", 2L));
        reportClassifyDTOList.add(new ReportClassifyDTO("22", 3L));
        when(httpRequestUtil.classifyReport(1L)).thenReturn(JsonUtil.toJson(reportClassifyDTOList));

        Set<Long> testIds = new HashSet<>();
        testIds.add(2L);
        testIds.add(3L);
        TestBatchQueryRequest testBatchQueryRequest = new TestBatchQueryRequest(testIds);
        FinishedTestInfoMapResponse finishedTestInfoMapResponse = new FinishedTestInfoMapResponse();
        Map<Long, FinishedTestInfo> finishedTestInfoMap = new HashMap<>();
        FinishedTestInfo finishedTestInfo1 = new FinishedTestInfo();
        finishedTestInfo1.setWorkerId(2L);
        finishedTestInfoMap.put(2L, finishedTestInfo1);
        FinishedTestInfo finishedTestInfo2 = new FinishedTestInfo();
        finishedTestInfo2.setWorkerId(3L);
        finishedTestInfoMap.put(3L, finishedTestInfo2);
        finishedTestInfoMapResponse.setMap(finishedTestInfoMap);
        when(testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest)).thenReturn(RpcResultUtil.success(finishedTestInfoMapResponse));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(3L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(2L, new UserInfo());
        userInfoMap.put(3L, new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(reportOptimizationHttpService.classifyReport(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void sortReportTest() throws Exception {
        UserService userService = mock(UserService.class);
        Field userServiceField = reportOptimizationHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(reportOptimizationHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = reportOptimizationHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(reportOptimizationHttpService, testService);

        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        testQueryRequest.setEvaluationThreshold(0.0);

        FinishedTestInfoResponse testInfoWithHighEvaluationRpcResult = new FinishedTestInfoResponse();
        List<FinishedTestInfo> finishedTestInfoList = new ArrayList<>();
        FinishedTestInfo finishedTestInfo1 = new FinishedTestInfo();
        finishedTestInfo1.setWorkerId(2L);
        finishedTestInfoList.add(finishedTestInfo1);
        FinishedTestInfo finishedTestInfo2 = new FinishedTestInfo();
        finishedTestInfo2.setWorkerId(3L);
        finishedTestInfoList.add(finishedTestInfo2);
        testInfoWithHighEvaluationRpcResult.setHighFinishedTestInfoList(finishedTestInfoList);
        when(testService.getTestInfoWithHighEvaluationByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(testInfoWithHighEvaluationRpcResult));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(3L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(2L, new UserInfo());
        userInfoMap.put(3L, new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(reportOptimizationHttpService.sortReport(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void filterReportTest() throws Exception {
        UserService userService = mock(UserService.class);
        Field userServiceField = reportOptimizationHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(reportOptimizationHttpService, userService);

        ReportOptimizationService reportOptimizationService = mock(ReportOptimizationService.class);
        Field reportOptimizationServiceField = reportOptimizationHttpService.getClass().getDeclaredField("reportOptimizationService");
        reportOptimizationServiceField.setAccessible(true);
        reportOptimizationServiceField.set(reportOptimizationHttpService, reportOptimizationService);

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(3L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(2L, new UserInfo());
        userInfoMap.put(3L, new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        List<TestSummaryInfo> testSummaryInfoList = new ArrayList<>();
        TestSummaryInfo testSummaryInfo1 = new TestSummaryInfo();
        testSummaryInfo1.setTestId(2L);
        testSummaryInfo1.setCollaborationNumber(10);
        testSummaryInfo1.setTestCaseCount(20);
        testSummaryInfo1.setTestToolCount(6);
        testSummaryInfo1.setAverageEvaluation(80.0);
        testSummaryInfo1.setWrongCaseCount(15);
        testSummaryInfo1.setWorkerId(2L);
        testSummaryInfo1.setBugImgCount(20);
        testSummaryInfo1.setWordCount(100);
        testSummaryInfoList.add(testSummaryInfo1);
        TestSummaryInfo testSummaryInfo2 = new TestSummaryInfo();
        testSummaryInfo2.setTestId(3L);
        testSummaryInfo2.setCollaborationNumber(10);
        testSummaryInfo2.setTestCaseCount(20);
        testSummaryInfo2.setTestToolCount(6);
        testSummaryInfo2.setAverageEvaluation(80.0);
        testSummaryInfo2.setWrongCaseCount(15);
        testSummaryInfo2.setWorkerId(3L);
        testSummaryInfo2.setBugImgCount(20);
        testSummaryInfo2.setWordCount(100);
        testSummaryInfoList.add(testSummaryInfo2);
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        when(reportOptimizationService.getTestSummaryByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(new TestSummaryResponse(testSummaryInfoList)));


        Assertions.assertEquals(reportOptimizationHttpService.filterReport(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void integrateReportTest() throws Exception {
        ReportOptimizationService reportOptimizationService = mock(ReportOptimizationService.class);
        Field reportOptimizationServiceField = reportOptimizationHttpService.getClass().getDeclaredField("reportOptimizationService");
        reportOptimizationServiceField.setAccessible(true);
        reportOptimizationServiceField.set(reportOptimizationHttpService, reportOptimizationService);

        HttpRequestUtil httpRequestUtil = mock(HttpRequestUtil.class);
        Field httpRequestUtilField = reportOptimizationHttpService.getClass().getDeclaredField("httpRequestUtil");
        httpRequestUtilField.setAccessible(true);
        httpRequestUtilField.set(reportOptimizationHttpService, httpRequestUtil);

        UserService userService = mock(UserService.class);
        Field userServiceField = reportOptimizationHttpService.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(reportOptimizationHttpService, userService);

        TestService testService = mock(TestService.class);
        Field testServiceField = reportOptimizationHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(reportOptimizationHttpService, testService);

        ReportIntegrationInfo reportIntegrationInfo=new ReportIntegrationInfo();
        reportIntegrationInfo.getRightCaseCountMap().put(1L,2);
        reportIntegrationInfo.getRightCaseCountMap().put(2L,3);
        reportIntegrationInfo.getTestToolCountMap().put("11",2);
        reportIntegrationInfo.getOperatingSystemCountMap().put("22",21);
        reportIntegrationInfo.getWrongCaseCountMap().put(2L,3);
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        when(reportOptimizationService.getReportIntegrationResponse(testQueryRequest)).thenReturn(RpcResultUtil.success(new ReportIntegrationResponse(reportIntegrationInfo)));

        List<WordCloudDTO> wordCloudDTOList=new ArrayList<>();
        wordCloudDTOList.add(new WordCloudDTO(1,"moqi"));

        when(httpRequestUtil.getWordCloudClu(1L)).thenReturn(JsonUtil.toJson(wordCloudDTOList));
        when(httpRequestUtil.getWordCloudSug(1L)).thenReturn(JsonUtil.toJson(wordCloudDTOList));
        when(httpRequestUtil.getWordCloudErr(1L)).thenReturn(JsonUtil.toJson(wordCloudDTOList));

        List<ClassifySimDTO> classifySimDTOList=new ArrayList<>();
        ClassifySimDTO classifySimDTO1=new ClassifySimDTO();
        classifySimDTO1.setSim(0.1);
        classifySimDTO1.setReportId(2L);
        classifySimDTO1.setLabel("与预期不符");
        classifySimDTOList.add(classifySimDTO1);
        ClassifySimDTO classifySimDTO2=new ClassifySimDTO();
        classifySimDTO2.setSim(0.9);
        classifySimDTO2.setReportId(3L);
        classifySimDTO2.setLabel("与预期不符");
        classifySimDTOList.add(classifySimDTO2);
        ClassifySimDTO classifySimDTO3=new ClassifySimDTO();
        classifySimDTO3.setSim(1.0);
        classifySimDTO3.setLabelName("与预期不符");
        classifySimDTOList.add(classifySimDTO3);
        when(httpRequestUtil.getClassifySim(1L)).thenReturn(JsonUtil.toJson(classifySimDTOList));

        Set<Long> testIds = new HashSet<>();
        testIds.add(2L);
        testIds.add(3L);
        TestBatchQueryRequest testBatchQueryRequest = new TestBatchQueryRequest(testIds);
        FinishedTestInfoMapResponse finishedTestInfoMapResponse = new FinishedTestInfoMapResponse();
        Map<Long, FinishedTestInfo> finishedTestInfoMap = new HashMap<>();
        FinishedTestInfo finishedTestInfo1 = new FinishedTestInfo();
        finishedTestInfo1.setWorkerId(2L);
        finishedTestInfoMap.put(2L, finishedTestInfo1);
        FinishedTestInfo finishedTestInfo2 = new FinishedTestInfo();
        finishedTestInfo2.setWorkerId(3L);
        finishedTestInfoMap.put(3L, finishedTestInfo2);
        finishedTestInfoMapResponse.setMap(finishedTestInfoMap);
        when(testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest)).thenReturn(RpcResultUtil.success(finishedTestInfoMapResponse));

        BatchGetUserInfoRequest userInfosRequest = new BatchGetUserInfoRequest();
        Set<Long> userIds = new HashSet<>();
        userIds.add(2L);
        userIds.add(3L);
        userInfosRequest.setUserIdSet(userIds);
        BatchGetUserInfoResponse batchGetUserInfoResponse = new BatchGetUserInfoResponse();
        Map<Long, UserInfo> userInfoMap = new HashMap<>();
        userInfoMap.put(2L, new UserInfo());
        userInfoMap.put(3L, new UserInfo());
        batchGetUserInfoResponse.setUserInfos(userInfoMap);
        when(userService.batchGetUserInfoByUserId(userInfosRequest)).thenReturn(RpcResultUtil.success(batchGetUserInfoResponse));

        Assertions.assertEquals(reportOptimizationHttpService.integrateReport(1L).getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void whetherOptimizeReportTest() throws Exception {
        TestService testService = mock(TestService.class);
        Field testServiceField = reportOptimizationHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(reportOptimizationHttpService, testService);

        PublishService publishService = mock(PublishService.class);
        Field publishServiceField = reportOptimizationHttpService.getClass().getDeclaredField("publishService");
        publishServiceField.setAccessible(true);
        publishServiceField.set(reportOptimizationHttpService, publishService);

        TaskInfo taskInfo=new TaskInfo();
        taskInfo.setTaskId(1L);
        taskInfo.setTestEndTime(LocalDateTimeUtil.asString(LocalDateTime.now()));
        TaskIdRequest taskIdRequest=new TaskIdRequest(1L);
        when(publishService.getTaskById(taskIdRequest)).thenReturn(RpcResultUtil.success(new TaskResponse(taskInfo)));

        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        when(testService.getTestCountByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(new TestCountResponse(1)));
        when(testService.getSimpleFinishedTestByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(new FinishedTestInfoResponse(new ArrayList<>())));

        Assertions.assertEquals(reportOptimizationHttpService.whetherOptimizeReport(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getFinishedReportCountTest() throws Exception {
        TestService testService = mock(TestService.class);
        Field testServiceField = reportOptimizationHttpService.getClass().getDeclaredField("testService");
        testServiceField.setAccessible(true);
        testServiceField.set(reportOptimizationHttpService, testService);

        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(1L);
        when(testService.getTestCountByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(new TestCountResponse(1)));
        when(testService.getSimpleFinishedTestByTaskId(testQueryRequest)).thenReturn(RpcResultUtil.success(new FinishedTestInfoResponse(new ArrayList<>())));

        Assertions.assertEquals(reportOptimizationHttpService.getFinishedReportCount(1L).getCode(),HttpCodes.SUCCESS.getCode());
    }
}
