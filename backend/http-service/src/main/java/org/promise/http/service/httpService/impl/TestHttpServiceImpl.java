package org.promise.http.service.httpService.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.weaver.ast.Var;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.http.service.convert.*;
import org.promise.http.service.httpService.TestHttpService;
import org.promise.http.service.vo.test.*;
import org.promise.publish.service.api.api.PublishService;
import org.promise.publish.service.api.request.TaskIdRequest;
import org.promise.publish.service.api.response.TaskTimeResponse;
import org.promise.publish.service.api.response.TaskMaxWorkerCountResponse;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.CollaborationInfo;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author moqi
 * @date 2022/3/26 22:59
 */
@Slf4j
@Service
public class TestHttpServiceImpl implements TestHttpService {

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private PublishService publishService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private TestService testService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private UserService userService;

    @Resource
    private TestConvert testConvert;

    @Resource
    private TestBugImgConvert testBugImgConvert;

    @Resource
    private TestCaseConvert testCaseConvert;

    @Resource
    private TestCollaborationRecordConvert testCollaborationRecordConvert;

    @Resource
    private HighEvaluationTestConvert highEvaluationTestConvert;

    @Resource
    private SimpleTestConvert simpleTestConvert;

    @Resource
    private CollaborationConvert collaborationConvert;

    /**
     * 测试工人接受一个测试任务
     * @param testVO 测试信息
     * @return 请求结果
     */
    @Override
    public HttpResult<String> createTest(TestVO testVO) {
        //获取允许的最大工人数量
        TaskIdRequest taskIdRequest = new TaskIdRequest(testVO.getTaskId());
        RpcResult<TaskMaxWorkerCountResponse> maxWorkerCountRpcResult = publishService.getMaxWorkerCountByTaskId(taskIdRequest);
        if (maxWorkerCountRpcResult.getData() == null) {
            return HttpResultUtil.fail(maxWorkerCountRpcResult.getMessage());
        }
        Integer maxWorkerCount = maxWorkerCountRpcResult.getData().getMaxWorkerCount();

        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setTaskId(testVO.getTaskId());
        createTestRequest.setWorkerId(testVO.getWorkerId());
        createTestRequest.setAllowedMaxWorkerCount(maxWorkerCount);

        RpcResult<CreateTestResponse> testRpcResult = testService.createTest(createTestRequest);
        if (testRpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(testRpcResult.getMessage());
        }
    }

    /**
     * 查看当前工人是否已经接受该任务
     * @param workerId
     * @param taskId
     * @return
     */
    @Override
    public HttpResult<WhetherWorkerHasAcceptTaskResponse> whetherWorkerHasAcceptTask(Long workerId, Long taskId) {
        WhetherWorkerHasAcceptTaskRequest whetherWorkerHasAcceptTaskRequest =
                new WhetherWorkerHasAcceptTaskRequest();
        whetherWorkerHasAcceptTaskRequest.setTaskId(taskId);
        whetherWorkerHasAcceptTaskRequest.setWorkerId(workerId);

        RpcResult<WhetherWorkerHasAcceptTaskResponse> hasTaskRpcResult =
                testService.whetherWorkerHasAcceptTask(whetherWorkerHasAcceptTaskRequest);
        if (!hasTaskRpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.fail(hasTaskRpcResult.getMessage());
        }
        return HttpResultUtil.success(hasTaskRpcResult.getData());
    }

