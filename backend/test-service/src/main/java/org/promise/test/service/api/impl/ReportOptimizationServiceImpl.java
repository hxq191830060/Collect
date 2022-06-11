package org.promise.test.service.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.promise.common.result.rpc.RpcResult;
import org.promise.common.util.RpcResultUtil;
import org.promise.test.service.api.api.ReportOptimizationService;
import org.promise.test.service.api.info.ReportIntegrationInfo;
import org.promise.test.service.api.info.TestSummaryInfo;
import org.promise.test.service.api.request.TestQueryRequest;
import org.promise.test.service.api.response.ReportIntegrationResponse;
import org.promise.test.service.api.response.TestSummaryResponse;
import org.promise.test.service.manager.*;
import org.promise.test.service.manager.dao.*;
import org.promise.test.service.mapperService.constant.TestCaseStatusEnum;
import org.promise.test.service.mapperService.constant.TestStateEnum;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author moqi
 * @date 2022/5/19 14:42
 */

@DubboService(interfaceClass = ReportOptimizationService.class, group = "${dubbo.registry.group}")
@Slf4j
public class ReportOptimizationServiceImpl implements ReportOptimizationService {


    @Resource
    private TestManager testManager;

    @Resource
    private TestBugImgManager testBugImgManager;

    @Resource
    private TestCollaborationRecordManager testCollaborationRecordManager;

    @Resource
    private TestCaseManager testCaseManager;

    @Resource
    private CollaborationManager collaborationManager;


    @Override
    public RpcResult<TestSummaryResponse> getTestSummaryByTaskId(TestQueryRequest testQueryRequest) {
        if(testQueryRequest==null) {
            return RpcResultUtil.fail("request为空");
        }
        if(testQueryRequest.getTaskId()==null){
            return RpcResultUtil.fail("所需参数为空");
        }

        //用于返回结果
        List<TestSummaryInfo> testSummaryInfoList=new ArrayList<>();
        TestSummaryResponse testSummaryResponse=new TestSummaryResponse();

        List<TestDAO> testDAOList = testManager.getTestDAOListByTaskIdAndState(testQueryRequest.getTaskId(), TestStateEnum.finish);
        if (CollectionUtils.isEmpty(testDAOList)) {
            testSummaryResponse.setTestSummaryInfoList(Collections.emptyList());
            return RpcResultUtil.success(testSummaryResponse);
        }

        //对每个测试对象计算其统计数据，并保存
        for (TestDAO testDAO : testDAOList) {
            TestSummaryInfo testSummaryInfo=new TestSummaryInfo();

            //计算平均得分
            List<TestCollaborationRecordDAO> testCollaborationRecordDAOList = testCollaborationRecordManager.getUsableTestCollaborationRecordListByTestId(testDAO.getTestId());
            int collaborationRecordNumber = testCollaborationRecordDAOList.size();
            double averageEvaluation=0.0;
            if(collaborationRecordNumber>0){
                int totalEvaluation=0;
                for (TestCollaborationRecordDAO testCollaborationRecordDAO : testCollaborationRecordDAOList) {
                    totalEvaluation=totalEvaluation+testCollaborationRecordDAO.getEvaluation();
                }
                averageEvaluation= ((double) totalEvaluation) / ((double) collaborationRecordNumber);
            }
            testSummaryInfo.setAverageEvaluation(averageEvaluation);

            //计算总用例数
            List<TestCaseDAO> testcaseList = testCaseManager.getUsableTestCaseListByTestId(testDAO.getTestId());
            testSummaryInfo.setTestCaseCount(testcaseList.size());

            //计算错误用例数
            int wrongCaseCount=0;
            for (TestCaseDAO testCaseDAO : testcaseList) {
                if(testCaseDAO.getStatus().equals(TestCaseStatusEnum.failed)){
                    wrongCaseCount++;
                }
            }
            testSummaryInfo.setWrongCaseCount(wrongCaseCount);

            //计算截图数
            List<TestBugImgDAO> bugImgList = testBugImgManager.getUsableBugImgListByTestId(testDAO.getTestId());
            testSummaryInfo.setBugImgCount(bugImgList.size());

            //计算协作数
            List<CollaborationDAO> collaborationList = collaborationManager.getUsableCollaborationDAOListByTestId(testDAO.getTestId());
            testSummaryInfo.setCollaborationNumber(collaborationList.size());

            //填充其他信息
            testSummaryInfo.setTestId(testDAO.getTestId());
            testSummaryInfo.setWorkerId(testDAO.getWorkerId());
            testSummaryInfo.setTestToolCount(testDAO.getTestTools().size());
            testSummaryInfo.setWordCount(testDAO.getSuggestion().length()+testDAO.getConclusion().length());
            testSummaryInfoList.add(testSummaryInfo);
        }

        testSummaryResponse.setTestSummaryInfoList(testSummaryInfoList);
        return RpcResultUtil.success(testSummaryResponse);
    }

