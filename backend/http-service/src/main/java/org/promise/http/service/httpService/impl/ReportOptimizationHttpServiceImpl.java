package org.promise.http.service.httpService.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.promise.common.constant.ExceptionEnum;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.HttpResultUtil;
import org.promise.common.util.JsonUtil;
import org.promise.common.util.LocalDateTimeUtil;
import org.promise.http.service.convert.TaskConvert;
import org.promise.http.service.httpService.ReportOptimizationHttpService;
import org.promise.http.service.httpService.dto.ClassifySimDTO;
import org.promise.http.service.httpService.dto.ReportClassifyDTO;
import org.promise.http.service.httpService.dto.ReportClusteringDTO;
import org.promise.http.service.httpService.dto.WordCloudDTO;
import org.promise.http.service.util.CoordinateConvertUtil;
import org.promise.http.service.util.HttpRequestUtil;
import org.promise.http.service.vo.report.*;
import org.promise.http.service.vo.report.classify.ReportClassVO;
import org.promise.http.service.vo.report.classify.ReportClassifyVO;
import org.promise.http.service.vo.report.cluster.ReportClusteringVO;
import org.promise.http.service.vo.report.integration.*;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author moqi
 * @date 2022/5/16 20:25
 */

@Slf4j
@Service
public class ReportOptimizationHttpServiceImpl implements ReportOptimizationHttpService {


    private static final double EVALUATION_WEIGHT = 15.0;

    private static final double COLLABORATION_WEIGHT = 15.0;

    private static final double TEST_CASE_WEIGHT = 25.0;

    private static final double TEST_TOOL_WEIGHT = 10.0;

    private static final double WRONG_CASE_WEIGHT = 25.0;

    private static final double BUG_IMG_WEIGHT = 5.0;

    private static final double WORD_WEIGHT = 5.0;

    private static final int MAX_FILTER_COUNT = 5;

    @Resource
    private HttpRequestUtil httpRequestUtil;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private ReportOptimizationService reportOptimizationService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private UserService userService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private TestService testService;

    @DubboReference(group = "${dubbo.registry.group}", check = false)
    private PublishService publishService;

    @Resource
    private TaskConvert taskConvert;

    @Override
    public HttpResult<List<ReportClusteringVO>> clusteringReport(Long taskId, Integer k) throws Exception {
        String responseBody = httpRequestUtil.clusteringReport(taskId, k);
        List<ReportClusteringDTO> reportClusteringDTOList = JsonUtil.fromJsonToList(responseBody, ReportClusteringDTO.class);
        log.info("python模型结果{}", reportClusteringDTOList);
        List<ReportClusteringVO> reportClusteringVOList = new ArrayList<>();
        for (ReportClusteringDTO reportClusteringDTO : reportClusteringDTOList) {
            ReportClusteringVO reportClusteringVO = new ReportClusteringVO();
            reportClusteringVO.setXAxis(reportClusteringDTO.getFeature().get(0));
            reportClusteringVO.setYAxis(reportClusteringDTO.getFeature().get(1));
            reportClusteringVO.setTestId(reportClusteringDTO.getReportId());
            reportClusteringVO.setClusteringType(Integer.parseInt(reportClusteringDTO.getLabel()));
            reportClusteringVOList.add(reportClusteringVO);
        }
        //获取报告的所有信息
        Set<Long> testIdSet = new HashSet<>();
        for (ReportClusteringVO reportClusteringVO : reportClusteringVOList) {
            testIdSet.add(reportClusteringVO.getTestId());
        }
        if (CollectionUtils.isEmpty(testIdSet)) {
            return HttpResultUtil.fail("当前任务不存在完成的报告");
        }
        Map<Long, UserInfo> userInfoMap = getUserInfoMapByTestIdSet(testIdSet);

        //为返回体中加入工人信息
        for (ReportClusteringVO reportClusteringVO : reportClusteringVOList) {
            UserInfo userInfo = userInfoMap.get(reportClusteringVO.getTestId());
            reportClusteringVO.setWorkerId(userInfo.getUserId());
            reportClusteringVO.setNickname(userInfo.getNickName());
            reportClusteringVO.setAvatar(userInfo.getAvatar());
        }

        return HttpResultUtil.success(reportClusteringVOList);
    }

