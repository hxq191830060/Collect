package org.promise.test.service.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.promise.common.result.page.Page;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.LocalDateTimeUtil;
import org.promise.common.util.PageCheckUtil;
import org.promise.common.util.RpcResultUtil;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.*;
import org.promise.test.service.api.request.*;
import org.promise.test.service.api.response.*;
import org.promise.test.service.convert.*;
import org.promise.test.service.facotry.TestInfoFactory;
import org.promise.test.service.manager.*;
import org.promise.test.service.manager.dao.*;
import org.promise.test.service.mapperService.constant.TestCaseStatusEnum;
import org.promise.test.service.mapperService.constant.TestStateEnum;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;


@DubboService(interfaceClass = TestService.class, group = "${dubbo.registry.group}")
@Slf4j
public class TestServiceImpl implements TestService {

    @Resource
    private TestManager testManager;

    @Resource
    private TestCountManager testCountManager;

    @Resource
    private TestBugImgManager testBugImgManager;

    @Resource
    private TestCollaborationRecordManager testCollaborationRecordManager;

    @Resource
    private TestCaseManager testCaseManager;

    //评分阈值，超过该评分
    @Value("${evaluation.threshold}")
    private Double evaluationThreshold;

    @Resource
    private CollaborationManager collaborationManager;


    @Override
    public RpcResult<CreateTestResponse> createTest(CreateTestRequest request) {
        if (request == null || request.getTaskId() == null || request.getWorkerId() == null || request.getAllowedMaxWorkerCount() == null) {
            return RpcResultUtil.fail("参数缺省");
        }

        //检查工人是否已经接受了任务
        if (testManager.whetherWorkerHasAcceptTask(request.getTaskId(), request.getWorkerId()) != null) {
            return RpcResultUtil.fail("已接受该任务,请勿重复接受");
        }

        //获取当前已经接受了任务的工人数目
        Integer currentWorkerNumber = testCountManager.getWorkerNumberByTaskId(request.getTaskId());
        if (currentWorkerNumber == null) {
            return RpcResultUtil.fail("任务不存在");
        }
        if (currentWorkerNumber >= request.getAllowedMaxWorkerCount()) {
            return RpcResultUtil.fail("工人已满,无法接受任务");
        }

        testManager.createNewTestIfIncreaseSuccess(request.getTaskId(), request.getWorkerId(), currentWorkerNumber);

        return RpcResultUtil.success();
    }


