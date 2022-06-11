package org.promise.test.service;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.common.constant.HttpCodes;
import org.promise.common.constant.RpcCodes;
import org.promise.common.result.http.HttpResult;
import org.promise.common.result.page.Page;
import org.promise.common.result.rpc.RpcResult;
import org.promise.test.service.api.api.TestService;
import org.promise.test.service.api.info.CollaborationInfo;
import org.promise.test.service.api.info.TestBugImgInfo;
import org.promise.test.service.api.info.TestCaseInfo;
import org.promise.test.service.api.info.TestCollaborationRecordInfo;
import org.promise.test.service.api.request.*;
import org.promise.test.service.api.response.*;
import org.promise.test.service.http.TestInfoController;
import org.promise.test.service.http.TestInfoListVO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: 黄相淇
 * @date: 2022年03月17日 16:10
 * @description:
 */
@Slf4j
@ExtendWith(SpringExtension.class) //导入spring测试框架
@SpringBootTest  //提供spring依赖注入
@Transactional  //事务管理，默认回滚,如果配置了多数据源记得指定事务管理器
public class TestServiceTest {

    @Resource
    private TestService testService;

    @Resource
    private TestInfoController testInfoController;

    @Test
    public void testWhetherWorkerHasAcceptTask(){
        WhetherWorkerHasAcceptTaskRequest request=new WhetherWorkerHasAcceptTaskRequest ();
        request.setTaskId (202L);
        for(long id=242L;id<=245;id+=1){
            request.setWorkerId (id);
            log.warn ("{}",testService.whetherWorkerHasAcceptTask (request));
        }
        request.setTaskId (203L);
        log.warn ("{}",testService.whetherWorkerHasAcceptTask (request));
    }

    @Test
    public void testCreateTest(){
        CreateTestRequest request=new CreateTestRequest ();

        //已经接受过该任务
        request.setWorkerId (242L);
        request.setTaskId (202L);
        request.setAllowedMaxWorkerCount (10);
        log.warn ("已经接受过任务的情况: {}",testService.createTest (request));

        request.setWorkerId (242L);
        request.setTaskId (203L);
        request.setAllowedMaxWorkerCount (10);
        log.warn ("已经接受过任务的情况: {}",testService.createTest (request));


        //任务不存在的情况
        request.setTaskId (200L);
        request.setWorkerId (242L);
        request.setAllowedMaxWorkerCount (10);
        log.warn ("任务不存在的情况: {}",testService.createTest (request));


        //工人已满的情况
        request.setTaskId (202L);
        request.setWorkerId (252L);
        request.setAllowedMaxWorkerCount (1);
        log.warn("工人已满的情况: {}",testService.createTest (request));


        //正常处理的情况
        request.setWorkerId (242L);
        request.setTaskId (209L);
        request.setAllowedMaxWorkerCount (10);
        log.warn ("正常创建任务的情况: {}",testService.createTest (request));

    }

    @Test
    public void testCancelTest(){
        CancelTestRequest request=new CancelTestRequest (  );
        request.setTestId (100L);
        request.setAllowCancelTime ("2022-03-28 18:00:00");

        log.warn ("超过最晚取消时间的情况: {}",testService.cancelTest (request));

        request.setTestId (100L);
        request.setAllowCancelTime ("2023-04-28 18:00:00");
        log.warn ("不存在testId对应的记录的情况: {}",testService.cancelTest (request));

        request.setTestId (95L);
        request.setAllowCancelTime ("2023-04-28 18:00:00");
        log.warn ("测试已提交的情况: {}",testService.cancelTest (request));

        request.setTestId (108L);
        request.setAllowCancelTime ("2023-04-28 18:00:00");
        log.warn ("测试已取消的情况: {}",testService.cancelTest (request));

        request.setTestId (112L);
        request.setAllowCancelTime ("2023-04-28 18:00:00");
        request.setWorkerId(252L);
        log.warn ("正确处理的情况: {}",testService.cancelTest (request));

    }

    @Test
    public void testGetWorkerAcceptedTaskNumber(){
        TestQueryRequest request=new TestQueryRequest ();
        request.setWorkerId (242L);
        log.warn ("接了0个任务: {}",testService.getWorkerAcceptedTaskNumber (request));

        request.setWorkerId (247L);
        log.warn ("接了0个任务: {}",testService.getWorkerAcceptedTaskNumber (request));

        request.setWorkerId (248L);
        log.warn ("接了0个任务: {}",testService.getWorkerAcceptedTaskNumber (request));

        request.setWorkerId (252L);
        log.warn ("接了3个任务: {}",testService.getWorkerAcceptedTaskNumber (request));
    }

