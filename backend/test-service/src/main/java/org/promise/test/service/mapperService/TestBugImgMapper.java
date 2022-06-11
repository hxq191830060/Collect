package org.promise.test.service.mapperService;

import org.promise.test.service.mapperService.po.TestBugImgPO;

import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:41
 * @description:
 */
public interface TestBugImgMapper {
    Integer insert( TestBugImgPO testBugImgPO );


    Integer insertBatch(List<TestBugImgPO> testBugImgPOList);

    /**
     * 根据testId查出所有的test_bug_img信息，不管这个test_bug_img是否删除
     */
    List<TestBugImgPO> getAllBugImgByTestId(Long testId);


    /**
     * 根据testId查找出所有的未被删除的test_bug_img信息
     */
    List<TestBugImgPO> getUsableBugImgByTestId(Long testId);


    /**
     *  删除testId下所有的test_bug_img信息
     */
    Integer deleteAllBugImgByTestId(Long testId);
}