    @Override
    public RpcResult<ReportIntegrationResponse> getReportIntegrationResponse(TestQueryRequest testQueryRequest) {
        if(testQueryRequest==null) {
            return RpcResultUtil.fail("request为空");
        }
        if(testQueryRequest.getTaskId()==null){
            return RpcResultUtil.fail("所需参数为空");
        }

        //用于返回结果
        ReportIntegrationResponse response=new ReportIntegrationResponse();
        ReportIntegrationInfo reportIntegrationInfo=new ReportIntegrationInfo();
        response.setReportIntegrationInfo(reportIntegrationInfo);

        //获取测试报告
        List<TestDAO> testDAOList = testManager.getTestDAOListByTaskIdAndState(testQueryRequest.getTaskId(), TestStateEnum.finish);
        if (CollectionUtils.isEmpty(testDAOList)) {
            response.setReportIntegrationInfo(reportIntegrationInfo);
            return RpcResultUtil.success(response);
        }

        for (TestDAO testDAO : testDAOList) {
            //记录报告本身用例数量
            List<TestCaseDAO> testcaseList = testCaseManager.getUsableTestCaseListByTestId(testDAO.getTestId());
            for (TestCaseDAO testCaseDAO : testcaseList) {
                Long caseId=testCaseDAO.getCaseId();
                if(testCaseDAO.getStatus().equals(TestCaseStatusEnum.failed)){
                    incMap(caseId, reportIntegrationInfo.getRightCaseCountMap());
                }else {
                    incMap(caseId, reportIntegrationInfo.getWrongCaseCountMap());
                }
            }

            //记录报告本身测试环境数
            incMap(testDAO.getOperatingSystem(),reportIntegrationInfo.getOperatingSystemCountMap());

            //记录报告本身工具使用数
            for (String testTool : testDAO.getTestTools()) {
                incMap(testTool,reportIntegrationInfo.getTestToolCountMap());
            }

            //查看其可能存在的协作报告
            List<CollaborationDAO> collaborationDAOList = collaborationManager.getUsableCollaborationDAOListByTestId(testDAO.getTestId());
            for (CollaborationDAO collaborationDAO : collaborationDAOList) {

                //记录协作报告用例数量
                List<CollaborationCaseDAO> collaborationCaseDAOList =
                        collaborationManager.getCollaborationCaseDAOListByCollaborationDAO(collaborationDAO);
                for (CollaborationCaseDAO collaborationCaseDAO : collaborationCaseDAOList) {
                    Long caseId=collaborationCaseDAO.getCaseId();
                    if(collaborationCaseDAO.getStatus().equals(TestCaseStatusEnum.failed)){
                        incMap(caseId, reportIntegrationInfo.getRightCaseCountMap());
                    }else {
                        incMap(caseId, reportIntegrationInfo.getWrongCaseCountMap());
                    }
                }

                //记录协作报告测试环境数
                incMap(collaborationDAO.getOperatingSystem(),reportIntegrationInfo.getOperatingSystemCountMap());

                //记录协作报告工具使用数
                for (String testTool : collaborationDAO.getTestTools()) {
                    incMap(testTool,reportIntegrationInfo.getTestToolCountMap());
                }
            }
        }

        return RpcResultUtil.success(response);
    }


    private <T> void incMap(T key,Map<T,Integer> map){
        map.put(key,map.getOrDefault(key,0)+1);
    }

}
