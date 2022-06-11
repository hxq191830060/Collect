package org.promise.test.service.mapperService;

import org.apache.ibatis.annotations.Param;
import org.promise.test.service.mapperService.po.TestCollaborationRecordPO;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:42
 * @description:
 */
public interface TestCollaborationRecordMapper {

    Integer insert( TestCollaborationRecordPO testCollaborationRecordPO );

    Integer insertBatch( List<TestCollaborationRecordPO> list );

    /**
     * 根据testId获得所有的TestCollaborationRecord信息，不管是否删除
     */
    List<TestCollaborationRecordPO> getAllTestCollaborationRecordByTestId(Long testId);

    /**
     * 根据testId获得所有未删除的TestCollaborationRecord信息
     */
    List<TestCollaborationRecordPO> getUsableTestCollaborationRecordByTestId(Long testId);

    /**
     * 根据testId和workerId获得未删除的TestCollaborationRecord信息
     */
    TestCollaborationRecordPO getUsableTestCollaborationRecordByTestIdAndWorkerId(@Param ("testId")Long testId,@Param ("workerId")Long workerId);


    /**
     * 根据主键id去更新,只会更新2个字段——evaluation和recommendation
     */
    Integer updateById(TestCollaborationRecordPO testCollaborationRecordPO);
}
