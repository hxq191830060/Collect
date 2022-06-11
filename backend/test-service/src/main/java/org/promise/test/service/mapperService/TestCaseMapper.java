package org.promise.test.service.mapperService;

import org.promise.test.service.mapperService.po.TestCasePO;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:41
 * @description:
 */
public interface TestCaseMapper {

    Integer insert( TestCasePO testCasePO );


    Integer insertBatch( List<TestCasePO> testCasePOList );

    /**
     * 根据testId获取所有的test_case信息，不管test_case是否删除
     */
    List<TestCasePO> getAllTestCaseByTestId(Long testId);


    /**
     * 根据testId获取所有未被删除的test_case信息
     */
    List<TestCasePO> getUsableTestCaseByTestId(Long testId);


    Integer deleteAllTestCaseByTestId(Long testId);
}