    @Test
    public void testGetSimpleTestInfoByTestId(){
        TestQueryRequest request=new TestQueryRequest ();

        request.setTestId (100L);
        log.warn ("无效的testId的情况: {}",testService.getSimpleTestInfoByTestId (request));

        request.setTestId (95L);
        log.warn ("正确的情况: {}",testService.getSimpleTestInfoByTestId (request));
    }

    @Test
    public void getTestInfoByTestId(){
        TestQueryRequest request=new TestQueryRequest ();

        request.setTestId (100L);
        log.warn ("不存在的情况: {}",testService.getTestInfoByTestId (request));

        request.setTestId (95L);
        log.warn ("正确情况: {}",testService.getTestInfoByTestId (request));
    }

    @Test
    public void testGetTestInfoWithHighEvaluationByTaskId(){
        TestQueryRequest request=new TestQueryRequest ();
        request.setTaskId (100L);
        log.warn ("为空的taskId: {}",testService.getTestInfoWithHighEvaluationByTaskId (request));

        request.setTaskId (202L);
        log.warn ("正确的情况: {}",testService.getTestInfoWithHighEvaluationByTaskId (request));

    }

    @Test
    public void testGetWorkerTestInfo(){
        GetWorkerTestInfoRequest request=new GetWorkerTestInfoRequest ();
        request.setPage (new Page ( 1,10 ));
        request.setWorkerId (242L);
        List<String> list=new ArrayList<> (  );
        request.setStateList (list);
        list.add ("delete");
        log.warn ("状态值错误情况: {}",testService.getWorkerTestInfo (request));

        list.remove(list.size()-1);
        list.add ("running");
        log.warn ("只查running: {}",testService.getWorkerTestInfo (request));
        list.add ("finish");
        log.warn ("查running和finish: {}",testService.getWorkerTestInfo (request));

        request.setWorkerId (243L);
        log.warn ("worker没有任务的情况: {}",testService.getWorkerTestInfo (request));
    }

    @Test
    public void testSubmitCollaborationRecord(){
        SubmitCollaborationRecordRequest request=new SubmitCollaborationRecordRequest ();
        TestCollaborationRecordInfo testCollaborationRecordInfo=new TestCollaborationRecordInfo ();
        request.setTestCollaborationRecordInfo (testCollaborationRecordInfo);
        testCollaborationRecordInfo.setTestId (100L);
        testCollaborationRecordInfo.setWorkerId (243L);
        testCollaborationRecordInfo.setRecommendation ("不错");
        testCollaborationRecordInfo.setEvaluation (4);
        log.warn ("测试不存在的情况: {}",testService.submitCollaborationRecord (request));

        testCollaborationRecordInfo.setTestId (109L);
        log.warn ("测试报告未提交,无法评论的情况: {}",testService.submitCollaborationRecord (request));


        testCollaborationRecordInfo.setTestId (95L);
        testCollaborationRecordInfo.setWorkerId (243L);
        log.warn ("用户未接受测试任务,无法评价的情况: {}",testService.submitCollaborationRecord (request));

        testCollaborationRecordInfo.setTestId (95L);
        testCollaborationRecordInfo.setWorkerId (247L);
        log.warn ("正常评价: {}",testService.submitCollaborationRecord (request));


    }

    @Test
    public void testSubmitTest(){
        SubmitTestRequest request=new SubmitTestRequest (  );
        request.setWorkerId (242L);
        request.setTestId (95L);
        request.setConclusion ("再次总结");
        request.setLastSubmitTime ("2022-03-28 18:00:00");
        request.setOperatingSystem ("centos");
        request.setSuggestion ("再次建议去死");


        List<String> list=new ArrayList<>();
        list.add ("zly");
        list.add ("j8");
        request.setTestTools (list);


        List<TestBugImgInfo> testBugImgInfoList=new ArrayList<> ( );
        TestBugImgInfo testBugImgInfo=new TestBugImgInfo ();
        testBugImgInfo.setImgUrl ("www.alibaba.com");
        testBugImgInfoList.add(testBugImgInfo);
        TestBugImgInfo testBugImgInfo1=new TestBugImgInfo ();
        testBugImgInfo1.setImgUrl ("www.bytedance.com");
        testBugImgInfoList.add (testBugImgInfo1);
        request.setBugImgList (testBugImgInfoList);


        List<TestCaseInfo> testCaseInfoList=new ArrayList<> ();
        testCaseInfoList.add (new TestCaseInfo ( 1L,"failed","5s","severe","测试用例出错","重试" ));
        testCaseInfoList.add (new TestCaseInfo ( 2L,"failed","10min","fatal","测试用例执行错误","重试" ));
        request.setTestCaseList (testCaseInfoList);

        log.warn ("超过了最晚提交时间的情况: {}",testService.submitTest (request));

        request.setLastSubmitTime ("2023-03-28 18:00:00");

        request.setTestId (100L);
        log.warn ("testId不存在的情况: {}",testService.submitTest (request));

        request.setTestId (95L);
        request.setWorkerId (243L);
        log.warn ("测试不属于用户的情况: {}",testService.submitTest (request));

        request.setTestId (108L);
        request.setWorkerId (242L);
        log.warn ("测试已取消的情况: {}",testService.submitTest (request));

        request.setTestId (95L);
        request.setWorkerId (242L);
        log.warn ("正确提交: {}",testService.submitTest (request));
    }

