package org.promise.publish.service.mapperService;

import org.promise.publish.service.mapperService.po.TaskFilePO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/2/26 23:10
 */
public interface TaskFileMapper {

    Integer insert(TaskFilePO taskFilePO);

    Integer insertBatch(List<TaskFilePO> taskDAOList);

    List<TaskFilePO> getTaskFileByTaskId(Long taskId);

    Integer deleteByTaskId(Long taskId);
}
