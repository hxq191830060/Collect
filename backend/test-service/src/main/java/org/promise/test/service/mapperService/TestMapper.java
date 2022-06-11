package org.promise.test.service.mapperService;

import org.apache.ibatis.annotations.Param;
import org.promise.test.service.mapperService.constant.TestStateEnum;
import org.promise.test.service.mapperService.po.TestPO;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:42
 * @description:
 */
public interface TestMapper {

    Integer insert(TestPO testPO);

    /**
     *
     * @param taskId:指定taskId
     * @param workerId:指定workerId
     * @param state:指定的状态
     * @return 返回taskId,workerId,状态的记录条数
     */
    Integer countTestByWorkerIdAndTaskIdAndState( @Param ("taskId")Long taskId, @Param ("workerId")Long workerId, @Param ("state") TestStateEnum state );


    List<TestPO> getTestByWorkerIdAndTaskIdAndState(@Param ("taskId")Long taskId,@Param ("workerId")Long workerId,@Param ("state") TestStateEnum state);

    /**
     * 根据testId更新test表中的记录信息(test_id,task_id,worker_id)这三个字段是不会更新饿
     * 要更新记录的test_id存放在updateTest中
     * @param updateTest:存储更新值
     */
    Integer updateByTestId(TestPO updateTest);


    Integer countTestByWorkerIdAndState(@Param("workerId")Long workerId,@Param ("state")TestStateEnum state);


    TestPO getTestInfoByTestId(Long testId);

    List<TestPO> getTestListByWorkerIdAndStateByPage(@Param ("workerId")Long workerId,@Param ("state") TestStateEnum state,@Param ("begin")Integer begin,@Param ("pageSize")Integer pageSize);

    List<TestPO> getTestListByWorkerIdAndStateListByPage ( @Param ("workerId")Long workerId, @Param ("list") List<TestStateEnum> stateList, @Param ("begin")Integer begin, @Param ("pageSize")Integer pageSize);


    List<TestPO> getTestListByTaskIdAndState(@Param ("taskId")Long taskId,@Param ("state")TestStateEnum state);


    Integer countTestInfoByWorkerIdAndStateList(@Param ("workerId")Long workerId,@Param ("list")List<TestStateEnum> stateList);


    TestPO getTestInfoByTaskIdAndWorkerAndStateList(@Param ("taskId")Long taskId,@Param ("workerId")Long workerId,@Param ("list")List<TestStateEnum> stateList);

    List<TestPO> getTestListByTestIdList(@Param ("list")List<Long> testIdList);
}