    @Test
    public void testGetTestInfoListByTaskId(){
        log.warn ("空的taskId: {}",testInfoController.getTestInfoListByTaskId (100L));
        log.warn ("正确的情况: {}",testInfoController.getTestInfoListByTaskId (202L));
    }

    @Test
    public void testGetSimpleFinishedTestByTaskId(){
        TestQueryRequest request=new TestQueryRequest ();
        request.setTaskId (100L);

        log.warn ("测试不存在的情况: {}",testService.getSimpleFinishedTestByTaskId (request));

        request.setTaskId (202L);
        log.warn ("正确情况: {}",testService.getSimpleFinishedTestByTaskId (request));
    }

    @Test
    void getSimpleFinishedTestMapByTestIdListTest(){
        Set<Long> testIdSet=new HashSet<>();
        testIdSet.add(95L);
        testIdSet.add(96L);
        testIdSet.add(97L);
        TestBatchQueryRequest testBatchQueryRequest=new TestBatchQueryRequest(testIdSet);
        RpcResult<FinishedTestInfoMapResponse> simpleFinishedTestMapByTestIdList = testService.getSimpleFinishedTestMapByTestIdList(testBatchQueryRequest);
        Assertions.assertEquals(simpleFinishedTestMapByTestIdList.getCode(), RpcCodes.SUCCESS.getCode());
    }

    @Test
    void submitCollaborationTest(){
        CollaborationInfo collaborationInfo=new CollaborationInfo();
        collaborationInfo.setTestId(95L);
        collaborationInfo.setWorkerId(1L);
        List<String> testTools=new ArrayList<>();
        testTools.add("11");
        collaborationInfo.setTestTools(testTools);
        collaborationInfo.setConclusion("conclusion");
        collaborationInfo.setSuggestion("suggestion");
        collaborationInfo.setOperatingSystem("system");
        SubmitCollaborationRequest submitCollaborationRequest=new SubmitCollaborationRequest(collaborationInfo);
        RpcResult<SubmitCollaborationResponse> submitCollaborationResponseRpcResult = testService.submitCollaboration(submitCollaborationRequest);

        Assertions.assertEquals(submitCollaborationResponseRpcResult.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getCollaborationByIdTest(){
        CollaborationIdRequest collaborationIdRequest=new CollaborationIdRequest(30L);
        RpcResult<CollaborationInfoResponse> collaborationById = testService.getCollaborationById(collaborationIdRequest);

        Assertions.assertEquals(collaborationById.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getSimpleCollaborationListByTestIdTest(){
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTestId(159L);
        RpcResult<CollaborationInfoListResponse> simpleCollaborationListByTestId = testService.getSimpleCollaborationListByTestId(testQueryRequest);
        Assertions.assertEquals(simpleCollaborationListByTestId.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void whetherWorkerHasSubmitCollaborationTest(){
        WhetherWorkerHasSubmitCollaborationRequest whetherWorkerHasSubmitCollaborationRequest=
                new WhetherWorkerHasSubmitCollaborationRequest(159L,1L);
        RpcResult<WhetherWorkerHasSubmitCollaborationResponse> workerHasSubmitCollaboration = testService.whetherWorkerHasSubmitCollaboration(whetherWorkerHasSubmitCollaborationRequest);
        Assertions.assertEquals(workerHasSubmitCollaboration.getCode(),RpcCodes.SUCCESS.getCode());
    }

    @Test
    void getTestInfoByTestIdTest(){
        HttpResult<TestInfoListVO> testInfoByTestId = testInfoController.getTestInfoByTestId(95L);
        Assertions.assertEquals(testInfoByTestId.getCode(), HttpCodes.SUCCESS.getCode());
    }

    @Test
    void getTestCountByTaskIdTest(){
        TestQueryRequest testQueryRequest=new TestQueryRequest();
        testQueryRequest.setTaskId(202L);
        RpcResult<TestCountResponse> testCountByTaskId = testService.getTestCountByTaskId(testQueryRequest);
        Assertions.assertEquals(testCountByTaskId.getCode(),RpcCodes.SUCCESS.getCode());
    }
}
