package org.promise.test.service.manager;

import org.promise.common.result.page.Page;
import org.promise.test.service.api.info.TestInfo;
import org.promise.test.service.manager.dao.TestBugImgDAO;
import org.promise.test.service.manager.dao.TestCaseDAO;
import org.promise.test.service.manager.dao.TestDAO;
import org.promise.test.service.mapperService.constant.TestStateEnum;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:53
 * @description:
 */
public interface TestManager {

    /**
     * 测试工人是否已经接受了测试任务
     * 如果已经接受了任务，返回其信息
     * 如果没有接受任务，返回null
     */
    TestDAO whetherWorkerHasAcceptTask(Long taskId,Long workerId);


    /**
     * 测试人员接受一个任务后，需要在test表中插入一条新的记录，并且要保证接受该测试任务的测试人员数目不能超过阈值
     * 这个方法采用乐观插入，如果成功将接收测试人的人数+1，那么就会创建一条新的记录
     */
    void createNewTestIfIncreaseSuccess(Long taskId,Long wordId,Integer expect);



    /**
     * 根据taskId,workerId,state查询对应的test记录
     */
    List<TestDAO> getTestDAOByWorkerIdAndTaskIdAndState( Long taskId, Long workerId, TestStateEnum state );


    /**
     * 工人取消接受的测试任务
     */
    void cancelTest (Long testId);



    Integer getWorkerAcceptedTaskNumber(Long workerId);


    TestDAO getTestInfoByTestId(Long testId);

    /**
     * 根据workerId,state分页查询符合条件的test信息
     */
    List<TestDAO> getTestDAOListByWorkerIdAndStateByPage( Long workerId, TestStateEnum state, Page page);

    List<TestDAO> getTestDAOListByWorkerIdAndStateListByPage( Long workerId, List<TestStateEnum> stateList, Page page);
    /**
     * 计算test表中，满足workerId和state的记录的条数
     */
    Integer countTestInfoByWorkerIdAndState(Long workerId,TestStateEnum state);

    /**
     * 计算test表中，满足workerId AND state为stateList中任意一个的记录的条数
     */
    Integer countTestInfoByWorkerIdAndStateList(Long workerId,List<TestStateEnum> stateList);


    List<TestDAO> getTestDAOListByTaskIdAndState(Long taskId,TestStateEnum state);


    /**
     * 提交测试报告
     * @param originalState 测试原本的状态(running or finish)
     * @param testDAO
     * @param testBugImgDAOList
     * @param testCaseDAOList
     */
    void submitTest(TestStateEnum originalState,TestDAO testDAO, List<TestBugImgDAO> testBugImgDAOList, List<TestCaseDAO> testCaseDAOList );


    List<TestDAO> getTestDAOListByTestIdList(List<Long> testIdList);
}