    @Override
    public HttpResult<List<ReportClassifyVO>> classifyReport(Long taskId) throws Exception {
        String responseBody = httpRequestUtil.classifyReport(taskId);
        List<ReportClassifyDTO> reportClassifyDTOList = JsonUtil.fromJsonToList(responseBody, ReportClassifyDTO.class);
        log.info("python模型结果{}", reportClassifyDTOList);
        //将数据处理成要求的格式
        Map<String, List<ReportClassVO>> map = new HashMap<>();
        for (ReportClassifyDTO reportClassifyDTO : reportClassifyDTOList) {
            List<ReportClassVO> reportClassVOList = map.getOrDefault(reportClassifyDTO.getLabel(), new ArrayList<>());
            ReportClassVO reportClassVO = new ReportClassVO();
            reportClassVO.setTestId(reportClassifyDTO.getReportId());
            reportClassVOList.add(reportClassVO);
            map.put(reportClassifyDTO.getLabel(), reportClassVOList);
        }
        List<ReportClassifyVO> res = new ArrayList<>();
        for (Map.Entry<String, List<ReportClassVO>> stringListEntry : map.entrySet()) {
            ReportClassifyVO reportClassifyVO = new ReportClassifyVO(stringListEntry.getKey(), stringListEntry.getValue());
            res.add(reportClassifyVO);
        }

        //获取报告的完整信息
        Set<Long> testIdSet = new HashSet<>();
        for (ReportClassifyVO reportClassifyVO : res) {
            for (ReportClassVO reportClassVO : reportClassifyVO.getChildren()) {
                testIdSet.add(reportClassVO.getTestId());
            }
        }
        Map<Long, UserInfo> userInfoMap = getUserInfoMapByTestIdSet(testIdSet);

        for (ReportClassifyVO reportClassifyVO : res) {
            for (ReportClassVO reportClassVO : reportClassifyVO.getChildren()) {
                UserInfo userInfo = userInfoMap.get(reportClassVO.getTestId());
                reportClassVO.setWorkerId(userInfo.getUserId());
                reportClassVO.setName(userInfo.getNickName());
                reportClassVO.setAvatar(userInfo.getAvatar());
            }
        }
        return HttpResultUtil.success(res);
    }

