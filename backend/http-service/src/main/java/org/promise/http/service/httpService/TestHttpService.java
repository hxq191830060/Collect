package org.promise.http.service.httpService;

import org.promise.common.result.http.HttpResult;
import org.promise.http.service.vo.test.*;
import org.promise.test.service.api.request.WhetherWorkerHasAcceptTaskRequest;
import org.promise.test.service.api.response.WhetherWorkerHasAcceptTaskResponse;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/26 22:59
 */
public interface TestHttpService {

    /**
     * 测试工人接受一个测试任务
     * @param testVO 测试信息
     * @return 请求结果
     */
    HttpResult<String> createTest(TestVO testVO);

    /**
     * 查看当前工人是否已经接受该任务
     * @param workerId
     * @param taskId
     * @return
     */
    HttpResult<WhetherWorkerHasAcceptTaskResponse> whetherWorkerHasAcceptTask(Long workerId, Long taskId);

    /**
     * 当前工人取消当前任务
     * @param testId
     * @return
     */
    public HttpResult<String> cancelTest(Long testId,Long workerId) ;

    /**
     * 根据TestId获取当前test的所有详细信息
     * @param testId
     * @return
     */
    HttpResult<TestVO> getTestByTestId(Long testId);

    /**
     * 提交任务
     * @param testVO
     * @return
     */
    HttpResult<String> submitTest(TestVO testVO);

    /**
     * 对其他worker提交的测试宝报进行评价
     * @param testCollaborationRecordVO
     * @return
     */
    HttpResult<String> submitCollaborationRecord(TestCollaborationRecordVO testCollaborationRecordVO);

    /**
     * 获得当前task中的高分评价
     * @param taskId
     * @return
     */
    HttpResult<List<HighEvaluationTestVO>> getTestInfoWithHighEvaluationByTaskId(Long taskId);

    /**
     * 根据任务id查询当前任务的所有测试
     * @param taskId
     * @return
     */
    HttpResult<List<SimpleTestVO>> getSimpleTestByTaskId(Long taskId);

    /**
     * 提交协作报告
     * @param collaborationVO
     * @return
     */
    HttpResult<String> submitCollaboration(CollaborationVO collaborationVO);

    /**
     * 根据testId获取协作报告列表
     * @param testId
     * @return
     */
    HttpResult<List<CollaborationVO>> getCollaborationListByTestId(Long testId);

    /**
     * 获取协作报告详细信息
     * @param id
     * @return
     */
    HttpResult<CollaborationVO> getCollaborationById(Long id);

    /**
     * 查看当前用户是否填写该协同报告
     * @param testId
     * @param workerId
     * @return
     */
    HttpResult<Long> workerHasCollaboration(Long testId,Long workerId);
}