    @Override
    public RpcResult<WhetherWorkerHasAcceptTaskResponse> whetherWorkerHasAcceptTask(WhetherWorkerHasAcceptTaskRequest request) {
        if (request == null || request.getTaskId() == null || request.getWorkerId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        TestDAO testDAO = testManager.whetherWorkerHasAcceptTask(request.getTaskId(), request.getWorkerId());
        WhetherWorkerHasAcceptTaskResponse response = new WhetherWorkerHasAcceptTaskResponse();


        if (testDAO == null) {
            response.setHasAccept(false);
            return RpcResultUtil.success(response);
        }
        response.setTestId(testDAO.getTestId());
        response.setHasAccept(true);
        response.setState(testDAO.getState().name());
        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<CancelTestResponse> cancelTest(CancelTestRequest request) {
        if (request == null || request.getTestId() == null || request.getWorkerId() == null || StringUtils.isEmpty(request.getAllowCancelTime())) {
            return RpcResultUtil.fail("参数缺省");
        }

        //先判断能否取消
        LocalDateTime lastCancelTIme = LocalDateTimeUtil.asLocalDateTime(request.getAllowCancelTime());
        if (LocalDateTime.now().isAfter(lastCancelTIme)) {
            return RpcResultUtil.fail("无法取消任务,已经过了最晚取消时间");
        }

        TestDAO testDAO = testManager.getTestInfoByTestId(request.getTestId());
        if (testDAO == null) {
            return RpcResultUtil.fail("不存在对应的测试");
        }

        if (TestStateEnum.finish.equals(testDAO.getState())) {
            return RpcResultUtil.fail("测试已提交,无法取消");
        }

        if (TestStateEnum.cancel.equals(testDAO.getState())) {
            return RpcResultUtil.fail("测试已取消,请勿重复取消");
        }

        if (!testDAO.getWorkerId().equals(request.getWorkerId())) {
            return RpcResultUtil.fail("该测试不属于你,无法取消");
        }


        //真正去取消测试任务
        testManager.cancelTest(request.getTestId());
        return RpcResultUtil.success();
    }


    @Override
    public RpcResult<Integer> getWorkerAcceptedTaskNumber(TestQueryRequest request) {
        if (request == null || request.getWorkerId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        Long workerId = request.getWorkerId();
        Integer taskNumber = testManager.getWorkerAcceptedTaskNumber(workerId);
        return RpcResultUtil.success(taskNumber);
    }


    @Override
    public RpcResult<TestInfoResponse> getTestInfoByTestId(TestQueryRequest request) {
        if (request == null || request.getTestId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        Long testId = request.getTestId();

        TestDAO testDAO = testManager.getTestInfoByTestId(testId);
        if (testDAO == null) {
            return RpcResultUtil.fail("无效的testId");
        }

        List<TestCaseDAO> testCaseDAOList = testCaseManager.getUsableTestCaseListByTestId(testId);
        List<TestBugImgDAO> testBugImgDAOList = testBugImgManager.getUsableBugImgListByTestId(testId);
        List<TestCollaborationRecordDAO> testCollaborationRecordDAOList = testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId(testId);

        //拼接成testInfo
        TestInfo testInfo = TestInfoFactory.createTestInfo(testDAO, testCaseDAOList, testCollaborationRecordDAOList, testBugImgDAOList);

        List<TestInfo> resList = new ArrayList<>(1);
        resList.add(testInfo);
        Page resPage = new Page(1);
        TestInfoResponse response = new TestInfoResponse(resList, resPage);
        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<TestInfoResponse> getSimpleTestInfoByTestId(TestQueryRequest request) {
        if (request == null || request.getTestId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        Long testId = request.getTestId();

        TestDAO testDAO = testManager.getTestInfoByTestId(testId);
        if (testDAO == null) {
            return RpcResultUtil.fail("无效的testId");
        }
        TestInfo testInfo = TestInfoFactory.createTestInfo(testDAO);
        List<TestInfo> list = new ArrayList<>(1);
        list.add(testInfo);
        TestInfoResponse response = new TestInfoResponse();
        response.setTestInfoList(list);
        response.setPage(new Page(1));
        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<TestInfoResponse> getWorkerTestInfo(GetWorkerTestInfoRequest request) {
        if (request == null || request.getWorkerId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        if (CollectionUtils.isEmpty(request.getStateList())) {
            return RpcResultUtil.fail("请传入要查询的记录的状态");
        }

        List<TestStateEnum> stateList = new ArrayList<>(request.getStateList().size());
        for (String stateString : request.getStateList()) {
            TestStateEnum state = TestStateEnum.parse(stateString);
            if (state == null) {
                return RpcResultUtil.fail("传入的状态值有误");
            }
            if (!stateList.contains(state)) {
                stateList.add(state);
            }
        }

        if (!PageCheckUtil.pageParamIsLegal(request.getPage())) {
            return RpcResultUtil.fail("分页参数有误");
        }
        TestInfoResponse response = new TestInfoResponse();

        Integer total = testManager.countTestInfoByWorkerIdAndStateList(request.getWorkerId(), stateList);
        Page resPage = new Page(total);
        response.setPage(resPage);

        List<TestDAO> testDAOList = testManager.getTestDAOListByWorkerIdAndStateListByPage(request.getWorkerId(), stateList, request.getPage());
        if (CollectionUtils.isEmpty(testDAOList)) {
            response.setTestInfoList(Collections.emptyList());
        } else {
            List<TestInfo> testInfoList = TestConvert.INSTANCE.convertTestDAOList2TestInfoList(testDAOList);
            response.setTestInfoList(testInfoList);
        }
        return RpcResultUtil.success(response);
    }


    @Override
    public RpcResult<SubmitCollaborationRecordResponse> submitCollaborationRecord(SubmitCollaborationRecordRequest request) {
        if (request == null || request.getTestCollaborationRecordInfo() == null) {
            return RpcResultUtil.fail("请求参数为空");
        }
        TestCollaborationRecordInfo collaboration = request.getTestCollaborationRecordInfo();
        if (collaboration.getTestId() == null || collaboration.getWorkerId() == null || collaboration.getEvaluation() == null || StringUtils.isEmpty(collaboration.getRecommendation())) {
            return RpcResultUtil.fail("参数缺省");
        }

        //获取被评价的测试报告信息
        TestDAO testDAO = testManager.getTestInfoByTestId(collaboration.getTestId());
        if (testDAO == null) {
            return RpcResultUtil.fail("测试不存在");
        }
        if (!TestStateEnum.finish.equals(testDAO.getState())) {
            return RpcResultUtil.fail("测试报告未提交,无法评价");
        }
        if (testManager.whetherWorkerHasAcceptTask(testDAO.getTaskId(), collaboration.getWorkerId()) == null) {
            return RpcResultUtil.fail("用户未接受task,无法对该task的测试报告进行评论");
        }

        //开始提交评论信息
        testCollaborationRecordManager.submitTestCollaborationRecord(
                TestCollaborationRecordConvert.INSTANCE.convertInfo2DAO(collaboration));
        SubmitCollaborationRecordResponse response = new SubmitCollaborationRecordResponse();
        return RpcResultUtil.success(response);
    }


    @Override
    public RpcResult<SubmitTestResponse> submitTest(SubmitTestRequest request) {
        if (request == null || request.getWorkerId() == null || request.getTestId() == null || StringUtils.isEmpty(request.getOperatingSystem())
                || CollectionUtils.isEmpty(request.getTestTools()) || CollectionUtils.isEmpty(request.getBugImgList())
                || CollectionUtils.isEmpty(request.getTestCaseList()) || StringUtils.isEmpty(request.getConclusion()) || StringUtils.isEmpty(request.getSuggestion())) {
            return RpcResultUtil.fail("参数缺省");
        }

        LocalDateTime lastSubmitTime = LocalDateTimeUtil.asLocalDateTime(request.getLastSubmitTime());
        if (LocalDateTime.now().isAfter(lastSubmitTime)) {
            return RpcResultUtil.fail("已超过最晚提交时间,无法提交");
        }

        //对提交的bug截图和测试用例数据进行校验
        for (TestBugImgInfo bugImg : request.getBugImgList()) {
            if (bugImg == null) {
                return RpcResultUtil.fail("不能提交空的截图");
            }
            if (StringUtils.isEmpty(bugImg.getImgUrl())) {
                return RpcResultUtil.fail("截图信息必须包含截图的url");
            }
            bugImg.setTestId(request.getTestId());
        }
        for (TestCaseInfo testCase : request.getTestCaseList()) {
            if (testCase == null) {
                return RpcResultUtil.fail("不能提交空的测试用例");
            }
            testCase.setTestId(request.getTestId());
            if (testCase.getCaseId() == null || StringUtils.isEmpty(testCase.getStatus())
                    || StringUtils.isEmpty(testCase.getRunningTime())) {
                return RpcResultUtil.fail("测试用例信息不完整");
            }
            if (TestCaseStatusEnum.failed.name().equals(testCase.getDefectLevel())) {
                //如果testCase为failed，那么必须有defectLevel,errorMsg,reproduceSteps
                if (StringUtils.isEmpty(testCase.getDefectLevel()) || StringUtils.isEmpty(testCase.getErrorMsg()) || StringUtils.isEmpty(testCase.getReproduceSteps())) {
                    return RpcResultUtil.fail("测试用例信息不完整");
                }
            }
        }


        TestDAO testDAO = testManager.getTestInfoByTestId(request.getTestId());
        if (testDAO == null) {
            return RpcResultUtil.fail("testId不存在对应的信息");
        }
        if (!testDAO.getWorkerId().equals(request.getWorkerId())) {
            return RpcResultUtil.fail("测试不属于你,无法提交");
        }
        if (TestStateEnum.cancel.equals(testDAO.getState())) {
            return RpcResultUtil.fail("测试已取消,无法提交");
        }

        testManager.submitTest(testDAO.getState(), TestConvert.INSTANCE.convertSubmitTestRequest2TestDAO(request)
                , TestBugImgConvert.INSTANCE.convertInfoList2DAOList(request.getBugImgList())
                , TestCaseConvert.INSTANCE.convertTestCaseInfoList2TestCaseDAOList(request.getTestCaseList()));

        SubmitTestResponse response = new SubmitTestResponse();
        return RpcResultUtil.success(response);
    }


    @Override
    public RpcResult<FinishedTestInfoResponse> getTestInfoWithHighEvaluationByTaskId(TestQueryRequest request) {
        if (request == null || request.getTaskId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        FinishedTestInfoResponse response = new FinishedTestInfoResponse();
        List<TestDAO> testDAOList = testManager.getTestDAOListByTaskIdAndState(request.getTaskId(), TestStateEnum.finish);
        if (CollectionUtils.isEmpty(testDAOList)) {
            response.setHighFinishedTestInfoList(Collections.emptyList());
            return RpcResultUtil.success(response);
        }

        List<FinishedTestInfo> highFinishedTestInfoList = new ArrayList<>();

        for (TestDAO testDAO : testDAOList) {
            List<TestCollaborationRecordDAO> testCollaborationRecordDAOList = testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId(testDAO.getTestId());
            int collaborationRecordNumber = testCollaborationRecordDAOList.size();

            int evaluationTotal = 0;
            for (TestCollaborationRecordDAO testCollaborationRecordDAO : testCollaborationRecordDAOList) {
                evaluationTotal = evaluationTotal + testCollaborationRecordDAO.getEvaluation();
            }
            double averageEvaluation=0.0;
            if(collaborationRecordNumber>0){
                averageEvaluation = ((double) evaluationTotal) / ((double) collaborationRecordNumber);
            }
            if ((request.getEvaluationThreshold() != null && averageEvaluation >= request.getEvaluationThreshold())
                    || averageEvaluation >= evaluationThreshold) {
                FinishedTestInfo highFinishedTestInfo = new FinishedTestInfo();
                highFinishedTestInfo.setTestId(testDAO.getTestId());
                highFinishedTestInfo.setWorkerId(testDAO.getWorkerId());
                highFinishedTestInfo.setCollaborationNumber(collaborationRecordNumber);
                highFinishedTestInfo.setAverageEvaluation(averageEvaluation);
                highFinishedTestInfoList.add(highFinishedTestInfo);
            }
        }
        highFinishedTestInfoList.sort(Comparator.comparing(FinishedTestInfo::getAverageEvaluation, Comparator.reverseOrder()));
        response.setHighFinishedTestInfoList(highFinishedTestInfoList);
        return RpcResultUtil.success(response);
    }


    @Override
    public RpcResult<FinishedTestInfoResponse> getSimpleFinishedTestByTaskId(TestQueryRequest request) {
        if (request == null || request.getTaskId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }

        FinishedTestInfoResponse response = new FinishedTestInfoResponse();
        List<TestDAO> testDAOList = testManager.getTestDAOListByTaskIdAndState(request.getTaskId(), TestStateEnum.finish);

        if (CollectionUtils.isEmpty(testDAOList)) {
            response.setHighFinishedTestInfoList(Collections.emptyList());
            return RpcResultUtil.success(response);
        }

        List<FinishedTestInfo> finishedTestInfoList = new ArrayList<>(testDAOList.size());

        for (TestDAO testDAO : testDAOList) {
            List<TestCollaborationRecordDAO> testCollaborationRecordDAOList = testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId(testDAO.getTestId());
            int collaborationRecordNumber = testCollaborationRecordDAOList.size();
            FinishedTestInfo finishedTestInfo = new FinishedTestInfo();
            finishedTestInfo.setTestId(testDAO.getTestId());
            finishedTestInfo.setWorkerId(testDAO.getWorkerId());
            finishedTestInfo.setFinishTime(LocalDateTimeUtil.asString(testDAO.getFinishTime()));
            finishedTestInfo.setCollaborationNumber(collaborationRecordNumber);
            //计算平均得分
            if (collaborationRecordNumber > 0) {
                int evaluationTotal = 0;
                for (TestCollaborationRecordDAO testCollaborationRecordDAO : testCollaborationRecordDAOList) {
                    evaluationTotal = evaluationTotal + testCollaborationRecordDAO.getEvaluation();
                }
                double averageEvaluation = ((double) evaluationTotal) / ((double) collaborationRecordNumber);
                finishedTestInfo.setAverageEvaluation(averageEvaluation);
            }
            finishedTestInfoList.add(finishedTestInfo);
        }

        response.setHighFinishedTestInfoList(finishedTestInfoList);
        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<FinishedTestInfoMapResponse> getSimpleFinishedTestMapByTestIdList(TestBatchQueryRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getTestIdSet())) {
            return RpcResultUtil.fail("参数缺省");
        }

        FinishedTestInfoMapResponse response = new FinishedTestInfoMapResponse();

        List<TestDAO> testDAOList = testManager.getTestDAOListByTestIdList(new ArrayList<>(request.getTestIdSet()));

        if (CollectionUtils.isEmpty(testDAOList)) {
            response.setMap(Collections.emptyMap());
            return RpcResultUtil.success(response);
        }

        Map<Long, FinishedTestInfo> map = new HashMap<>(testDAOList.size());

        for (TestDAO testDAO : testDAOList) {

            List<TestCollaborationRecordDAO> testCollaborationRecordDAOList = testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId(testDAO.getTestId());
            int collaborationRecordNumber = testCollaborationRecordDAOList.size();

            FinishedTestInfo finishedTestInfo = new FinishedTestInfo();
            finishedTestInfo.setTestId(testDAO.getTestId());
            finishedTestInfo.setWorkerId(testDAO.getWorkerId());
            finishedTestInfo.setFinishTime(LocalDateTimeUtil.asString(testDAO.getFinishTime()));
            finishedTestInfo.setCollaborationNumber(collaborationRecordNumber);
            //计算平均得分
            if (collaborationRecordNumber > 0) {
                int evaluationTotal = 0;
                for (TestCollaborationRecordDAO testCollaborationRecordDAO : testCollaborationRecordDAOList) {
                    evaluationTotal = evaluationTotal + testCollaborationRecordDAO.getEvaluation();
                }
                double averageEvaluation = ((double) evaluationTotal) / ((double) collaborationRecordNumber);
                finishedTestInfo.setAverageEvaluation(averageEvaluation);
            }
            //


            map.put(testDAO.getTestId(), finishedTestInfo);

        }
        response.setMap(map);
        return RpcResultUtil.success(response);

    }

    @Override
    public RpcResult<SubmitCollaborationResponse> submitCollaboration(SubmitCollaborationRequest request) {
        if (request == null || request.getCollaborationInfo() == null) {
            return RpcResultUtil.fail("未传入数据");
        }
        CollaborationInfo collaborationInfo = request.getCollaborationInfo();
        if (collaborationInfo.getTestId() == null || collaborationInfo.getWorkerId() == null || CollectionUtils.isEmpty(collaborationInfo.getTestTools())
                || StringUtils.isEmpty(collaborationInfo.getConclusion()) || StringUtils.isEmpty(collaborationInfo.getSuggestion()) || StringUtils.isEmpty(collaborationInfo.getOperatingSystem())) {
            return RpcResultUtil.fail("参数缺省");
        }

        CollaborationDAO collaborationDAO = CollaborationConvert.INSTANCE.convertInfo2DAO(collaborationInfo);
        List<CollaborationCaseDAO> collaborationCaseDAOList = null;
        if (CollectionUtils.isEmpty(collaborationInfo.getCollaborationCaseInfoList())) {
            collaborationCaseDAOList = Collections.emptyList();
        } else {
            collaborationCaseDAOList = CollaborationCaseConvert.INSTANCE.convertInfoList2DAOList(collaborationInfo.getCollaborationCaseInfoList());
        }

        Long resultId = collaborationManager.submitCollaboration(collaborationDAO, collaborationCaseDAOList);
        SubmitCollaborationResponse response = new SubmitCollaborationResponse();
        response.setId(resultId);
        return RpcResultUtil.success(response);
    }

    /**
     * 根据collaborationId唯一查询所有信息
     * @param collaborationIdRequest
     * @return
     */
    @Override
    public RpcResult<CollaborationInfoResponse> getCollaborationById(CollaborationIdRequest collaborationIdRequest) {
        if (collaborationIdRequest == null || collaborationIdRequest.getId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }
        CollaborationInfoResponse response = new CollaborationInfoResponse();


        CollaborationDAO collaborationDAO = collaborationManager.getCollaborationDAOById(collaborationIdRequest.getId());
        if (collaborationDAO == null) {
            //不存在的情况
            return RpcResultUtil.fail("id不存在对应的协作报告");
        }

        List<CollaborationCaseDAO> collaborationCaseDAOList = collaborationManager.getCollaborationCaseDAOListByCollaborationDAO(collaborationDAO);

        CollaborationInfo collaborationInfo = CollaborationConvert.INSTANCE.convertDAO2Info(collaborationDAO);
        if (CollectionUtils.isEmpty(collaborationCaseDAOList)) {
            collaborationInfo.setCollaborationCaseInfoList(Collections.emptyList());
        } else {
            collaborationInfo.setCollaborationCaseInfoList(CollaborationCaseConvert.INSTANCE.convertDAOList2InfoList(collaborationCaseDAOList));
        }

        response.setCollaborationInfo(collaborationInfo);
        return RpcResultUtil.success(response);
    }

    /**
     * 根据testId查询未被删除的CollaborationInfo列表，不带case
     * @param testQueryRequest
     * @return
     */
    @Override
    public RpcResult<CollaborationInfoListResponse> getSimpleCollaborationListByTestId(TestQueryRequest testQueryRequest) {
        if (testQueryRequest == null || testQueryRequest.getTestId() == null) {
            return RpcResultUtil.fail("参数缺型");
        }
        Long testId = testQueryRequest.getTestId();

        List<CollaborationDAO> collaborationDAOList = collaborationManager.getUsableCollaborationDAOListByTestId(testId);

        CollaborationInfoListResponse response = new CollaborationInfoListResponse();
        if (CollectionUtils.isEmpty(collaborationDAOList)) {
            response.setCollaborationInfoList(Collections.emptyList());
        } else {
            response.setCollaborationInfoList(CollaborationConvert.INSTANCE.convertDAOList2InfoList(collaborationDAOList));
        }
        return RpcResultUtil.success(response);
    }


    @Override
    public RpcResult<WhetherWorkerHasSubmitCollaborationResponse> whetherWorkerHasSubmitCollaboration(WhetherWorkerHasSubmitCollaborationRequest request) {
        if (request == null || request.getTestId() == null || request.getWorkerId() == null) {
            return RpcResultUtil.fail("参数缺省");
        }

        Long id = collaborationManager.whereWorkerHasSubmitCollaboration(request.getTestId(), request.getWorkerId());
        WhetherWorkerHasSubmitCollaborationResponse response = new WhetherWorkerHasSubmitCollaborationResponse();
        response.setId(id);
        return RpcResultUtil.success(response);
    }

    @Override
    public RpcResult<TestCountResponse> getTestCountByTaskId(TestQueryRequest request) {
        if (request == null || request.getTaskId()==null) {
            return RpcResultUtil.fail("参数缺省");
        }
        Integer workerNumberByTaskId = testCountManager.getWorkerNumberByTaskId(request.getTaskId());
        return RpcResultUtil.success(new TestCountResponse(workerNumberByTaskId));
    }
}
