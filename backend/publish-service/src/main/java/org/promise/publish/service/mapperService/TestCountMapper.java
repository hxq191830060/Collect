package org.promise.publish.service.mapperService;

import org.promise.publish.service.mapperService.po.TestCountPO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/3/9 16:42
 */
public interface TestCountMapper {

    Integer insert(TestCountPO testCountPO);

    Integer countWorkerByTaskId(Long taskId);

    List<TestCountPO> batchCountWorkerByTaskId(List<Long> taskIdList);
}
