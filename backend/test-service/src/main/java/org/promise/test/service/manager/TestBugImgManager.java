package org.promise.test.service.manager;

import org.promise.test.service.manager.dao.TestBugImgDAO;

import java.util.List;

public interface TestBugImgManager {

    /**
     * 根据testId获得获得所有未被删除的test_bug_img信息
     */
    List<TestBugImgDAO> getUsableBugImgListByTestId(Long testId);
}
