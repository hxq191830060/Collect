package org.promise.test.service.mapperService;

import org.apache.ibatis.annotations.Param;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:42
 * @description:
 */
public interface TestCountMapper {

    /**
     * 对test_num字段进行乐观自增
     * @param taskId:指定更改的记录的taskId
     * @param expect:期望的test_num的值
     * @param addValue: 增加值
     */
    Integer optimismIncreaseTestNum( @Param("taskId")Long taskId, @Param ("expect")Integer expect, @Param ("addValue")Integer addValue);


    /**
     * @return 查询接受了指定任务的工人数目
     */
    Integer getWorkerNumberByTaskId(Long taskId);


    Integer decreaseNum(Integer num);
}