    /**
     * 当前工人取消当前任务
     * @param testId
     * @return
     */
    @Override
    public HttpResult<String> cancelTest(Long testId, Long workerId) {
        //先根据testId获取对应的taskId
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(testId);
        RpcResult<TestInfoResponse> simpleTestInfoRpcResult = testService.getSimpleTestInfoByTestId(testQueryRequest);
        if (simpleTestInfoRpcResult.getData() == null) {
            return HttpResultUtil.fail(simpleTestInfoRpcResult.getMessage());
        }
        TestInfo testInfo = simpleTestInfoRpcResult.getData().getTestInfoList().get(0);

        //获取最晚可取消的时间
        TaskIdRequest taskIdRequest = new TaskIdRequest(testInfo.getTaskId());
        RpcResult<TaskTimeResponse> allowCancelTimeRpcResult = publishService.getAllowCancelTimeByTaskId(taskIdRequest);
        if (allowCancelTimeRpcResult.getData() == null) {
            return HttpResultUtil.fail(allowCancelTimeRpcResult.getMessage());
        }
        String allowCancelTime = allowCancelTimeRpcResult.getData().getAllowCancelTime();

        //实际的取消操作
        CancelTestRequest cancelTestRequest = new CancelTestRequest(testId, workerId, allowCancelTime);
        RpcResult<CancelTestResponse> cancelTestRpcResult = testService.cancelTest(cancelTestRequest);
        if (cancelTestRpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(cancelTestRpcResult.getMessage());
        }
    }

    /**
     * 根据TestId获取当前test的所有详细信息
     * @param testId
     * @return
     */
    @Override
    public HttpResult<TestVO> getTestByTestId(Long testId) {
        //先查询test的具体信息
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(testId);
        RpcResult<TestInfoResponse> testInfoRpcResult = testService.getTestInfoByTestId(testQueryRequest);
        if (testInfoRpcResult.getData() == null) {
            return HttpResultUtil.fail(testInfoRpcResult.getMessage());
        }
        TestVO testVO = testConvert.convertInfo2VO(testInfoRpcResult.getData().getTestInfoList().get(0));

        RpcResult<CollaborationInfoListResponse> simpleCollaborationListRpcResult = testService.getSimpleCollaborationListByTestId(testQueryRequest);
        if (simpleCollaborationListRpcResult.getData() == null) {
            return HttpResultUtil.fail(simpleCollaborationListRpcResult.getMessage());
        }
        List<CollaborationVO> collaborationList = collaborationConvert.convertInfoList2VOList(simpleCollaborationListRpcResult.getData().getCollaborationInfoList());
        testVO.setCollaborationList(collaborationList);
        List<TestCollaborationRecordVO> testCollaborationRecordList = testVO.getTestCollaborationRecordList();
        //准备查询用户相关信息
        Set<Long> workerIdSet = new HashSet<>();
        workerIdSet.add(testVO.getWorkerId());
        for (TestCollaborationRecordVO testCollaborationRecordVO : testCollaborationRecordList) {
            workerIdSet.add(testCollaborationRecordVO.getWorkerId());
        }
        for (CollaborationVO collaborationVO : collaborationList) {
            workerIdSet.add(collaborationVO.getWorkerId());
        }

        RpcResult<BatchGetUserInfoResponse> userInfoMapRpcResult =
                userService.batchGetUserInfoByUserId(new BatchGetUserInfoRequest(workerIdSet));
        if (userInfoMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoMapRpcResult.getMessage());
        }
        Map<Long, UserInfo> userInfoMap = userInfoMapRpcResult.getData().getUserInfos();

        //开始为返回体赋值用户信息,先赋值testVO
        testVO.setNickname(userInfoMap.get(testVO.getWorkerId()).getNickName());
        testVO.setAvatar(userInfoMap.get(testVO.getWorkerId()).getAvatar());
        //赋值协作评分用户
        for (TestCollaborationRecordVO testCollaborationRecordVO : testCollaborationRecordList) {
            UserInfo userInfo = userInfoMap.get(testCollaborationRecordVO.getWorkerId());
            testCollaborationRecordVO.setNickname(userInfo.getNickName());
            testCollaborationRecordVO.setAvatar(userInfo.getAvatar());
        }
        //赋值协作用户信息
        for (CollaborationVO collaborationVO : collaborationList) {
            UserInfo userInfo = userInfoMap.get(collaborationVO.getWorkerId());
            collaborationVO.setNickname(userInfo.getNickName());
            collaborationVO.setAvatar(userInfo.getAvatar());
        }

