package org.promise.test.service.manager;

import org.promise.test.service.manager.dao.TestCaseDAO;

import java.util.List;

public interface TestCaseManager {

    /**
     * 根据testId返回所有未被删除的test_case信息
     */
    List<TestCaseDAO> getUsableTestCaseListByTestId(Long testId);
}
