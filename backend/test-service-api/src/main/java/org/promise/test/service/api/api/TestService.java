package org.promise.test.service.api.api;

import org.promise.common.result.rpc.RpcResult;
import org.promise.test.service.api.request.*;
import org.promise.test.service.api.response.*;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:28
 * @description:
 */
public interface TestService {

    /**
     * @param request
     * 测试工人接受一个新的测试任务
     */
    RpcResult<CreateTestResponse> createTest( CreateTestRequest request );

    /**
     * 指定的工人是否已经接受了指定的任务
     */
    RpcResult<WhetherWorkerHasAcceptTaskResponse> whetherWorkerHasAcceptTask( WhetherWorkerHasAcceptTaskRequest request );



    /**
     * 工人取消接受的任务
     */
    RpcResult<CancelTestResponse> cancelTest(CancelTestRequest request);

    /**
     * 获取工人接受的task数目
     */
    RpcResult<Integer> getWorkerAcceptedTaskNumber( TestQueryRequest request );

    /**
     * 获取worker相关的test信息(分页查询)
     */
    RpcResult<TestInfoResponse> getWorkerTestInfo( GetWorkerTestInfoRequest request);



    /**
     * 根据testId获取test的全部详细信息
     */
    RpcResult<TestInfoResponse> getTestInfoByTestId(TestQueryRequest request);

    /**
     * 根据testId获取test的简略信息
     */
    RpcResult<TestInfoResponse> getSimpleTestInfoByTestId(TestQueryRequest request);

    /**
     * worker对其他worker提交的测试报告进行评价
     * 如果原本没有评价过，这里会创建一条新的评价
     * 如果原本评价过，会覆盖原来的评价
     */
    RpcResult<SubmitCollaborationRecordResponse> submitCollaborationRecord( SubmitCollaborationRecordRequest request);


    RpcResult<SubmitTestResponse> submitTest(SubmitTestRequest request);


    /**
     * 根据taskId查找该任务下评分较高的测试
     */
    RpcResult<FinishedTestInfoResponse> getTestInfoWithHighEvaluationByTaskId( TestQueryRequest request);


    /**
     * 根据taskId查看已经完成的报告信息(只有testId,workerId,均分,finishTime)
     */
    RpcResult<FinishedTestInfoResponse> getSimpleFinishedTestByTaskId(TestQueryRequest request);


    RpcResult<FinishedTestInfoMapResponse> getSimpleFinishedTestMapByTestIdList(TestBatchQueryRequest request);


    /**
     * 提交协作报告,没有则插入，有则修改，增和改用同一个接口
     * @param request
     * @return
     */
    RpcResult<SubmitCollaborationResponse> submitCollaboration(SubmitCollaborationRequest request);

    /**
     * 根据collaborationId唯一查询
     * @param collaborationIdRequest
     * @return
     */
    RpcResult<CollaborationInfoResponse> getCollaborationById(CollaborationIdRequest collaborationIdRequest);

    /**
     * 根据testId查询未被删除的CollaborationInfo列表，不带case
     * @param testQueryRequest
     * @return
     */
    RpcResult<CollaborationInfoListResponse> getSimpleCollaborationListByTestId(TestQueryRequest testQueryRequest);


    RpcResult<WhetherWorkerHasSubmitCollaborationResponse> whetherWorkerHasSubmitCollaboration(WhetherWorkerHasSubmitCollaborationRequest request);

    RpcResult<TestCountResponse> getTestCountByTaskId(TestQueryRequest testQueryRequest);
}