        return HttpResultUtil.success(testVO);
    }

    /**
     * 提交任务
     * @param testVO
     * @return
     */
    @Override
    public HttpResult<String> submitTest(TestVO testVO) {

        //获取test的信息，主要是为了获取taskId
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(testVO.getTestId());
        RpcResult<TestInfoResponse> simpleTestInfoRpcResult = testService.getSimpleTestInfoByTestId(testQueryRequest);
        if (simpleTestInfoRpcResult.getData() == null) {
            return HttpResultUtil.fail(simpleTestInfoRpcResult.getMessage());
        }
        Long taskId = simpleTestInfoRpcResult.getData().getTestInfoList().get(0).getTaskId();

        //使用taskId获取当前task的结束时间
        TaskIdRequest taskIdRequest = new TaskIdRequest(taskId);
        RpcResult<TaskTimeResponse> taskEndTimeRpcResult = publishService.getTaskEndTimeByTaskId(taskIdRequest);
        if (taskEndTimeRpcResult.getData() == null) {
            return HttpResultUtil.fail(taskEndTimeRpcResult.getMessage());
        }
        String lastSubmitTime = taskEndTimeRpcResult.getData().getTestEndTime();

        //将testid，结束时间的参数传入进行提交操作
        SubmitTestRequest submitTestRequest =
                new SubmitTestRequest(
                        testVO.getTestId(),
                        testVO.getWorkerId(),
                        testVO.getTestEnvironment(),
                        testVO.getTestTools(),
                        testBugImgConvert.convertVOList2InfoList(testVO.getScreenshots()),
                        testCaseConvert.convertVOList2InfoList(testVO.getTestCases()),
                        testVO.getConclusion(),
                        testVO.getSuggestion(),
                        lastSubmitTime
                );

        RpcResult<SubmitTestResponse> submitTestRpcResult = testService.submitTest(submitTestRequest);
        if (submitTestRpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(submitTestRpcResult.getMessage());
        }
    }

    /**
     * 对其他worker提交的测试宝报进行评价
     * @param testCollaborationRecordVO
     * @return
     */
    @Override
    public HttpResult<String> submitCollaborationRecord(TestCollaborationRecordVO testCollaborationRecordVO) {
        TestCollaborationRecordInfo testCollaborationRecordInfo =
                testCollaborationRecordConvert.convertVO2Info(testCollaborationRecordVO);
        SubmitCollaborationRecordRequest submitCollaborationRecordRequest =
                new SubmitCollaborationRecordRequest(testCollaborationRecordInfo);
        RpcResult<SubmitCollaborationRecordResponse> submitCollaborationRecordRpcResult =
                testService.submitCollaborationRecord(submitCollaborationRecordRequest);
        if (submitCollaborationRecordRpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(submitCollaborationRecordRpcResult.getMessage());
        }
    }

    /**
     * 获得当前task中的高分评价
     * @param taskId
     * @return
     */
    @Override
    public HttpResult<List<HighEvaluationTestVO>> getTestInfoWithHighEvaluationByTaskId(Long taskId) {
        //先查询出所有的高分评价
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        RpcResult<FinishedTestInfoResponse> testInfoWithHighEvaluationRpcResult =
                testService.getTestInfoWithHighEvaluationByTaskId(testQueryRequest);
        if (testInfoWithHighEvaluationRpcResult.getData() == null) {
            return HttpResultUtil.fail(testInfoWithHighEvaluationRpcResult.getMessage());
        }
        List<HighEvaluationTestVO> highEvaluationTestVOList =
                highEvaluationTestConvert.convertInfoList2VOList(testInfoWithHighEvaluationRpcResult.getData().getHighFinishedTestInfoList());

        if (CollectionUtils.isEmpty(highEvaluationTestVOList)) {
            return HttpResultUtil.success(highEvaluationTestVOList);
        }
        //查出每个高分评价的发布者信息并写入返回的数据结构
        Set<Long> userIds = new HashSet<>();
        for (HighEvaluationTestVO highEvaluationTestVO : highEvaluationTestVOList) {
            userIds.add(highEvaluationTestVO.getWorkerId());
        }
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(userIds);
        RpcResult<BatchGetUserInfoResponse> userInfoMapRpcResult = userService.batchGetUserInfoByUserId(batchGetUserInfoRequest);
        if (userInfoMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoMapRpcResult.getMessage());
        }
        Map<Long, UserInfo> userInfoMap = userInfoMapRpcResult.getData().getUserInfos();
        for (HighEvaluationTestVO highEvaluationTestVO : highEvaluationTestVOList) {
            highEvaluationTestVO.setAvatar(userInfoMap.get(highEvaluationTestVO.getWorkerId()).getAvatar());
            highEvaluationTestVO.setNickname(userInfoMap.get(highEvaluationTestVO.getWorkerId()).getNickName());
        }

        return HttpResultUtil.success(highEvaluationTestVOList);
    }

    /**
     * 根据任务id查询当前任务的所有测试
     * @param taskId
     * @return
     */
    @Override
    public HttpResult<List<SimpleTestVO>> getSimpleTestByTaskId(Long taskId) {
        //先查出所有test
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        RpcResult<FinishedTestInfoResponse> simpleFinishedTestRpcResult = testService.getSimpleFinishedTestByTaskId(testQueryRequest);
        if (simpleFinishedTestRpcResult.getData() == null) {
            return HttpResultUtil.fail(simpleFinishedTestRpcResult.getMessage());
        }
        List<SimpleTestVO> simpleTestVOList =
                simpleTestConvert.convertInfoList2VOList(simpleFinishedTestRpcResult.getData().getHighFinishedTestInfoList());

        if (CollectionUtils.isEmpty(simpleTestVOList)) {
            return HttpResultUtil.success(simpleTestVOList);
        }

        //根据test查询用户信息
        Set<Long> userIds = new HashSet<>();
        for (SimpleTestVO simpleTestVO : simpleTestVOList) {
            userIds.add(simpleTestVO.getWorkerId());
        }
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(userIds);
        RpcResult<BatchGetUserInfoResponse> userInfoMapRpcResult = userService.batchGetUserInfoByUserId(batchGetUserInfoRequest);
        if (userInfoMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoMapRpcResult.getMessage());
        }
        Map<Long, UserInfo> userInfoMap = userInfoMapRpcResult.getData().getUserInfos();
        for (SimpleTestVO simpleTestVO : simpleTestVOList) {
            simpleTestVO.setAvatar(userInfoMap.get(simpleTestVO.getWorkerId()).getAvatar());
            simpleTestVO.setNickname(userInfoMap.get(simpleTestVO.getWorkerId()).getNickName());
        }

        return HttpResultUtil.success(simpleTestVOList);
    }

    /**
     * 提交写作报告
     * @param collaborationVO
     * @return
     */
    @Override
    public HttpResult<String> submitCollaboration(CollaborationVO collaborationVO) {
        CollaborationInfo collaborationInfo = collaborationConvert.convertVO2Info(collaborationVO);
        SubmitCollaborationRequest submitCollaborationRequest = new SubmitCollaborationRequest();
        submitCollaborationRequest.setCollaborationInfo(collaborationInfo);
        RpcResult<SubmitCollaborationResponse> submitCollaborationRpcResult = testService.submitCollaboration(submitCollaborationRequest);
        if (submitCollaborationRpcResult.getCode().equals(RpcCodes.SUCCESS.getCode())) {
            return HttpResultUtil.success();
        } else {
            return HttpResultUtil.fail(submitCollaborationRpcResult.getMessage());
        }
    }

    @Override
    public HttpResult<List<CollaborationVO>> getCollaborationListByTestId(Long testId) {
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTestId(testId);
        RpcResult<CollaborationInfoListResponse> collaborationListRpcResult = testService.getSimpleCollaborationListByTestId(testQueryRequest);
        if (collaborationListRpcResult.getData() == null) {
            return HttpResultUtil.fail(collaborationListRpcResult.getMessage());
        }
        List<CollaborationVO> collaborationVOList = collaborationConvert.convertInfoList2VOList(collaborationListRpcResult.getData().getCollaborationInfoList());

        if (CollectionUtils.isEmpty(collaborationVOList)) {
            return HttpResultUtil.success(collaborationVOList);
        }
        //根据test查询用户信息
        Set<Long> userIds = new HashSet<>();
        for (CollaborationVO collaborationVO : collaborationVOList) {
            userIds.add(collaborationVO.getWorkerId());
        }
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(userIds);
        RpcResult<BatchGetUserInfoResponse> userInfoMapRpcResult = userService.batchGetUserInfoByUserId(batchGetUserInfoRequest);
        if (userInfoMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoMapRpcResult.getMessage());
        }
        Map<Long, UserInfo> userInfoMap = userInfoMapRpcResult.getData().getUserInfos();
        for (CollaborationVO collaborationVO : collaborationVOList) {
            UserInfo userInfo = userInfoMap.get(collaborationVO.getWorkerId());
            collaborationVO.setAvatar(userInfo.getAvatar());
            collaborationVO.setNickname(userInfo.getNickName());
        }

        return HttpResultUtil.success(collaborationVOList);
    }

    /**
     * 获取协作报告详细信息
     * @param id
     * @return
     */
    @Override
    public HttpResult<CollaborationVO> getCollaborationById(Long id) {
        CollaborationIdRequest collaborationIdRequest = new CollaborationIdRequest(id);
        RpcResult<CollaborationInfoResponse> collaborationRpcResult = testService.getCollaborationById(collaborationIdRequest);
        if (collaborationRpcResult.getData() == null) {
            return HttpResultUtil.fail(collaborationRpcResult.getMessage());
        }
        CollaborationVO collaborationVO = collaborationConvert.convertInfo2VO(collaborationRpcResult.getData().getCollaborationInfo());

        RpcResult<UserInfoResponse> userInfoRpcResult = userService.getUserInfoByUserId(new UserIdRequest(collaborationVO.getWorkerId()));
        if (userInfoRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoRpcResult.getMessage());
        }
        collaborationVO.setNickname(userInfoRpcResult.getData().getUserInfo().getNickName());
        collaborationVO.setAvatar(userInfoRpcResult.getData().getUserInfo().getAvatar());
        return HttpResultUtil.success(collaborationVO);
    }

    /**
     * 查看当前用户是否填写该协同报告
     * @param testId
     * @param workerId
     * @return
     */
    @Override
    public HttpResult<Long> workerHasCollaboration(Long testId, Long workerId) {
        WhetherWorkerHasSubmitCollaborationRequest whetherWorkerHasSubmitCollaborationRequest = new WhetherWorkerHasSubmitCollaborationRequest();
        whetherWorkerHasSubmitCollaborationRequest.setWorkerId(workerId);
        whetherWorkerHasSubmitCollaborationRequest.setTestId(testId);
        RpcResult<WhetherWorkerHasSubmitCollaborationResponse> workerHasSubmitCollaborationRpcResult = testService.whetherWorkerHasSubmitCollaboration(whetherWorkerHasSubmitCollaborationRequest);
        if (workerHasSubmitCollaborationRpcResult.getData() == null) {
            return HttpResultUtil.fail(workerHasSubmitCollaborationRpcResult.getMessage());
        }
        return HttpResultUtil.success(workerHasSubmitCollaborationRpcResult.getData().getId());
    }


}
