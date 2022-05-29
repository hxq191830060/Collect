package org.promise.test.service.manager;

import org.promise.test.service.manager.dao.TestCollaborationRecordDAO;

import java.util.List;

public interface TestCollaborationRecordManager {

    /**
     * 根据testId，获得所有未被删除的test_collaboration_record信息
     */
    List<TestCollaborationRecordDAO> getUsableTestCollaborationRecordListByTestId(Long testId);


    /**
     * 提交对某个测试报告的评论
     */
    void submitTestCollaborationRecord(TestCollaborationRecordDAO testCollaborationRecordDAO);
}