    @Override
    public HttpResult<List<ReportScoreVO>> sortReport(Long taskId) {
        //返回结果
        List<ReportScoreVO> res = new ArrayList<>();

        //先对所有评价进行排序
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        testQueryRequest.setEvaluationThreshold(0.0);
        RpcResult<FinishedTestInfoResponse> testInfoWithHighEvaluationRpcResult =
                testService.getTestInfoWithHighEvaluationByTaskId(testQueryRequest);
        if (testInfoWithHighEvaluationRpcResult.getData() == null) {
            return HttpResultUtil.fail(testInfoWithHighEvaluationRpcResult.getMessage());
        }
        List<FinishedTestInfo> finishedTestInfoList = testInfoWithHighEvaluationRpcResult.getData().getHighFinishedTestInfoList();
        for (FinishedTestInfo finishedTestInfo : finishedTestInfoList) {
            ReportScoreVO reportScoreVO = new ReportScoreVO(finishedTestInfo.getTestId()
                    , finishedTestInfo.getAverageEvaluation(), finishedTestInfo.getWorkerId());
            res.add(reportScoreVO);
        }

        if (CollectionUtils.isEmpty(res)) {
            return HttpResultUtil.success(res);
        }
        //查出每个评价的发布者信息并写入返回的数据结构
        Set<Long> userIds = new HashSet<>();
        for (ReportScoreVO reportScoreVO : res) {
            userIds.add(reportScoreVO.getWorkerId());
        }
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(userIds);
        RpcResult<BatchGetUserInfoResponse> userInfoMapRpcResult = userService.batchGetUserInfoByUserId(batchGetUserInfoRequest);
        if (userInfoMapRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoMapRpcResult.getMessage());
        }
        Map<Long, UserInfo> userInfoMap = userInfoMapRpcResult.getData().getUserInfos();
        for (ReportScoreVO reportScoreVO : res) {
            UserInfo userInfo = userInfoMap.get(reportScoreVO.getWorkerId());
            reportScoreVO.setAvatar(userInfo.getAvatar());
            reportScoreVO.setNickname(userInfo.getNickName());
        }
        return HttpResultUtil.success(res);
    }

    @Override
    public HttpResult<List<ReportScoreVO>> filterReport(Long taskId) {
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        RpcResult<TestSummaryResponse> testSummaryRpcResult = reportOptimizationService.getTestSummaryByTaskId(testQueryRequest);
        if (testSummaryRpcResult.getData() == null) {
            return HttpResultUtil.fail(testSummaryRpcResult.getMessage());
        }
        List<TestSummaryInfo> testSummaryInfoList = testSummaryRpcResult.getData().getTestSummaryInfoList();
        List<ReportScoreVO> reportScoreVOList = new ArrayList<>();
        for (TestSummaryInfo testSummaryInfo : testSummaryInfoList) {
            reportScoreVOList.add(new ReportScoreVO(testSummaryInfo.getTestId(), getScore(testSummaryInfo), testSummaryInfo.getWorkerId()));
        }
        reportScoreVOList.sort(Comparator.comparing(ReportScoreVO::getScore, Comparator.reverseOrder()));

        int actualCount = reportScoreVOList.size() / 2 + 1;
        actualCount = Math.min(actualCount, MAX_FILTER_COUNT);
        reportScoreVOList = reportScoreVOList.subList(0, actualCount);

        //查询用户信息
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest();
        Set<Long> userIdSet = new HashSet<>();
        for (TestSummaryInfo testSummaryInfo : testSummaryInfoList) {
            userIdSet.add(testSummaryInfo.getWorkerId());
        }
        batchGetUserInfoRequest.setUserIdSet(userIdSet);
        RpcResult<BatchGetUserInfoResponse> userInfoRpcResult = userService.batchGetUserInfoByUserId(batchGetUserInfoRequest);
        if (userInfoRpcResult.getData() == null) {
            return HttpResultUtil.fail(userInfoRpcResult.getMessage());
        }
        Map<Long, UserInfo> userInfoMap = userInfoRpcResult.getData().getUserInfos();

        //将用户信息写入返回结果
        for (ReportScoreVO reportScoreVO : reportScoreVOList) {
            UserInfo userInfo = userInfoMap.get(reportScoreVO.getWorkerId());
            reportScoreVO.setNickname(userInfo.getNickName());
            reportScoreVO.setAvatar(userInfo.getAvatar());
        }

        return HttpResultUtil.success(reportScoreVOList);
    }

    @Override
    public HttpResult<ReportIntegrationVO> integrateReport(Long taskId) throws Exception {
        TestQueryRequest testQueryRequest = new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        RpcResult<ReportIntegrationResponse> reportIntegrationRpcResult = reportOptimizationService.getReportIntegrationResponse(testQueryRequest);
        if (reportIntegrationRpcResult.getData() == null) {
            return HttpResultUtil.fail(reportIntegrationRpcResult.getMessage());
        }
        ReportIntegrationInfo reportIntegrationInfo = reportIntegrationRpcResult.getData().getReportIntegrationInfo();

        ReportIntegrationVO reportIntegrationVO = constructReportIntegrationVO(reportIntegrationInfo);

        String wordCloudErr = httpRequestUtil.getWordCloudErr(taskId);
        List<WordCloudDTO> wordCloudErrList = JsonUtil.fromJsonToList(wordCloudErr, WordCloudDTO.class);
        log.info("python模型结果{}", wordCloudErrList);
        List<WordCloudVO> wordCloudErrVOList = new ArrayList<>();
        for (WordCloudDTO wordCloudDTO : wordCloudErrList) {
            wordCloudErrVOList.add(new WordCloudVO(wordCloudDTO.getWord(), wordCloudDTO.getCount()));
        }
        reportIntegrationVO.setDefectWordCloud(wordCloudErrVOList);

        String wordCloudSug = httpRequestUtil.getWordCloudSug(taskId);
        String wordCloudClu = httpRequestUtil.getWordCloudClu(taskId);
        List<WordCloudDTO> wordCloudSugList = JsonUtil.fromJsonToList(wordCloudSug, WordCloudDTO.class);
        log.info("python模型结果{}", wordCloudSugList);
        List<WordCloudDTO> wordCloudCluList = JsonUtil.fromJsonToList(wordCloudClu, WordCloudDTO.class);
        log.info("python模型结果{}", wordCloudCluList);
        List<WordCloudVO> suggestWordCloud = new ArrayList<>();
        for (WordCloudDTO wordCloudDTO : wordCloudSugList) {
            suggestWordCloud.add(new WordCloudVO(wordCloudDTO.getWord(), wordCloudDTO.getCount()));
        }
        for (WordCloudDTO wordCloudDTO : wordCloudCluList) {
            suggestWordCloud.add(new WordCloudVO(wordCloudDTO.getWord(), wordCloudDTO.getCount()));
        }
        reportIntegrationVO.setSuggestWordCloud(suggestWordCloud);

        //构建报告间相似关系
        String classifySim = httpRequestUtil.getClassifySim(taskId);
        List<ClassifySimDTO> classifySimDTOList = JsonUtil.fromJsonToList(classifySim, ClassifySimDTO.class);
        ReportRelationVO reportRelationVO = constructReportRelationVO(classifySimDTOList);
        reportIntegrationVO.setReportRelation(reportRelationVO);

        return HttpResultUtil.success(reportIntegrationVO);
    }

    @Override
    public HttpResult<Boolean> whetherOptimizeReport(Long taskId) {
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        RpcResult<FinishedTestInfoResponse> finishedTestInfoRpcResult = testService.getSimpleFinishedTestByTaskId(testQueryRequest);
        if(finishedTestInfoRpcResult.getData()==null){
            return HttpResultUtil.fail(finishedTestInfoRpcResult.getMessage());
        }
        Integer finishedTestCount=finishedTestInfoRpcResult.getData().getHighFinishedTestInfoList().size();
        if(finishedTestCount<2){
            return HttpResultUtil.success(false);
        }

        RpcResult<TaskResponse> taskInfoRpcResult = publishService.getTaskById(new TaskIdRequest(taskId));
        if(taskInfoRpcResult.getData()==null){
            return HttpResultUtil.fail(taskInfoRpcResult.getMessage());
        }
        TaskInfo taskInfo = taskInfoRpcResult.getData().getTaskInfo();
        if(LocalDateTime.now().isAfter(LocalDateTimeUtil.asLocalDateTime(taskInfo.getTestEndTime()))){
            return HttpResultUtil.success(true);
        }

        RpcResult<TestCountResponse> testCountRpcResult = testService.getTestCountByTaskId(testQueryRequest);
        if(testCountRpcResult.getData()==null){
            return HttpResultUtil.fail(taskInfoRpcResult.getMessage());
        }
        Integer testCount = testCountRpcResult.getData().getCount();

        return HttpResultUtil.success(finishedTestCount.equals(testCount));
    }

    @Override
    public HttpResult<Integer> getFinishedReportCount(Long taskId) {
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(taskId);
        RpcResult<FinishedTestInfoResponse> finishedTestInfoRpcResult = testService.getSimpleFinishedTestByTaskId(testQueryRequest);
        if(finishedTestInfoRpcResult.getData()==null){
            return HttpResultUtil.fail(finishedTestInfoRpcResult.getMessage());
        }
        Integer finishedTestCount=finishedTestInfoRpcResult.getData().getHighFinishedTestInfoList().size();
        return HttpResultUtil.success(finishedTestCount);
    }

    private Map<Long, UserInfo> getUserInfoMapByTestIdSet(Set<Long> testIdSet) {
        TestBatchQueryRequest testBatchQueryRequest = new TestBatchQueryRequest(testIdSet);
        RpcResult<FinishedTestInfoMapResponse> testMapRpcResult = testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest);
        if (testMapRpcResult.getData() == null) {
            ExceptionEnum.NullException.maybeThrow();
        }
        Map<Long, FinishedTestInfo> testMap = testMapRpcResult.getData().getMap();
        Set<Long> workerIdSet = new HashSet<>();
        for (FinishedTestInfo value : testMap.values()) {
            workerIdSet.add(value.getWorkerId());
        }
        BatchGetUserInfoRequest batchGetUserInfoRequest = new BatchGetUserInfoRequest(workerIdSet);
        RpcResult<BatchGetUserInfoResponse> userInfoMapRpcResult = userService.batchGetUserInfoByUserId(batchGetUserInfoRequest);
        if (userInfoMapRpcResult.getData() == null) {
            ExceptionEnum.NullException.maybeThrow();
        }
        Map<Long, UserInfo> userInfoMap = userInfoMapRpcResult.getData().getUserInfos();
        Map<Long, UserInfo> res = new HashMap<>();
        for (Long testId : testIdSet) {
            UserInfo userInfo = userInfoMap.get(testMap.get(testId).getWorkerId());
            res.put(testId, userInfo);
        }
        return res;
    }


    private double getScore(TestSummaryInfo testSummaryInfo) {
        double averageEvaluationScore = scoreMappingFunction(testSummaryInfo.getAverageEvaluation());
        double collaborationScore = scoreMappingFunction(testSummaryInfo.getCollaborationNumber());
        double testCaseScore = scoreMappingFunction(testSummaryInfo.getTestCaseCount());
        double testToolScore = scoreMappingFunction(testSummaryInfo.getTestToolCount());
        double wrongCaseScore = scoreMappingFunction(testSummaryInfo.getWrongCaseCount());
        double bugImgScore = scoreMappingFunction(testSummaryInfo.getBugImgCount());
        double wordScore = scoreMappingFunction(testSummaryInfo.getWordCount() / 10.0);

        double score = averageEvaluationScore * EVALUATION_WEIGHT + collaborationScore * COLLABORATION_WEIGHT
                + testCaseScore * TEST_CASE_WEIGHT + testToolScore * TEST_TOOL_WEIGHT
                + wrongCaseScore * WRONG_CASE_WEIGHT + bugImgScore * BUG_IMG_WEIGHT
                + wordScore * WORD_WEIGHT;
        return Math.sqrt(score) * 10.0;
    }

    private static double scoreMappingFunction(double x) {
        return 1.0 - Math.exp(-x / 3.0);
    }

    private static double distanceMappingFunction(double x) {
        return (Math.E / Math.exp(x)-1)*7+1;
    }


    private ReportIntegrationVO constructReportIntegrationVO(ReportIntegrationInfo reportIntegrationInfo) {
        ReportIntegrationVO res = new ReportIntegrationVO();

        List<OperatingSystemVO> testEnv = new ArrayList<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : reportIntegrationInfo.getOperatingSystemCountMap().entrySet()) {
            OperatingSystemVO operatingSystemVO = new OperatingSystemVO(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
            testEnv.add(operatingSystemVO);
        }
        res.setTestEnv(testEnv);

        List<TestToolVO> testTool = new ArrayList<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : reportIntegrationInfo.getTestToolCountMap().entrySet()) {
            TestToolVO testToolVO = new TestToolVO(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
            testTool.add(testToolVO);
        }
        res.setTestTool(testTool);

        List<CaseStatisticsVO> caseStatisticsVOList = new ArrayList<>();
        Set<Long> caseIdSet = new HashSet<>();
        caseIdSet.addAll(reportIntegrationInfo.getRightCaseCountMap().keySet());
        caseIdSet.addAll(reportIntegrationInfo.getWrongCaseCountMap().keySet());
        for (Long id : caseIdSet) {
            CaseStatisticsVO caseStatisticsVO = new CaseStatisticsVO();
            caseStatisticsVO.setCaseId(id);
            caseStatisticsVO.setPass(reportIntegrationInfo.getRightCaseCountMap().getOrDefault(id, 0));
            caseStatisticsVO.setFailed(reportIntegrationInfo.getWrongCaseCountMap().getOrDefault(id, 0));
            caseStatisticsVOList.add(caseStatisticsVO);
        }
        res.setCaseStatistics(caseStatisticsVOList);
        return res;
    }

    private ReportRelationVO constructReportRelationVO(List<ClassifySimDTO> classifySimDTOList) {
        ReportRelationVO res = new ReportRelationVO();
        res.getCategories().addAll(Arrays.asList(new ReportRelationCategoryVO("root"),
                new ReportRelationCategoryVO("class"),
                new ReportRelationCategoryVO("report")));

        //加入根节点
        ReportRelationNodeVO root = new ReportRelationNodeVO("0", "root", 30.0, 0.0, 0.0, 0, 0);
        res.getNodes().add(root);
        int nodeCount = 1;

        //尝试对第二层节点计数
        int classNum = 0;
        List<ClassifySimDTO> classTypeNode = new ArrayList<>();
        for (ClassifySimDTO classifySimDTO : classifySimDTOList) {
            if (classifySimDTO.getLabelName() != null) {
                classNum++;
                classTypeNode.add(classifySimDTO);
            }
        }
        //对根节点的子节点数进行赋值
        root.setValue(classNum);

        //尝试创建第二层节点
        for (int i = 0; i < classTypeNode.size(); i++) {
            ClassifySimDTO classifySimDTO = classTypeNode.get(i);
            CoordinateConvertUtil.PolarCoordinate polarCoordinate = new CoordinateConvertUtil.PolarCoordinate(0, 0);
            polarCoordinate.setDistance(distanceMappingFunction(classifySimDTO.getSim()));
            polarCoordinate.setAngle(2 * Math.PI / classNum * i);
            CoordinateConvertUtil.RectangularCoordinate rectangularCoordinate = CoordinateConvertUtil.polar2Rectangular(polarCoordinate);
            ReportRelationNodeVO reportRelationNodeVO = new ReportRelationNodeVO(
                    String.valueOf(nodeCount++),
                    classifySimDTO.getLabelName(),
                    20.0,
                    rectangularCoordinate.getX(),
                    rectangularCoordinate.getY(),
                    0,
                    1
            );
            res.getNodes().add(reportRelationNodeVO);
            res.getLinks().add(new ReportRelationLinkVO("0", reportRelationNodeVO.getId()));
        }

        //获取用户信息
        Set<Long> testIdSet=new HashSet<>();
        for (ClassifySimDTO classifySimDTO : classifySimDTOList) {
            if(classifySimDTO.getReportId()!=null){
                testIdSet.add(classifySimDTO.getReportId());
            }
        }
        Map<Long, UserInfo> userInfoMap = getUserInfoMapByTestIdSet(testIdSet);

        //创建第三层节点
        for (int i = 1; i < classNum + 1; i++) {
            ReportRelationNodeVO fatherNode = res.getNodes().get(i);
            List<ClassifySimDTO> childrenList = new ArrayList<>();
            for (ClassifySimDTO classifySimDTO : classifySimDTOList) {
                if (fatherNode.getName().equals(classifySimDTO.getLabel())) {
                    childrenList.add(classifySimDTO);
                }
            }
            CoordinateConvertUtil.PolarCoordinate fatherPolar =
                    CoordinateConvertUtil.rectangular2Polar(
                            new CoordinateConvertUtil.RectangularCoordinate(fatherNode.getX(), fatherNode.getY()),
                            0, 0);
            double startAngel = fatherPolar.getAngle() - Math.PI / 2;

            int num = childrenList.size();
            fatherNode.setValue(num);
            for (int j = 0; j < num; j++) {
                ClassifySimDTO classifySimDTO = childrenList.get(j);
                CoordinateConvertUtil.PolarCoordinate polarCoordinate =
                        new CoordinateConvertUtil.PolarCoordinate(fatherNode.getX(), fatherNode.getY());
                polarCoordinate.setDistance(distanceMappingFunction(classifySimDTO.getSim()));
                polarCoordinate.setAngle(startAngel + Math.PI / num * (j + 0.5));
                CoordinateConvertUtil.RectangularCoordinate rectangularCoordinate = CoordinateConvertUtil.polar2Rectangular(polarCoordinate);
                ReportRelationNodeVO reportRelationNodeVO = new ReportRelationNodeVO(
                        String.valueOf(nodeCount++),
                        userInfoMap.get(classifySimDTO.getReportId()).getNickName(),
                        10.0,
                        rectangularCoordinate.getX(),
                        rectangularCoordinate.getY(),
                        Math.toIntExact(classifySimDTO.getReportId()),
                        2
                );
                res.getNodes().add(reportRelationNodeVO);
                res.getLinks().add(new ReportRelationLinkVO(fatherNode.getId(), reportRelationNodeVO.getId()));
            }
        }

        return res;
    }

}
